package com.church.elim.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.exceptions.TemplateProcessingException;

/**
 * Handles requests for the application home page.
 */
@Controller
public class RootController {

	/**
	 * homeForm
	 * 
	 * Selects the root view to render by returning its name.
	 * 
	 * @param model 
	 * @return String - View name
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/html")
	public String homeForm(Model model) {
		return "index";
		
	}
	
	/**
	 * errorTest
	 * 
	 * Cause and internal error
	 * 
	 * @param message 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/errortest/{message}", method = RequestMethod.GET, produces = "text/html")
	public void errorTest(@PathVariable String message) throws Exception {
	
		if (message.toString().equalsIgnoreCase("string")) {
			// Throw a generic Exception with a message
			message = "Error: Bad error ... Bad Bad!";
		} else if (message.toString().equalsIgnoreCase("divide")) {
			// Cause a divide by zero error
			int a = 12/0;
		} else if (message.toString().equalsIgnoreCase("throw")) {
			// Throw an NPE
			throw new NullPointerException("Testing NPE from RootController...");
		} else if (message.toString().equalsIgnoreCase("template")) {
			// Simulate Thymeleaf throwing a Template Processing Exception
			message = "";
			throw new  TemplateProcessingException("Testing Template Processing Exception from RootController...");
		} else {
			// Some generic error occurred
			message = "errorTest - Generic Unknown...";
		}
		
		throw new Exception(message);
		
	}
}
