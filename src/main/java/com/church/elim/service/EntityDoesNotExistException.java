package com.church.elim.service;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 6/4/13
 * Time: 6:36 PM
 */
public class EntityDoesNotExistException extends Exception {
    public static String MESSAGE = "Could not find any {0} with id {1}!";
    public EntityDoesNotExistException(String entityName, Long id) {
        super(String.format(MESSAGE, entityName, id));
    }
}
