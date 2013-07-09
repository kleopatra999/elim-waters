package com.church.elim.builders;

import com.church.elim.domain.Caregiver;
import com.church.elim.domain.Children;
import com.church.elim.domain.Person;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.church.elim.ElimRandom.newId;
import static com.church.elim.ElimRandom.newName;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 5/15/13
 * Time: 10:43 PM
 */
public class CaregiverBuilder implements DomainBuilder<Caregiver>{
    public static CaregiverBuilder aCaregiver(){
        return new CaregiverBuilder();
    }
    public Caregiver buildRandom(){
        return null;
    }

    public Caregiver build() {
        Person person = PersonBuilder.aPerson().build();
        Caregiver caregiver =  new Caregiver();
        caregiver.setPerson(person);
        caregiver.setId((long) 1);
        return caregiver;
    }
}
