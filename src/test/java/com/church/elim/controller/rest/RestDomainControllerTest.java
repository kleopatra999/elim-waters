package com.church.elim.controller.rest;

import com.church.elim.ElimTest;
import com.church.elim.builders.ChildrenBuilder;
import com.church.elim.domain.Caregiver;
import com.church.elim.domain.EntityHelper;
import com.church.elim.domain.Identifiable;
import com.church.elim.service.*;
import com.church.elim.utils.SpringJUnit4ParameterizedClassRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
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
@RunWith(SpringJUnit4ParameterizedClassRunner.class)
@WebAppConfiguration
public class RestDomainControllerTest extends ElimTest implements BeanFactoryAware{
    private final String entityName;
    ChildrenBuilder childrenBuilder = new ChildrenBuilder();
    @Autowired
    WebApplicationContext wac;
    private MockMvc mockMvc;
    private EntityHelper entityHelper;
    private Identifiable aEntity;
    private BeanFactory beanFactory;

    @SpringJUnit4ParameterizedClassRunner.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[]{"Person"},new Object[]{"Caregiver"});
    }

    @Autowired
    private ApplicationContext applicationContext;
    public RestDomainControllerTest(String entityName){
        this.entityName = entityName;
    }

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(wac).build();
        this.entityHelper.mock();
    }
    @Test
    public void testGetResource() throws Exception {
        long id = (long) 1;
        ResultActions actions = mockMvc.perform(get(this.entityHelper.getResourceUrl()+"/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        System.out.println(actions.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testCreateResource() throws Exception{
        when(this.entityHelper.getEntityService().create(Matchers.<Identifiable>anyObject())).thenReturn(aEntity);
        String entity = toJSON(aEntity);
        System.out.println(entity);
        this.mockMvc.perform(post(this.entityHelper.getResourceUrl())
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", linkTo(this.entityHelper.getEntityController().getClass())
                        .slash(aEntity.getId()).toUri().toString()));
    }

    @Test
    public void testDeleteResource() throws Exception{
        this.mockMvc.perform(delete(this.entityHelper.getResourceUrl()+"/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testDeleteResourceNotFound() throws Exception{
        Mockito.doThrow(new EntityDoesNotExistException(entityHelper.getEntityName(), (long)2)).
                when(this.entityHelper.getEntityService()).remove((long) 2);
        this.mockMvc.perform(delete(entityHelper.getResourceUrl() + "/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(String.format(EntityDoesNotExistException.MESSAGE, entityHelper.getEntityName(), 2)));
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        this.entityHelper = (EntityHelper) beanFactory.getBean("entityHelper",entityName);
        aEntity = (Identifiable) this.entityHelper.aEntity();
    }
}
