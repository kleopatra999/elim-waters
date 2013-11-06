package com.church.elim.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class PropertyEditorUtils {
	@Autowired
    private ReloadableResourceBundleMessageSource messageSource;
	@InitBinder
	public void initDateBinder(final WebDataBinder dataBinder, final Locale locale) 
	{ 
		final String dateformat = this.messageSource.getMessage("date.format", null, locale);
		final SimpleDateFormat sdf = new SimpleDateFormat(dateformat); 
		sdf.setLenient(false); 
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}
}
