package com.church.elim.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ElimMessage {
    private String key;
    Locale locale;                        
    
    @Autowired
    private MessageSource messageSource;

    public ElimMessage(String key) {
        super();
        this.key = key;        
    }
    
    public ElimMessage() {
    	locale = LocaleContextHolder.getLocale();
    }
    public String getMessage(String code, Object...params) {
        Locale locale = LocaleContextHolder.getLocale();                        
        return messageSource.getMessage(code, params, locale);
    }
    public String getMessage(String code) {
    	return messageSource.getMessage(code, null, locale);
    }
}