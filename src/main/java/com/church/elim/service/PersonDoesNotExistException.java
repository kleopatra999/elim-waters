package com.church.elim.service;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 5/14/13
 * Time: 9:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class PersonDoesNotExistException extends Exception {
    public static String MESSAGE = "Person with id {0} does not exist!";
    public PersonDoesNotExistException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
