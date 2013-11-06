package com.church.elim.web.controller;
import java.beans.*;

import com.church.elim.domain.Person;

import com.church.elim.repository.PersonRepository;

public class PersonPropertyEditor extends PropertyEditorSupport {
	PersonRepository personRepo=null;
	public PersonPropertyEditor(PersonRepository personRepo){
		this.personRepo = personRepo;
	}
	public void setAsText(String id) {
		Person person = null;
		if(id!=null && !id.equals("")){
			person = personRepo.findOne(Long.valueOf(id));
			System.out.println("id " + id + ": " + 
			person.getFirstName()+" " + person.getLastName());
		}
		setValue(person);
	}
} 