package com.church.elim.controller.rest;

import com.church.elim.ElimTest;
import com.church.elim.builders.ChildrenBuilder;
import com.church.elim.builders.PersonBuilder;
import com.church.elim.controller.PersonController;
import com.church.elim.domain.Caregiver;
import com.church.elim.domain.Person;
import com.church.elim.matchers.PersonMatcher;
import com.church.elim.repository.PersonRepository;
import com.church.elim.service.CaregiverService;
import com.church.elim.service.CustomUserDetailsService;
import com.church.elim.service.EntityDoesNotExistException;
import com.church.elim.service.PersonService;
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

import static com.church.elim.builders.CaregiverBuilder.aCaregiver;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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
    PersonBuilder personBuilder = new PersonBuilder();
    ChildrenBuilder childrenBuilder = new ChildrenBuilder();
    @Autowired
    WebApplicationContext wac;
    @Autowired
    MockServletContext servletContext; // cached
    @Autowired
    MockHttpSession session;
    @Autowired
    MockHttpServletRequest request;
    @Autowired
    MockHttpServletResponse response;
    @Autowired
    ServletWebRequest webRequest;
    @Autowired
    private PersonController personController;

    PersonService mockPersonService = Mockito.mock(PersonService.class);
    PersonRepository mockPersonRepo = Mockito.mock(PersonRepository.class);
    CaregiverService mockCaregiverService = Mockito.mock(CaregiverService.class);;
    @Autowired
    CustomUserDetailsService userService;
    @Autowired
    PersonRepository personRepo;
    Person popIonel;
    Caregiver aCaregiver = aCaregiver().build();
    private MockMvc mockMvc;
    private String entityName = "Caregiver";
    private String resourceUrl = "/caregivers";
    @Before
    public void setup() {
        popIonel = new PersonBuilder().buildWithChildren();
        mockMvc = webAppContextSetup(wac).build();
        ReflectionTestUtils.setField(wac.getBean(RestCaregiverController.class), "domainService", mockCaregiverService);
        when(this.mockCaregiverService.get(any(Long.class))).thenReturn(aCaregiver().build());
    }
    @Test
    public void testGet() throws Exception {
        long id = (long) 1;
        ResultActions actions = mockMvc.perform(get(resourceUrl+"/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        System.out.println(actions.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testCreatePerson() throws Exception{
        when(this.mockCaregiverService.create(any(Caregiver.class))).thenReturn(aCaregiver);
        String person = toJSON(aCaregiver);
        System.out.println(person);
        this.mockMvc.perform(post(resourceUrl)
                .content(person)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", linkTo(RestCaregiverController.class).slash(aCaregiver.getId()).toUri().toString()));
    }

    @Test
    public void testDeletePerson() throws Exception{
        this.mockMvc.perform(delete(resourceUrl+"/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testDeletePersonNotFound() throws Exception{
        Mockito.doThrow(new EntityDoesNotExistException(entityName, (long)2)).when(mockCaregiverService).remove((long) 2);
        this.mockMvc.perform(delete(resourceUrl+"/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(String.format(EntityDoesNotExistException.MESSAGE, entityName,2)));
    }
}
