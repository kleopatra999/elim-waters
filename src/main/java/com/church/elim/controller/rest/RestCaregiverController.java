package com.church.elim.controller.rest;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.church.elim.controller.rest.RestDomainController;
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
@Controller
@RequestMapping("/caregivers")
public class RestCaregiverController extends RestDomainController<Caregiver>{
}
