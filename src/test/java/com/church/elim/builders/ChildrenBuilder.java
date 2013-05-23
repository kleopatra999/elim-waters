package com.church.elim.builders;

import com.church.elim.domain.Children;
import com.church.elim.domain.Person;
import org.apache.commons.lang.math.RandomUtils;

import java.util.HashSet;
import java.util.Set;

import static com.church.elim.ElimRandom.newId;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 5/15/13
 * Time: 10:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChildrenBuilder {
    private String firstName = "Pop";
    private String lastName = "Ionel";
    private Long id = (long) 1;
    private Set<Children> children = new HashSet<Children>() {
        {
            add(new Children());
        }
    };

    public ChildrenBuilder(){
    }
    public ChildrenBuilder withId(Long id){
        this.id = id;
        return this;
    }
    public ChildrenBuilder withFirstName(String firstName){
        this.firstName = firstName;
        return this;
    }
    public ChildrenBuilder withLastName(String lastName){
        this.lastName = lastName;
        return this;
    }
    public Children buildRandom(Person parent){
        Children children = new Children();
        Person child = new PersonBuilder().buildRandom();
        children.setId(newId());
        children.setParent(parent);
        children.setChild(child);
        return children;
    }
}
