package com.church.elim.builders;

import com.church.elim.domain.Children;
import com.church.elim.domain.Person;
import org.apache.commons.lang.math.RandomUtils;

import java.util.HashSet;
import java.util.Set;

import static com.church.elim.ElimRandom.newId;
import static com.church.elim.ElimRandom.newName;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 5/15/13
 * Time: 10:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class PersonBuilder {
    private String firstName = "Pop";
    private String lastName = "Ionel";
    private Long id = (long) 1;
    private Set<Children> children = new HashSet<Children>() {
        {
            add(new Children());
        }
    };

    public PersonBuilder(){
    }
    public PersonBuilder withId(Long id){
        this.id = id;
        return this;
    }
    public PersonBuilder withFirstName(String firstName){
        this.firstName = firstName;
        return this;
    }
    public PersonBuilder withLastName(String lastName){
        this.lastName = lastName;
        return this;
    }
    public Person build(){
        Person person = new Person(this.firstName,this.lastName);
        person.setId(id);
        return person;
    }

    public Person buildRandom(){
        Person person = new Person(newName("firstName"),newName("lastName"));
        person.setId(newId());
        return person;
    }

    public Person addRandomChildren(Person person){
        ChildrenBuilder childrenBuilder = new ChildrenBuilder();
        Set<Children> children = new HashSet<Children>();
        children.add(childrenBuilder.buildRandom(person));
        children.add(childrenBuilder.buildRandom(person));
        person.setChildren(children);
        return person;
    }

    public Person buildWithChildren() {
        return addRandomChildren(buildRandom());
    }
}