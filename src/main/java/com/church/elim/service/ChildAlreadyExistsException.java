package com.church.elim.service;

public class ChildAlreadyExistsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ChildAlreadyExistsException(Long parentId, String childName){
		super("Parent with id " + parentId + 
				" already has child "+ childName + "!");
	}

}
