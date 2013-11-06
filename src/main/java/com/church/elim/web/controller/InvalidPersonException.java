package com.church.elim.web.controller;

import com.church.elim.domain.Person;

public class InvalidPersonException extends Exception {
	private static final long serialVersionUID = -2800036690484607898L;
	public InvalidPersonException(Person person){
		super("Invalid person " + person);
	}
}
