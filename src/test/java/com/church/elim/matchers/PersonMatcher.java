package com.church.elim.matchers;

import com.church.elim.domain.Person;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 5/15/13
 * Time: 9:59 PM
 */
public class PersonMatcher extends BaseMatcher<Person> {
    private final ResultActions actions;

    public enum MatchType {JSON_PATH}

    ;
    MatchType matchType;

    public PersonMatcher(ResultActions actions) {
        this.actions = actions;
        this.matchType = MatchType.JSON_PATH;
    }

    public static PersonMatcher createMatcher(ResultActions actions) {
        return new PersonMatcher(actions);
    }

    @Override
    public boolean matches(Object o) {
        Person person = (Person) o;
        switch (matchType) {
            case JSON_PATH:
                try {
                    actions.andExpect(jsonPath("$.firstName").value(person.getFirstName()))
                            .andExpect(jsonPath("$.lastName").value(person.getLastName()));
                } catch (Exception e) {
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void describeMismatch(Object o, Description description) {
    }

    @Override
    public void describeTo(Description description) {
    }
}
