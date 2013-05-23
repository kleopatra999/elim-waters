package com.church.elim.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import org.springframework.web.multipart.MultipartFile;

import com.church.elim.domain.Caregiver;
import com.church.elim.domain.Person;
import com.church.elim.repository.CaregiverRepository;
import com.church.elim.repository.PersonRepository;
import com.church.elim.service.DownloadService;
import com.church.elim.service.CaregiverService;

@Secured("ROLE_USER")
@SessionAttributes(value={"maritalStatus","studiesList"})
@Controller
public class CaregiverController {
	@Autowired
	private PersonRepository personRepo;
	
	@Autowired
	private CaregiverRepository caregiverRepo;
	
	@Resource(name="downloadService")
	private DownloadService downloadService;
	
	@Autowired
	CaregiverService caregiverService;
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/caregivers/create", method = RequestMethod.GET)
	public ModelAndView addNewMember(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("caregivers/save");
		Caregiver caregiver = new Caregiver();
		Person person = new Person();
		caregiver.setPerson(person);
		modelAndView.addObject("caregiver", caregiver);
		return modelAndView;
	} 

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/caregivers/create", method = RequestMethod.POST)
	public String create(
			@Valid @ModelAttribute("caregiver") Caregiver caregiver,
			BindingResult result) throws Exception {

		if (result.hasErrors()) {
			return "caregivers/save";
		}
		caregiverService.addCaregiver(caregiver);
		return "redirect:/caregivers/list";

	}

	/*@RequestMapping( value = "/caregivers/list",params = { "page", "size" },method = RequestMethod.GET )
	public ModelAndView listPaginatedResults(@RequestParam( "page" ) int page, 
			@RequestParam( "size" ) int size) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("caregivers/list");
		if (!modelAndView.getModel().containsKey("caregivers")){
			modelAndView.addObject("caregivers", caregiverDAO.findPaginated(page,size));
		}
		return modelAndView;
	}*/
	
	@RequestMapping( value = "/caregivers/list", method = RequestMethod.GET )
	public ModelAndView listCaregivers() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("caregivers/list");
		modelAndView.addObject("caegivers", caregiverRepo.findAll());
		return modelAndView;
	}
	
	@RequestMapping( value = "/caregivers/filter", method = RequestMethod.GET )
	public ModelAndView selectFilterCriteria() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("caregivers/filter");
		return modelAndView;
	}
	
	@RequestMapping(value = "/caregivers/{id}", params = "form", produces = "text/html")
	public String updateCaregiver(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute(caregiverRepo.findOne(id));
		return "caregivers/show";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/caregivers/{id}", params="delete", produces = "text/html")
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		Caregiver caregiver = caregiverRepo.findOne(id);

		System.out.println("delete "+caregiver.getId() + caregiver.getPerson().getFirstName());
		caregiverRepo.delete(caregiver);
		
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		
		return "redirect:/caregivers/list";
	}
	
	@RequestMapping(value = "/caregivers/{id}", params="copy", method = RequestMethod.GET)
	public String copyMember(@PathVariable("id") Long id, Model uiModel){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("caregivers/save");
		Caregiver caregiver = caregiverRepo.findOne(id);
		System.out.println("copying from " + caregiver.getPerson().getLastName()+caregiver.getId());
		
		uiModel.addAttribute(caregiverRepo.findOne(id));
		uiModel.asMap().clear();
		return "redirect:/caregivers/save?id=" + id;
	} 

	@RequestMapping(value = "/caregivers/{id}", params="update", method = RequestMethod.GET)
	public ModelAndView updateMember(@PathVariable("id") Long id){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("caregivers/update");
		Caregiver caregiver = caregiverRepo.findOne(id);
		System.out.println("updating from " + caregiver.getPerson().getLastName()+caregiver.getId());

		modelAndView.addObject("caregiver", caregiver);
		modelAndView.addObject("caregivers", caregiverRepo.findAll());
		modelAndView.addObject("baptistsList",caregiverService.getBaptistsList());
		return modelAndView;
	} 

	@RequestMapping(value = "/caregivers/update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("caregiver") Caregiver caregiver, @RequestParam("photo") MultipartFile photo, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws Exception {
		
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute(caregiver);
			return "caregivers/update";
		}
		
		uiModel.asMap().clear();
		caregiverService.addCaregiver(caregiver);
		return "redirect:/caregivers/list" ;
	}

	
	@RequestMapping(value = "/caregivers/import", params="excel", method = RequestMethod.GET)
	public ModelAndView importFromExcel() throws IOException{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		try{
			caregiverService.importDataFromExcelFile("F:/olda calc/Biserica/fisiere_xls/EvidentaMembrii.xlsx");
		}catch(IOException e){
			System.out.println(e);
			throw e;
		} catch (ParseException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return modelAndView;
	} 

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		} catch (UnsupportedEncodingException uee) {}
		return pathSegment;
	}
}
