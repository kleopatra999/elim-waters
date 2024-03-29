package com.church.elim.web.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.beans.PropertyEditor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.church.elim.web.components.ParishionerTable;
import com.church.elim.web.components.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import org.springframework.web.multipart.MultipartFile;

import com.church.elim.domain.Education;
import com.church.elim.domain.MaritalStatus;
import com.church.elim.domain.Parishioner;
import com.church.elim.domain.Person;
import com.church.elim.repository.ParishionerRepository;
import com.church.elim.repository.PersonRepository;
import com.church.elim.service.DownloadService;
import com.church.elim.service.ParishionerService;
import com.church.elim.service.PersonService;

@Secured("ROLE_USER")
@SessionAttributes(value = {"maritalStatus", "studiesList"})
@Controller
@RequestMapping("/parishioners")
public class ParishionerController {
    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private PersonService personService;
    @Autowired
    private ParishionerRepository parishionerRepo;
    @Resource(name = "downloadService")
    private DownloadService downloadService;
    @Autowired
    ParishionerService parishionerService;

    @InitBinder
    public void initVarietyBinder(final WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Person.class, (PropertyEditor) new PersonPropertyEditor(personRepo));
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView addNewMember() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("parishioners/create");
        Parishioner parishioner = new Parishioner();
        Person person = new Person();
        parishioner.setPerson(person);
        modelAndView.addObject("parishioner", parishioner);
        return modelAndView;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView showResults() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("parishioners/list");
        Table parishionerTable = new ParishionerTable(parishionerRepo.findAll());
        modelAndView.addObject("table", parishionerTable);
        return modelAndView;
    }


    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public ModelAndView selectFilterCriteria() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("parishioners/filter");
        return modelAndView;
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateParishioner(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute(parishionerRepo.findOne(id));
        return "parishioners/show";
    }


    @RequestMapping(value = "/{id}", params = "copy", method = RequestMethod.GET)
    public String copyMember(@PathVariable("id") Long id, Model uiModel) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("parishioners/create");
        Parishioner parishioner = parishionerRepo.findOne(id);
        System.out.println("copying from " + parishioner.getPerson().getLastName() + parishioner.getId());

        uiModel.addAttribute(parishionerRepo.findOne(id));
        uiModel.asMap().clear();
        return "redirect:/parishioners/create?id=" + id;
    }

    @RequestMapping(value = "/{id}", params = "update", method = RequestMethod.GET)
    public ModelAndView updateMember(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("parishioners/update");
        Parishioner parishioner = parishionerRepo.findOne(id);
        System.out.println("updating from " + parishioner.getPerson().getLastName() + parishioner.getId());

        modelAndView.addObject("parishioner", parishioner);
        modelAndView.addObject("parishioners", parishionerRepo.findAll());
        modelAndView.addObject("baptistsList", parishionerService.getBaptistsList());
        return modelAndView;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("parishioner") Parishioner parishioner, @RequestParam("photo") MultipartFile photo, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {

        if (bindingResult.hasErrors()) {
            uiModel.addAttribute(parishioner);
            return "parishioners/update";
        }

        uiModel.asMap().clear();
        //parishionerDAO.updateParishioner(parishioner);
        parishionerService.updateParishioner(parishioner);
        return "redirect:/parishioners/list";
    }

    @ModelAttribute("maritalStatusList")
    public List<MaritalStatus> populateMaritalStatus() {
        return parishionerService.getMaritalStatusList();
    }

    @ModelAttribute("studiesList")
    public List<Education> populateStudies() {
        return parishionerService.getEducationList();
    }

    @RequestMapping(value = "/import", params = "excel", method = RequestMethod.GET)
    public ModelAndView importFromExcel() throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        try {
            parishionerService.importDataFromExcelFile("F:/olda calc/Biserica/fisiere_xls/EvidentaMembrii.xlsx");
        } catch (IOException e) {
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
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }
}
