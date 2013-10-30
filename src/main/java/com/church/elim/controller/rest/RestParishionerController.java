package com.church.elim.controller.rest;

import java.beans.PropertyEditor;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.church.elim.controller.DataTablesRequest;
import com.church.elim.controller.DataTablesResponse;
import com.church.elim.controller.PersonPropertyEditor;
import com.church.elim.domain.Parishioner;
import com.church.elim.domain.Person;
import com.church.elim.domain.views.ParishionerAdminView;
import com.church.elim.repository.ParishionerRepository;
import com.church.elim.repository.PersonRepository;
import com.church.elim.service.DownloadService;
import com.church.elim.service.ParishionerService;
import com.church.elim.utils.ElimMessage;
import com.church.elim.validation.ParishionerValidator;

@Controller
public class RestParishionerController {
	@Autowired
	ParishionerRepository parishionerRepo;
	@Autowired
	PersonRepository personRepo;
	@Autowired
	private ElimMessage messageSource;
	@Autowired
	ParishionerService parishionerService;
	@Resource(name="downloadService")
	private DownloadService downloadService;
	@Autowired
	@Qualifier("parishionerValidator")
	private ParishionerValidator parishionerValidator;


	public static final String SUCCESS_KEY = "success";
	public static final String RESULT_KEY = "result";
	public static final String ID_KEY = "id";

	@InitBinder
	public void initVarietyBinder(final WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(Person.class, (PropertyEditor) new PersonPropertyEditor(personRepo));
	}
	
