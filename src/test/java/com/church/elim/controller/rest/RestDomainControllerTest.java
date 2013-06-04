package com.church.elim.controller.rest;

import com.church.elim.ElimTest;
import com.church.elim.builders.ChildrenBuilder;
import com.church.elim.builders.PersonBuilder;
import com.church.elim.matchers.PersonMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 6/2/13
 * Time: 12:16 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class RestDomainControllerTest extends ElimTest{
    @Autowired
    RestCaregiverController categiverController;
    private MockMvc mockMvc;
    PersonBuilder personBuilder = new PersonBuilder();
    ChildrenBuilder childrenBuilder = new ChildrenBuilder();
    @Before
    public void setup() {
        mockMvc = webAppContextSetup(wac).build();
    }
    @Test
    public void testGet() throws Exception {
        long id = (long) 1;
        ResultActions actions = mockMvc.perform(get("/caregivers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        System.out.println(actions.andReturn().getResponse().getContentAsString());
    }
}
