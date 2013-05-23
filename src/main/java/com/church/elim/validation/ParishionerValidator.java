package com.church.elim.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.church.elim.domain.Parishioner;
import com.church.elim.domain.Person;

public class ParishionerValidator implements Validator{

	 private final Validator personValidator;

	   public ParishionerValidator(Validator personValidator) {
	      if (personValidator == null) {
	          throw new IllegalArgumentException("The supplied [Validator] is required and must not be null.");
	      }
	      if (!personValidator.supports(Person.class)) {
	          throw new IllegalArgumentException(
	            "The supplied [Validator] must support the validation of [Person] instances.");
	      }
	      this.personValidator = personValidator;
	   }
	
	public boolean supports(Class<?> className) {
		return Parishioner.class.equals(className);
	}

	public void validate(Object obj, Errors errors) {
		System.out.println("Validating parishioner obj");
		Parishioner parishioner = (Parishioner) obj;
	      try {
	    	  if (errors.hasFieldErrors("baptismDate") && parishioner.getBaptismDate() != null) 
	    		  errors.rejectValue("baptismDate", "field.invalid.baptismateDate", "The Baptism Date value is not in the required format");
	          
	    	  if (errors.hasFieldErrors("holySpiritBaptism") && parishioner.getHolySpiritBaptism() != null)
	        	  errors.rejectValue("holySpiritBaptism", "field.invalid.holySpiritBaptism", "The Holy Spirit Baptism Date value is not in the required format");
	    	  
	    	  errors.pushNestedPath("person");
	          ValidationUtils.invokeValidator(this.personValidator, parishioner.getPerson(), errors);
	      } finally {
	          errors.popNestedPath();
	      }
	}
}
