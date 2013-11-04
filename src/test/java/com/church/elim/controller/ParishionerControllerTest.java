package com.church.elim.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.church.elim.builders.PersonBuilder;
import com.church.elim.controller.rest.RestPersonController;
import com.church.elim.service.EntityDoesNotExistException;
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
import org.springframework.web.context.WebApplicationContext;

import com.church.elim.ElimTest;
import com.church.elim.domain.Person;
import com.church.elim.repository.PersonRepository;
import com.church.elim.service.CustomUserDetailsService;
import com.church.elim.service.PersonService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class ParishionerControllerTest extends ElimTest{
	@Autowired WebApplicationContext wac;
	@Autowired
	private ParishionerController parishionerController;

	PersonService mockPersonService = Mockito.mock(PersonService.class);
	PersonRepository mockPersonRepo = Mockito.mock(PersonRepository.class);
	@Autowired
	CustomUserDetailsService userService;
	@Autowired
	PersonRepository personRepo;
	Person popIonel;
	private MockMvc mockMvc;
	@Before
	public void setup() {
        popIonel = new PersonBuilder().buildWithChildren();
		mockMvc = webAppContextSetup(wac).build();
		ReflectionTestUtils.setField(wac.getBean(ParishionerController.class), "personService", mockPersonService);
        ReflectionTestUtils.setField(wac.getBean(ParishionerController.class), "personRepo", mockPersonRepo);
        ReflectionTestUtils.setField(wac.getBean(RestPersonController.class),
                "domainService", mockPersonService);
	}
	@Test
	public void testCreatePerson() throws Exception{
		when(this.mockPersonService.create(any(Person.class))).thenReturn(popIonel);
		String person = toJSON(popIonel);
		System.out.println(person);
		this.mockMvc.perform(post("/persons")
				.content(person)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(header().string("Location", linkTo(RestPersonController.class).slash(popIonel.getId()).toUri().toString()));
	}
    @Test
    public void testDeletePerson() throws Exception{
        this.mockMvc.perform(delete("/persons/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testDeletePersonNotFound() throws Exception{
        Mockito.doThrow(new EntityDoesNotExistException("Person", (long)2)).when(mockPersonService).remove((long) 2);
        this.mockMvc.perform(delete("/persons/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(String.format(EntityDoesNotExistException.MESSAGE, "Person",2)));
    }
    @After
	public void tearDown(){
	}
}
