package com.church.elim.matchers;

import com.church.elim.domain.Person;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 5/15/13
 * Time: 9:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class PersonMatcher implements Matcher<Person> {
    private final ResultActions actions;

    public enum MatchType {JSON_PATH}

    ;
    MatchType matchType;

    private PersonMatcher(ResultActions actions) {
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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void describeTo(Description description) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
