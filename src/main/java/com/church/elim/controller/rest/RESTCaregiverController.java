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
import com.church.elim.domain.Caregiver;
import com.church.elim.domain.Person;
import com.church.elim.domain.views.CaregiverAdminView;
import com.church.elim.repository.CaregiverRepository;
import com.church.elim.repository.PersonRepository;
import com.church.elim.service.DownloadService;
import com.church.elim.service.CaregiverService;
import com.church.elim.utils.ElimMessage;
import com.church.elim.validation.CaregiverValidator;

@Controller
public class RESTCaregiverController {
	@Autowired
	CaregiverRepository caregiverRepo;
	@Autowired
	PersonRepository personRepo;
	@Autowired
	private ElimMessage messageSource;
	@Autowired
	CaregiverService caregiverService;
	@Resource(name="downloadService")
	private DownloadService downloadService;
	@Autowired
	@Qualifier("caregiverValidator")
	private CaregiverValidator caregiverValidator;


	public static final String SUCCESS_KEY = "success";
	public static final String RESULT_KEY = "result";
	public static final String ID_KEY = "id";

	@InitBinder
	public void initVarietyBinder(final WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(Person.class, (PropertyEditor) new PersonPropertyEditor(personRepo));
	}
	
	@InitBinder("caregiver")
	public void initCaregiverBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(caregiverValidator);
	}

	@RequestMapping(value = "/rest/caregivers/{id}", method = RequestMethod.GET)
	public @ResponseBody 
	Caregiver getCaregiver(@PathVariable("id") Long id, HttpServletResponse response, HttpServletRequest request) {
		Caregiver p = caregiverRepo.findOne(id); 
		return p;
	}

	@RequestMapping(value = "/rest/caregivers/{id}/photo", method = RequestMethod.GET)
	public @ResponseBody 
	ResponseEntity<byte[]> getCaregiverPhoto(@PathVariable("id") Long id, HttpServletResponse response, HttpServletRequest request) {
		Caregiver p = caregiverRepo.findOne(id);
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

	@RequestMapping(value = "/rest/caregivers", method = RequestMethod.GET)
	public @ResponseBody 
	List<Caregiver> getAllCaregivers(HttpServletResponse response, HttpServletRequest request, Model uiModel) {
		return caregiverRepo.findAll();
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/rest/caregivers/basic", method = RequestMethod.GET)
	public @ResponseBody 
	List getAllCaregiversByColumns(HttpServletResponse response, HttpServletRequest request) {
		return caregiverService.getAllCaregiversAdminView();
	}

	@RequestMapping(value = "/rest/caregivers/update-form/{id}", produces = "text/html", 
			method = RequestMethod.GET)
	public String getUpdateForm(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("caregiver",caregiverRepo.findOne(id));
		uiModel.addAttribute("maritalStatusList",caregiverService.getMaritalStatusList());
		uiModel.addAttribute("baptistsList",caregiverService.getBaptistsList());
		uiModel.addAttribute("studiesList",caregiverService.getEducationList());
		return "caregivers/update-form";
	}

	@RequestMapping(value = "/rest/caregivers/names-datalist", produces = "text/html", 
			method = RequestMethod.GET)
	public String getCaregiversNamesDatalist(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("caregivers",caregiverRepo.findAll());
		return "caregivers/names-datalist";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.POST, 
	value="/rest/caregivers/delete/{id}",
	produces="text/html")
	public @ResponseBody String removeCaregiver(@PathVariable Long id, 
			Model uiModel,
			HttpServletResponse httpResponse) {
		Caregiver caregiver = caregiverRepo.findOne(id);
		if(caregiver==null){
			return messageSource.getMessage("caregiver.exception.not_found", id);
		}
		personRepo.delete(caregiver.getPerson());
		httpResponse.setStatus(HttpServletResponse.SC_OK);
		return messageSource.getMessage("caregiver.delete.success", caregiver);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/rest/caregivers/create", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> create(@Valid @ModelAttribute("caregiver") Caregiver caregiver, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {

		Map<String, Object> out = getActionOutput(bindingResult);
		if (out.get(SUCCESS_KEY).equals(false))
			return out;

		try{
			caregiverService.addCaregiver(caregiver);
			out.put(RESULT_KEY, messageSource.getMessage("caregiver.save.success"));
			out.put(ID_KEY, caregiver.getId());
			uiModel.asMap().clear();
			uiModel.addAttribute(caregiver);
		} catch(Exception e) {
			Map<String, String> errors = new LinkedHashMap<String, String>();

			if (e instanceof IllegalArgumentException) 
				errors.put("cnp", e.getMessage());
			else{
				errors.put("", messageSource.getMessage("caregiver.save.exception.unknown_reason",
						e.getMessage()));
				System.out.println(e.toString());
			}
				
			out.put(SUCCESS_KEY, false);
			out.put(RESULT_KEY, errors);
		}

		return out;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/rest/caregivers/update", method = RequestMethod.POST)	
	public @ResponseBody
	Map<String, Object> update(@Valid @ModelAttribute("caregiver") Caregiver caregiver, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {	
		Map<String, Object> out = getActionOutput(bindingResult);
		if (out.get(SUCCESS_KEY).equals(false))
			return out;
		try{
			Caregiver updatedCaregiver = caregiverService.updateCaregiver(caregiver);
			out.put(RESULT_KEY, messageSource.getMessage("caregiver.update.success"));
			out.put(ID_KEY, caregiver.getId());
			uiModel.asMap().clear();
			uiModel.addAttribute("caregiver",updatedCaregiver);
			uiModel.addAttribute("baptistsList",caregiverService.getBaptistsList());
		} catch(Exception e) {
			Map<String, String> errors = new LinkedHashMap<String, String>();
			if (e instanceof IllegalArgumentException) 
				errors.put("cnp", e.getMessage());
			else{
				errors.put("", messageSource.getMessage("caregiver.save.exception.unknown_reason",
						e.getMessage()));
				System.out.println(e.toString());
			}
			out.put(SUCCESS_KEY, false);
			out.put(RESULT_KEY, errors);
		}
		return out;
	}

	@SuppressWarnings("deprecation")
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/rest/caregivers/{id}/photo", method = RequestMethod.POST)	
	public @ResponseBody
	Map<String, Object> update(@PathVariable("id") Long id, @RequestParam("photo") MultipartFile photo) {	
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		out.put(SUCCESS_KEY, true);
		Caregiver caregiver = caregiverRepo.findOne(id);
		try {
			Blob blob = Hibernate.createBlob(photo.getInputStream());
			caregiver.getPerson().setPhoto(blob);
			caregiverRepo.saveAndFlush(caregiver);
			out.put(RESULT_KEY, messageSource.getMessage("caregiver.update_photo.success"));
		} catch(Exception e) {
			out.put(SUCCESS_KEY, false);
			out.put(RESULT_KEY, e);
		}

		return out;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/rest/caregivers/upload-excel", method = RequestMethod.POST)
	@ResponseBody
	public String importFromExcel (@RequestParam("fileToUpload") MultipartFile multipartFile,
			HttpServletRequest request) {
		try {
			return caregiverService.getCaregiversFromExcel(multipartFile.getInputStream());
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

	/* This REST method returns the caregivers data used in the jquery 
	 * DataTables plugin. 
	 */
	@Secured("ROLE_USER")
	@RequestMapping(value = "/rest/caregivers/data", 
	method = RequestMethod.POST,
	produces="application/json")
	public @ResponseBody
	DataTablesResponse<CaregiverAdminView> getTableData(@RequestBody DataTablesRequest dtReq, HttpServletResponse response) {
		DataTablesResponse<CaregiverAdminView> dtResponse =  new DataTablesResponse<CaregiverAdminView>(); 
		List<CaregiverAdminView> filteredData = caregiverService.getCaregiversAdminView(dtReq);
		List<CaregiverAdminView> page = new ArrayList<CaregiverAdminView>();
		int totalRecords = filteredData.size();
		int start = 0;
		for(CaregiverAdminView caregiver:filteredData){
			start++;
			if(start>=dtReq.displayStart){
				page.add(caregiver);
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
	 * Make sure this method doesn't return any model. Otherwise, you'll get 
	 * an "IllegalStateException: getOutputStream() has already been called for this response"
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "/rest/caregivers/download/xls", method = RequestMethod.GET)
	public @ResponseBody
	void getExcelReport(@RequestParam("dataTablesRequest") String dtReqJSON, HttpServletResponse response) throws ClassNotFoundException, JsonParseException, JsonMappingException, IOException {
		System.out.println("Received request to download report as an XLS");
		ObjectMapper mapper = new ObjectMapper();
		DataTablesRequest dtReq = mapper.readValue(dtReqJSON, DataTablesRequest.class);

		// Delegate to downloadService. Make sure to pass an instance of HttpServletResponse 
		downloadService.downloadXLS(response, caregiverService.getFilteredData(dtReq));
	}
}
