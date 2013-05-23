package com.church.elim.controller.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.church.elim.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.church.elim.domain.Children;
import com.church.elim.domain.Person;
import com.church.elim.repository.ChildrenRepository;
import com.church.elim.repository.PersonRepository;

@Controller
@RequestMapping("/persons")
public class RESTPersonController {
    @Autowired
    PersonRepository personRepo;
    @Autowired
    ChildrenRepository childrenRepo;
    @Autowired
    ChildrenService childrenService;
    @Autowired
    PersonService personService;
    @RequestMapping(value = "/names", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Person>>
    getPersonsNames(HttpServletResponse response, HttpServletRequest request, Model uiModel) {
        List<Person> persons = personRepo.findNames();
        return new ResponseEntity<List<Person>>(persons, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Person getPerson(@PathVariable("id") Long id) throws PersonDoesNotExistException {
        Person person = personService.findOne(id);
        return person;
    }

    @RequestMapping(value = "/{id}/children", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Person> getChildren(@PathVariable("id") Long id) throws PersonDoesNotExistException {
        List<Person> children = personService.getChildren(id);
        return children;
    }

    /* Adds a child to a person children list and returns the added children entity */
    @RequestMapping(value = "/{id}/children", method = RequestMethod.POST,
            produces = "application/json")
    public
    @ResponseBody
    ResponseEntity<Children> addChildrenById(@PathVariable("id") Long parentId, @RequestBody Long childId)
            throws IOException, ChildrenAlreadyExistsException, PersonDoesNotExistException {
        Children childrenEntity = personService.addChild(parentId, childId);
        return new ResponseEntity<Children>(childrenEntity, HttpStatus.CREATED);
    }

    /* Adds a child to a person children list and returns the added children entity */
    @RequestMapping(value = "/{id}/children/add", method = RequestMethod.POST,
            produces = "application/json")
    public
    @ResponseBody
    Children addChildrenByName(@PathVariable("id") Long parentId, @RequestBody String childName, HttpServletResponse response, HttpServletRequest request, Model uiModel) throws IOException {
        String message = "";
        Children children = null;
        try {
            children = personService.addChild(parentId, childName);
            message = "Successfully added children" + childName + "!";
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            message = e.toString();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
            return null;
        }
        return children;
    }

    /* Adds a child to a person children list and returns the added children entity */
    @RequestMapping(value = "/{id}/children/add-list", method = RequestMethod.POST,
            produces = "application/json")
    public
    @ResponseBody
    Children addChildrenListByName(@PathVariable("id") Long parentId, @RequestBody List<String> childNames, HttpServletResponse response, HttpServletRequest request, Model uiModel) throws IOException {
        String message = "";
        Children children = null;
        try {
            for (String childName : childNames) {
                try {
                    children = personService.addChild(parentId, childName);
                } catch (ChildAlreadyExistsException e) {
                    // TODO
                }
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            message = e.toString();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
            return null;
        }
        return children;
    }

    /* Adds a child to a person children list and returns the added children entity */
    @RequestMapping(value = "/{parentId}/children/{childId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> deleteChild(@PathVariable Long parentId, @PathVariable Long childId)
            throws ChildDoesNotExistException {
        personService.deleteChild(parentId, childId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
