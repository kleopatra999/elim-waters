package com.church.elim.service;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 5/16/13
 * Time: 11:35 PM
 */
public class ChildDoesNotExistException extends Throwable {
    public static String MESSAGE = "Child {0} does not exist!";
    public ChildDoesNotExistException(Long childId) {
        super(String.format(MESSAGE, childId));
    }
}
