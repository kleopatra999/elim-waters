package com.church.elim.controller.rest;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 5/15/13
 * Time: 11:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChildrenAlreadyExistsException extends Exception {
    public static String MESSAGE = "Parent with id {0} already has child with id {1}!";
    public ChildrenAlreadyExistsException(Long parentId, Long childId) {
        super(String.format(MESSAGE, parentId, childId));
    }
}
