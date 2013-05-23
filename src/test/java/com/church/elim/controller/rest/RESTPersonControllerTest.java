package com.church.elim.controller.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.church.elim.builders.ChildrenBuilder;
import com.church.elim.builders.PersonBuilder;
import com.church.elim.domain.Children;
import com.church.elim.matchers.PersonMatcher;
import com.church.elim.service.*;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.ServletWebRequest;

import com.church.elim.ElimTest;
import com.church.elim.domain.Person;
import com.church.elim.repository.PersonRepository;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class RESTPersonControllerTest extends ElimTest{
    @Autowired WebApplicationContext wac;
    @Autowired MockServletContext servletContext; // cached
    @Autowired MockHttpSession session;
    @Autowired MockHttpServletRequest request;
    @Autowired MockHttpServletResponse response;
    @Autowired ServletWebRequest webRequest;
    @Autowired
    private RESTPersonController personController;
    PersonService mockPersonService = Mockito.mock(PersonService.class);
    ChildrenService mockChildrenService = Mockito.mock(ChildrenService.class);
    PersonRepository mockPersonRepo = Mockito.mock(PersonRepository.class);
    @Autowired
    CustomUserDetailsService userService;
    @Autowired
    PersonRepository personRepo;
    Person popIonel;
    private MockMvc mockMvc;
    PersonBuilder personBuilder = new PersonBuilder();
    ChildrenBuilder childrenBuilder = new ChildrenBuilder();
    @Before
    public void setup() {
        popIonel = personBuilder.build();
        mockMvc = webAppContextSetup(wac).build();
        ReflectionTestUtils.setField(wac.getBean(RESTPersonController.class), "personService", mockPersonService);
        ReflectionTestUtils.setField(wac.getBean(RESTPersonController.class), "personRepo", mockPersonRepo);
        ReflectionTestUtils.setField(wac.getBean(RESTPersonController.class), "childrenService", mockChildrenService);
    }

    @Test
    public void testGetPerson() throws Exception{
        long id = (long) 1;
        when(this.mockPersonService.findOne(id)).thenReturn(popIonel);
        ResultActions actions = this.mockMvc.perform(get("/persons/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        assertThat(popIonel, PersonMatcher.createMatcher(actions));
    }

    @Test
    public void testGetChildren() throws Exception{
        long id = (long) 1;
        List<Person> children = new ArrayList<Person>(){
            {
                add(personBuilder.buildRandom());
                add(personBuilder.buildRandom());
            }
        };
        when(this.mockPersonService.getChildren(id)).thenReturn(children);
        ResultActions actions = this.mockMvc.perform(get("/persons/1/children")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id", Matchers.hasSize(2)));
    }

    @Test
    public void testAddChildrenById() throws Exception{
        long id = (long) 1;
        Person child = personBuilder.buildRandom();
        Children children = childrenBuilder.buildRandom(popIonel);
        when(mockPersonService.addChild(popIonel.getId(), child.getId())).thenReturn(children);
        ResultActions actions = this.mockMvc.perform(post("/persons/1/children")
                .content(child.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(children.getId()));
    }

    @Test
    public void testAddExistingChildrenById() throws Exception{
        long id = (long) 1;
        Person child = personBuilder.buildRandom();
        Children children = childrenBuilder.buildRandom(popIonel);
        Mockito.doThrow(new ChildrenAlreadyExistsException((long)1, child.getId())).
                when(mockPersonService).addChild(popIonel.getId(), child.getId());
        ResultActions actions = this.mockMvc.perform(post("/persons/1/children")
                .content(child.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(content().string(String.format(ChildrenAlreadyExistsException.MESSAGE,
                        popIonel.getId(), child.getId())));
    }

    @Test
    public void testDeleteNonExistingChildrenById() throws ChildDoesNotExistException, Exception {
        Mockito.doThrow(new ChildDoesNotExistException((long) 2)).
                when(mockPersonService).deleteChild((long) 1, (long) 2);
        ResultActions actions = this.mockMvc.perform(delete("/persons/1/children/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(String.format(ChildDoesNotExistException.MESSAGE, (long) 2)));
    }
    @Test
    public void testDeleteChildById() throws Exception{
        ResultActions actions = this.mockMvc.perform(delete("/persons/1/children/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @After
    public void tearDown(){
    }
}
