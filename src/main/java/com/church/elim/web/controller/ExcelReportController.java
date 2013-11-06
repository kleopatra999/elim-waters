package com.church.elim.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.church.elim.domain.Education;
import com.church.elim.domain.MaritalStatus;
import com.church.elim.service.ParishionerService;

@SessionAttributes(value={"maritalStatus","studiesList"})
@Controller
public class ExcelReportController {
	@Autowired
	ParishionerService parishionerService;
	
	protected ModelAndView handleRequestinternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	 
			//dummy data
			Map<String,String> revenueData = new HashMap<String,String>();
			revenueData.put("Jan-2010", "$100,000,000");
			revenueData.put("Feb-2010", "$110,000,000");
			revenueData.put("Mar-2010", "$130,000,000");
			revenueData.put("Apr-2010", "$140,000,000");
			revenueData.put("May-2010", "$200,000,000");
			return new ModelAndView("ExcelRevenueSummary","revenueData",revenueData);
	 
		}
	
	

	@ModelAttribute("maritalStatusList")
	public List<MaritalStatus> populateMaritalStatus() {
		return parishionerService.getMaritalStatusList();
	}

	@ModelAttribute("studiesList")
	public List<Education> populateStudies() {
		return parishionerService.getEducationList();
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
