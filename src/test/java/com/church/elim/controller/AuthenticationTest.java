package com.church.elim.controller;

import com.church.elim.ElimTest;
import com.church.elim.builders.PersonBuilder;
import com.church.elim.controller.rest.RestPersonController;
import com.church.elim.domain.Person;
import com.church.elim.repository.PersonRepository;
import com.church.elim.service.CustomUserDetailsService;
import com.church.elim.service.EntityDoesNotExistException;
import com.church.elim.service.PersonService;
import org.exolab.castor.mapping.xml.Ldap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class AuthenticationTest extends ElimTest{
	@Autowired WebApplicationContext wac;
    @Autowired
    private LdapAuthenticationProvider authenticationProvider;
	@Test
	public void testAuth() throws Exception{
        Authentication authentication = new UsernamePasswordAuthenticationToken( "glanceuser", "exod15:27" );
        Authentication reponseAuthentication = authenticationProvider.authenticate( authentication );
	}
    @After
	public void tearDown(){
	}
}
