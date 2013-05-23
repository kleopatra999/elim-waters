package com.church.elim.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.church.elim.domain.Person;

public class PersonValidator implements Validator{

	public boolean supports(Class className) {
		return Person.class.equals(className);
	}

	public void validate(Object target, Errors e) {

		Person person = (Person)target;
		System.out.println("Person:" + person);
		// Validating firstName field
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "firstName", "field.required.firstname", "The First Name cannot be empty.");
		if (!PatternValidator.validateName(person.getFirstName()))
			e.rejectValue("firstName", "field.invalid.firstName", "The First Name value is not in the required format.");
		
		// Validating lastName field
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "lastName", "field.required.lastName", "The Last Name cannot be empty.");
		if (!PatternValidator.validateName(person.getLastName()))
			e.rejectValue("lastName", "field.invalid.lastName", "The Last Name value is not in the required format.");
		
		// Validating cnp field
		/*ValidationUtils.rejectIfEmptyOrWhitespace(e, "cnp", "field.required.cnp", "The CNP cannot be empty.");
		if (!PatternValidator.validateCnp(person.getCnp()))
			e.rejectValue("cnp", "field.invalid.cnp", "The CNP value is not in the required format.");*/

		// Validating phone field
		if (!PatternValidator.validatePhone(person.getPhone()))
			e.rejectValue("phone", "field.invalid.phone", "The Phone value is not in the required format.");
		
		// Validating email field
		if (!PatternValidator.validateEmail(person.getEmail()))
			e.rejectValue("email", "field.invalid.email", "The E-Mail value is not in the required format.");

		// Validating marriageDate field
		if (e.hasFieldErrors("marriageDate") && person.getMarriageDate() != null)
			e.rejectValue("marriageDate", "field.invalid.marriageDate", "The Marriage Date value is not in the required format.");
	}
	
}
