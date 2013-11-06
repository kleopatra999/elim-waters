package com.church.elim.web.controller;

import com.church.elim.web.controller.rest.ChildrenAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * User: adi
 * Date: 5/14/13
 * Time: 10:08 PM
 */
@ControllerAdvice
public class ChildrenControllerAdvice {
    @ExceptionHandler(ChildrenAlreadyExistsException.class)
    ResponseEntity<String> handleChildrenAlreadyExists(Exception e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.FOUND);
    }

}
