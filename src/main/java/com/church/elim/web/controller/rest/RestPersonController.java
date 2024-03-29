package com.church.elim.web.controller.rest;

import java.io.IOException;
import java.util.List;

import com.church.elim.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.church.elim.domain.Children;
import com.church.elim.domain.Person;
import com.church.elim.repository.ChildrenRepository;
import com.church.elim.repository.PersonRepository;

import static com.church.elim.utils.CustomJsonFilter.filterList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping("/persons")
public class RestPersonController extends RestDomainController<Person>{
    @Autowired
    PersonRepository personRepo;
    @Autowired
    ChildrenRepository childrenRepo;
    @Autowired
    ChildrenService childrenService;
    @Autowired
    PersonService personService;
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<String>> getPersons(@RequestParam String fields) throws IOException {
        List<Person> persons = personService.findAll();
        return new ResponseEntity<List<String>>(filterList(fields, persons), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/children", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Person> getChildren(@PathVariable("id") Long id) throws PersonDoesNotExistException {
        List<Person> children = personService.getChildren(id);
        return children;
    }

    /* Adds a child to a person children list and returns the added children entity */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/{parentId}/children", method = RequestMethod.POST,
            produces = "application/json")
    public
    @ResponseBody
    ResponseEntity<Void> addChildren(@PathVariable int parentId, @RequestBody List<Children> children)
            throws IOException, ChildrenAlreadyExistsException, PersonDoesNotExistException {
        personService.addChildren(children);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(RestPersonController.class).slash(parentId).slash("children").toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    /* Adds a child to a person children list and returns the added children entity */
    @RequestMapping(value = "/{id}/children/add", method = RequestMethod.POST,
            produces = "application/json")
    public
    @ResponseBody
    ResponseEntity<Children> addChildrenByName(@PathVariable("id") Long parentId, @RequestBody String childName) throws Exception {
        String message = "";
        Children children = null;
        children = personService.addChild(parentId, childName);
        message = "Successfully added children" + childName + "!";
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(RestPersonController.class).slash(parentId).slash("children").toUri());
        return new ResponseEntity<Children>(children, HttpStatus.CREATED);
    }

    /* Deletes child. */
    @RequestMapping(value = "/{parentId}/children/{childId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> deleteChild(@PathVariable Long parentId, @PathVariable Long childId)
            throws ChildDoesNotExistException {
        personService.deleteChild(parentId, childId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
