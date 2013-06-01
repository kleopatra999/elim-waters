package com.church.elim.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.church.elim.controller.rest.ChildrenAlreadyExistsException;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.church.elim.domain.Children;
import com.church.elim.domain.Person;
import com.church.elim.repository.ChildrenRepository;
import com.church.elim.repository.PersonRepository;

@Service
public class PersonService {
    @Autowired
    ChildrenService childrenService;
    @Autowired
    ChildrenRepository childrenRepo;
    @Autowired
    PersonRepository personRepo;
    @PersistenceContext
    public EntityManager entityManager;

    /**
     * Adds a child in the list od the parent.
     *
     * @param parentId
     * @param childName the name of the child (possibly separated by | from the birth date value)
     * @return
     * @throws Exception
     */
    @Transactional
    public Children addChild(Long parentId, String childName) throws Exception {
        Children children = null;
        Children childrenEntity = null;
        String _childName = parseChildName(childName);
        Date birthDate = parseChildBirthDate(_childName);
        if (parentId != null && _childName != null && !_childName.equals("")) {
            children = new Children();
            System.out.println("Children++");
            Person parent = personRepo.findOne(parentId);
            System.out.println(parent.toString());
            String firstName = getFirstName(_childName);
            String lastName = getLastName(_childName);

            List<Person> result = personRepo.findByFirstNameAndLastName(firstName, lastName);
            Person child = null;
            if (result != null && result.size() > 0) {
                child = result.get(0);
            } else {
                Person p = new Person(firstName, lastName);
                p.setBirthDate(birthDate);
                child = personRepo.saveAndFlush(p);
            }
            System.out.println(child.toString());
            children.setParent(parent);
            children.setChild(child);
            if (childrenService.exists(children)) {
                throw new ChildAlreadyExistsException(parentId, _childName);
            }
            childrenEntity = childrenRepo.saveAndFlush(children);
        }
        return childrenEntity;
    }

    public static String getFirstName(String fullName) throws Exception {
        String _name = WordUtils.capitalize(fullName);
        String[] name = _name.split(" ");
        if (name.length < 2) {
            throw new Exception("Please provide first and last name separated by space(s)!");
        }
        String firstName = "";
        String delimiter = "";
        for (int i = 0; i < name.length - 1; i++) {
            firstName += delimiter + name[i];
            delimiter = " ";
        }
        return firstName;
    }

    public static String getLastName(String fullName) throws Exception {
        String _name = WordUtils.capitalize(fullName);
        String[] name = _name.split(" ");
        if (name.length < 2) {
            throw new Exception("Please provide first and last name separated by space(s)!");
        }
        return name[name.length - 1];
    }

    public Children addChild(Long parentId, String childName, Date birthDate) {
        // TODO Auto-generated method stub
        return null;
    }

    private String parseChildName(String _childName) {
        String[] _lines = _childName.split("\\|");
        String childName = _lines[0];
        return childName;
    }

    private Date parseChildBirthDate(String _childName) {
        String[] _lines = _childName.split("\\|");
        Date birthDate = null;
        if (_lines.length > 1 && !_lines[1].trim().equals("")) {
            birthDate = new Date(_lines[1]);
        }
        return birthDate;
    }

    @Transactional
    public Person save(Person person) {
        return personRepo.saveAndFlush(person);
    }

    @Transactional
    public void deleteByIdGreaterThan(Long id) {
        personRepo.deleteByIdGreatherThan(id);
    }

    public void remove(Long id) throws PersonDoesNotExistException {
        Person person = personRepo.findOne(id);
        if (person == null) {
            throw new PersonDoesNotExistException(id);
        }
        personRepo.delete(id);
    }

    public Person findOne(Long id) throws PersonDoesNotExistException {
        Person person = personRepo.findOne(id);
        if (person == null) {
            throw new PersonDoesNotExistException(id);
        }
        return person;
    }

    public List<Person> getChildren(Long id) throws PersonDoesNotExistException {
        List<Person> children = new ArrayList<Person>();
        Person person = findOne(id);
        if (person != null) {
            for (Children c : person.getChildren()) {
                children.add(c.getChild());
            }
        } else {
            throw new PersonDoesNotExistException(id);
        }
        return children;
    }

    @Transactional
    public Children addChild(Long parentId, Long childId) throws PersonDoesNotExistException, ChildrenAlreadyExistsException {
        Children children = null;
        Children childrenEntity = null;
        if (parentId != null && childId != null) {
            children = new Children();
            System.out.println("Children++");
            Person parent = findOne(parentId);
            Person child = findOne(childId);
            children.setParent(parent);
            children.setChild(child);
            if (childrenService.exists(children)) {
                throw new ChildrenAlreadyExistsException(parentId,childId);
            }
            childrenEntity = childrenService.save(children);
        }
        return childrenEntity;
    }

    @Transactional
    public void deleteChild(Long parentId, Long childId) throws ChildDoesNotExistException {
        Children children = null;
        if (parentId != null && childId != null) {
            children = new Children();
            Person parent = personRepo.findOne(parentId);
            Person child = personRepo.findOne(childId);
            children.setParent(parent);
            children.setChild(child);
            List<Children> childrenList = childrenRepo.findByParentIdAndChildId(parentId, childId);
            if (childrenList != null) {
                for (Children entity : childrenList)
                    childrenRepo.delete(entity);
            } else {
                throw new ChildDoesNotExistException(childId);
            }
        }
    }

    @Transactional
    public void addChildren(List<Children> children) throws PersonDoesNotExistException, ChildrenAlreadyExistsException {
        for(Children child: children){
            addChild(child.getParent().getId(),child.getChild().getId());
        }
    }

    public List<Person> findAll() {
        return personRepo.findAll();
    }
}
