package com.church.elim.controller.rest;

import static com.church.elim.builders.PersonBuilder.aPerson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.church.elim.ElimTest;
import com.church.elim.domain.Person;
import com.church.elim.repository.PersonRepository;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class RestPersonControllerTest extends ElimTest{
    @Autowired
    private RestPersonController personController;
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
        ReflectionTestUtils.setField(wac.getBean(RestPersonController.class), "personService", mockPersonService);
        ReflectionTestUtils.setField(wac.getBean(RestPersonController.class), "personRepo", mockPersonRepo);
        ReflectionTestUtils.setField(wac.getBean(RestPersonController.class), "childrenService", mockChildrenService);
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
    public void testGetPersonsNames() throws Exception{
        long id = (long) 1;
        List<Person> persons = new ArrayList<Person>();
        persons.add(aPerson().buildRandom());
        persons.add(aPerson().buildRandom());
        when(this.mockPersonService.findAll()).thenReturn(persons);
        ResultActions actions = this.mockMvc.perform(get("/persons?fields=firstName,lastName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..firstName").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$..lastName").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$..id").value(Matchers.empty()));
        System.out.println(actions.andReturn().getResponse().getContentAsString());
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
    public void testAddChildren() throws Exception{
        long id = (long) 1;
        Person child = personBuilder.buildRandom();
        List<Children> children = new ArrayList<Children>();
        children.add(childrenBuilder.buildRandom(popIonel));
        children.add(childrenBuilder.buildRandom(popIonel));
        //when(mockPersonService.addChildren((List<Children>) Any.ANY).thenReturn(children);
        ResultActions actions = this.mockMvc.perform(post("/persons/1/children")
                .content(toJSON(children))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        linkTo(RestPersonController.class).slash(popIonel.getId()).slash("children").toUri().toString()));;
    }

    @Test
    public void testAddExistingChildrenById() throws Exception{
        long id = (long) 1;
        Person child = personBuilder.buildRandom();
        List<Children> children = new ArrayList<Children>();
        children.add(childrenBuilder.buildRandom(popIonel));
        children.add(childrenBuilder.buildRandom(popIonel));
        Mockito.doThrow(new ChildrenAlreadyExistsException((long)1, child.getId())).
                when(mockPersonService).addChildren((List<Children>) anyObject());
        ResultActions actions = this.mockMvc.perform(post("/persons/1/children")
                .content(toJSON(children))
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
