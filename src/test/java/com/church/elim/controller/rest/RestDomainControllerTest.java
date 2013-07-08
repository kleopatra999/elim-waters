package com.church.elim.controller.rest;

import com.church.elim.ElimTest;
import com.church.elim.builders.ChildrenBuilder;
import com.church.elim.builders.PersonBuilder;
import com.church.elim.controller.PersonController;
import com.church.elim.domain.Caregiver;
import com.church.elim.domain.EntityTest;
import com.church.elim.domain.Identifiable;
import com.church.elim.domain.Person;
import com.church.elim.repository.PersonRepository;
import com.church.elim.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collection;

import static com.church.elim.builders.CaregiverBuilder.aCaregiver;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.junit.runners.Parameterized.Parameters;
/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 6/2/13
 * Time: 12:16 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class RestDomainControllerTest extends ElimTest implements ApplicationContextAware {
    @Autowired
    RestCaregiverController categiverController;
    PersonBuilder personBuilder = new PersonBuilder();
    ChildrenBuilder childrenBuilder = new ChildrenBuilder();
    @Autowired
    WebApplicationContext wac;
    @Autowired
    private PersonController personController;
    DomainService entityService;
    @Autowired
    PersonRepository personRepo;
    Person popIonel;
    private MockMvc mockMvc;
    private EntityTest entityTest;
    private Identifiable anEntity;
    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[]{new EntityTest("Person")},new Object[]{new EntityTest("Caregiver")});
    }

    public RestDomainControllerTest(EntityTest entityTest){
        anEntity = (Identifiable) this.entityTest.getEntityBuilder().build();
    }

    @Before
    public void setup() {
        popIonel = new PersonBuilder().buildWithChildren();
        mockMvc = webAppContextSetup(wac).build();
        ReflectionTestUtils.setField(wac.getBean(RestCaregiverController.class), "domainService",
                this.entityTest.getEntityService());
        when(this.entityService.get(any(Long.class))).thenReturn(anEntity);
    }
    @Test
    public void testGet() throws Exception {
        long id = (long) 1;
        ResultActions actions = mockMvc.perform(get(this.entityTest.getResourceUrl()+"/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        System.out.println(actions.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testCreatePerson() throws Exception{
        when(this.entityService.create(any(Caregiver.class))).thenReturn(anEntity);
        String person = toJSON(anEntity);
        System.out.println(person);
        this.mockMvc.perform(post(this.entityTest.getResourceUrl())
                .content(person)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", linkTo(this.entityTest.getEntityController().getClass())
                        .slash(anEntity.getId()).toUri().toString()));
    }

    @Test
    public void testDeletePerson() throws Exception{
        this.mockMvc.perform(delete(this.entityTest.getResourceUrl()+"/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testDeleteEntityNotFound() throws Exception{
        Mockito.doThrow(new EntityDoesNotExistException(entityTest.getEntityName(), (long)2)).when(entityService).remove((long) 2);
        this.mockMvc.perform(delete(entityTest.getResourceUrl()+"/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(String.format(EntityDoesNotExistException.MESSAGE, entityTest.getEntityName(),2)));
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.entityService = (DomainService) Mockito.mock(
                applicationContext.getBean(entityTest.getEntityName().toLowerCase() + "Service").getClass());
    }
}
