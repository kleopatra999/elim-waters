package com.church.elim.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.Date;
 
public class PatternValidator {
 
	  private enum FieldPattern {
		  NAME_PATTERN("^[a-zA-Z\\.\\'\\-_\\s]{1,40}$"),
		  CNP_PATTERN("^[0-9]{13}$"),
		  PHONE_PATTERN("^[0-9]*$"),
		  EMAIL_PATTERN("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"),
		  DATE_PATTERN("MM/dd/yyyy");
		  
		  private final String format;
		  
		  FieldPattern(String format){
			  this.format = format;
		  }
		  
		  public String toString(){
			  return format;
		  }
	  }
	  
	  public static boolean validateName(String name){
		  return validateField(FieldPattern.NAME_PATTERN, name);
	  }
		
	  public static boolean validateCnp(String cnp){
		  return validateField(FieldPattern.CNP_PATTERN, cnp);
	  }
	  
	  public static boolean validatePhone(String phone){
		  return validateField(FieldPattern.PHONE_PATTERN, phone);
	  }
	  
	  public static boolean validateEmail(String email){
		  return validateField(FieldPattern.EMAIL_PATTERN, email);
	  }
	  
	  public static boolean validateDate(Date date){
		  if (date == null)
			  return true;
		  
		  return validateDate(date.toString());
	  }
	  
	  public static boolean validateDate(String date){
		  if (date.trim().equals(""))
			  return true;

		  DateFormat format = new SimpleDateFormat(FieldPattern.DATE_PATTERN.toString());
		  try {
			  format.parse(date);
		  } catch (ParseException e) {
			  return false;
		  }
		  return true;
	  }
	  
	  public static boolean validateField(FieldPattern fieldFormat, String fieldValue){
		  return validateField(fieldFormat.toString(), fieldValue);
	  }
	  
	  public static boolean validateField(String fieldFormat, String fieldValue){
		  
		  if (fieldValue==null||fieldValue.trim().equals(""))
			  return true;
		  
		  Pattern pattern = Pattern.compile(fieldFormat);
		  return pattern.matcher(fieldValue).matches();
	  }
}