	@InitBinder("parishioner")
	public void initParishionerBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(parishionerValidator);
	}

	@RequestMapping(value = "/rest/parishioners/{id}", method = RequestMethod.GET)
	public @ResponseBody 
	Parishioner getParishioner(@PathVariable("id") Long id, HttpServletResponse response, HttpServletRequest request) {
		Parishioner p = parishionerRepo.findOne(id); 
		return p;
	}

	@RequestMapping(value = "/rest/parishioners/{id}/photo", method = RequestMethod.GET)
	public @ResponseBody 
	ResponseEntity<byte[]> getParishionerPhoto(@PathVariable("id") Long id, HttpServletResponse response, HttpServletRequest request) {
		Parishioner p = parishionerRepo.findOne(id);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "image/jpeg");

		try {
			Blob photo = p.getPerson().getPhoto();
			int photoLength = (int) photo.length();
			return new ResponseEntity<byte[]>(photo.getBytes(1, photoLength), responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<byte[]>(new byte[]{}, responseHeaders, HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/rest/parishioners", method = RequestMethod.GET)
	public @ResponseBody 
	List<Parishioner> getAllParishioners(HttpServletResponse response, HttpServletRequest request, Model uiModel) {
		return parishionerRepo.findAll();
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/rest/parishioners/basic", method = RequestMethod.GET)
	public @ResponseBody 
	List getAllParishionersByColumns(HttpServletResponse response, HttpServletRequest request) {
		return parishionerService.getAllParishionersAdminView();
	}

	@RequestMapping(value = "/rest/parishioners/update-form/{id}", produces = "text/html", 
			method = RequestMethod.GET)
	public String getUpdateForm(@PathVariable("id") Long id, Model uiModel) {
		Parishioner p = parishionerRepo.findOne(id);
		uiModel.addAttribute("parishioner", p);
		uiModel.addAttribute("maritalStatusList",parishionerService.getMaritalStatusList());
		uiModel.addAttribute("baptistsList",parishionerService.getBaptistsList());
		uiModel.addAttribute("studiesList",parishionerService.getEducationList());
		return "parishioners/update-form";
	}

	@RequestMapping(value = "/rest/parishioners/names-datalist", produces = "text/html", 
			method = RequestMethod.GET)
	public String getParishionersNamesDatalist(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("parishioners",parishionerRepo.findAll());
		return "parishioners/names-datalist";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.POST, 
	value="/rest/parishioners/delete/{id}",
	produces="text/html")
	public @ResponseBody String removeParishioner(@PathVariable Long id, 
			Model uiModel,
			HttpServletResponse httpResponse) {
		Parishioner parishioner = parishionerRepo.findOne(id);
		if(parishioner==null){
			return messageSource.getMessage("parishioner.exception.not_found", id);
		}
		//personRepo.delete(parishioner.getPerson());
		parishionerService.removeParishioner(parishioner);
		httpResponse.setStatus(HttpServletResponse.SC_OK);
		return messageSource.getMessage("parishioner.delete.success", parishioner);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/rest/parishioners/create", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> create(@Valid @ModelAttribute("parishioner") Parishioner parishioner, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {

		Map<String, Object> out = getActionOutput(bindingResult);
		if (out.get(SUCCESS_KEY).equals(false))
			return out;

		try{
			parishionerService.addParishioner(parishioner);
			out.put(RESULT_KEY, messageSource.getMessage("parishioner.create.success"));
			out.put(ID_KEY, parishioner.getId());
			uiModel.asMap().clear();
			uiModel.addAttribute(parishioner);
		} catch(Exception e) {
			Map<String, String> errors = new LinkedHashMap<String, String>();

			if (e instanceof IllegalArgumentException) 
				errors.put("cnp", e.getMessage());
			else{
				errors.put("", messageSource.getMessage("parishioner.create.exception.unknown_reason",
						e.getMessage()));
				e.printStackTrace();
			}
			
			out.put(SUCCESS_KEY, false);
			out.put(RESULT_KEY, errors);
		}

		return out;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/rest/parishioners/update", method = RequestMethod.POST)	
	public @ResponseBody
	Map<String, Object> update(@Valid @ModelAttribute("parishioner") Parishioner parishioner, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {	
		Map<String, Object> out = getActionOutput(bindingResult);
		if (out.get(SUCCESS_KEY).equals(false))
			return out;
		try{
			Parishioner updatedParishioner = parishionerService.updateParishioner(parishioner);
			out.put(RESULT_KEY, messageSource.getMessage("parishioner.update.success"));
			out.put(ID_KEY, parishioner.getId());
			uiModel.asMap().clear();
			uiModel.addAttribute("parishioner",updatedParishioner);
			uiModel.addAttribute("baptistsList",parishionerService.getBaptistsList());
		} catch(Exception e) {
			Map<String, String> errors = new LinkedHashMap<String, String>();
			if (e instanceof IllegalArgumentException) 
				errors.put("cnp", e.getMessage());
			else{
				errors.put("", messageSource.getMessage("parishioner.update.exception.unknown_reason",
						e.getMessage()));
				e.printStackTrace();
				System.out.println("Parishioner update failed: "+ e);
			}
			out.put(SUCCESS_KEY, false);
			out.put(RESULT_KEY, errors);
		}
		return out;
	}

	@SuppressWarnings("deprecation")
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/rest/parishioners/{id}/photo", method = RequestMethod.POST)	
	public @ResponseBody
	Map<String, Object> update(@PathVariable("id") Long id, @RequestParam("photo") MultipartFile photo) {	
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(SUCCESS_KEY, true);
		Parishioner parishioner = parishionerRepo.findOne(id);
		try {
			Blob blob = Hibernate.createBlob(photo.getInputStream());
			parishioner.getPerson().setPhoto(blob);
			parishionerService.updateParishioner(parishioner);
			out.put(RESULT_KEY, messageSource.getMessage("parishioner.update_photo.success"));
		} catch(Exception e) {
			out.put(SUCCESS_KEY, false);
			out.put(RESULT_KEY, e);
		}

		return out;
	}

	/* Upadte baptist for a given parishioner.
	 * baptistName - a string representing the first name and last name of the baptist person, 
	 * separated through a white space. If the persons table contains the given name then the
	 * baptist is updated to that name, otherwise we're creating a new person with this name. 
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/rest/parishioners/{parishionerId}/baptist/update", method = RequestMethod.POST)	
	public @ResponseBody
	Map<String, Object> updateBaptist(@PathVariable("parishionerId") Long parishionerId, 
			@RequestBody String baptistName) {	
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(SUCCESS_KEY, true);
		try {
			parishionerService.updateBaptist(parishionerId, baptistName);
			out.put(RESULT_KEY,"Successfully updated baptist!");
		} catch(Exception e) {
			out.put(SUCCESS_KEY, false);
			out.put(RESULT_KEY, e);
		}

		return out;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/rest/parishioners/update-church", method = RequestMethod.POST)
	public @ResponseBody 
	String updateChurch(@RequestBody Parishioner[] parishioners, Model uiModel) {

		try{
			for(Parishioner parishioner: parishioners)
				parishionerRepo.updateChurch(parishioner.getId(), parishioner.getChurch());
		} catch (Exception e){
			return "{\"result\": \"" + e.getMessage() + "\"}";
		}

		return "{\"result\": \""+messageSource.getMessage("parishioner.create.success")+"\"}";
	}


	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/rest/parishioners/upload-excel", method = RequestMethod.POST)
	@ResponseBody
	public String importFromExcel (@RequestParam("fileToUpload") MultipartFile multipartFile,
			HttpServletRequest request) {
		try {
			return parishionerService.getParishionersFromExcel(multipartFile.getInputStream());
		} catch (IOException e) {
			return e.toString();
		}

	}

	private Map<String, Object> getActionOutput (BindingResult bindingResult) {
		Map<String, Object> out = new LinkedHashMap<String, Object>();

		if (bindingResult.hasErrors()) {
			String fieldName;
			Map<String, String> errors = new LinkedHashMap<String, String>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				fieldName = error.getField();
				errors.put(fieldName.substring(fieldName.indexOf(".")+1), error.getDefaultMessage());
			}
			out.put(SUCCESS_KEY, false);
			out.put(RESULT_KEY, errors);
			return out;	
		}
		out.put(SUCCESS_KEY, true);
		return out;
	}

	/* This REST method returns the parishioners data used in the jquery 
	 * DataTables plugin. 
	 */
	@Secured("ROLE_USER")
	@RequestMapping(value = "/rest/parishioners/data", 
	method = RequestMethod.POST,
	produces="application/json")
	public @ResponseBody
	DataTablesResponse<ParishionerAdminView> getTableData(@RequestBody DataTablesRequest dtReq, HttpServletResponse response) {
		DataTablesResponse<ParishionerAdminView> dtResponse =  new DataTablesResponse<ParishionerAdminView>(); 
		List<ParishionerAdminView> filteredData = parishionerService.getParishionersAdminView(dtReq);
		List<ParishionerAdminView> page = new ArrayList<ParishionerAdminView>();
		int totalRecords = filteredData.size();
		int start = 0;
		for(ParishionerAdminView parishioner:filteredData){
			start++;
			if(start>=dtReq.displayStart){
				page.add(parishioner);
			}
			if(page.size()==dtReq.displayLength){
				break;
			}
		}

		dtResponse.data = page;
		dtResponse.totalDisplayRecords=totalRecords;
		dtResponse.totalRecords=totalRecords;
		/* We must send the echo back to the client otherwise it will stuck 
		 * without any error message. 
		 */
		dtResponse.echo=dtReq.echo;

		return dtResponse;
	}

	/**
	 * Downloads the report as an Excel format. 
	 * Make sure this method doesn't return any model. Otherwise, you'll create
	 * an "IllegalStateException: getOutputStream() has already been called for this response"
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "/rest/parishioners/download/xls", method = RequestMethod.GET)
	public @ResponseBody
	void getExcelReport(@RequestParam("dataTablesRequest") String dtReqJSON, HttpServletResponse response) throws ClassNotFoundException, JsonParseException, JsonMappingException, IOException {
		System.out.println("Received request to download report as an XLS");
		ObjectMapper mapper = new ObjectMapper();
		DataTablesRequest dtReq = mapper.readValue(dtReqJSON, DataTablesRequest.class);

		// Delegate to downloadService. Make sure to pass an instance of HttpServletResponse 
		downloadService.downloadXLS(response, parishionerService.getFilteredData(dtReq));
	}
}
