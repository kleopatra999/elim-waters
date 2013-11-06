package com.church.elim.web.controller;

import com.church.elim.ElimTest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class AuthenticationTest extends ElimTest{
	@Autowired WebApplicationContext wac;
    @Autowired
    private LdapAuthenticationProvider authenticationProvider;
    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;
    @Autowired
    LdapUserSearch ldapUserSearch;
    @Test
    public void testAuth() throws Exception{
        Authentication authentication = new UsernamePasswordAuthenticationToken( "glanceuser", "Admin123" );
        //Authentication responseAuthentication = authenticationProvider.authenticate( authentication );
        System.out.println(ldapUserSearch.searchForUser("glanceuser"));
        Authentication auth = authenticationManager.authenticate(authentication);
        for(String requiredAuth: new String[]{"ROLE_ADMIN","ROLE_USER"}){
            boolean found = false;
            for(GrantedAuthority authority:auth.getAuthorities()){
                if (authority.getAuthority().equals(requiredAuth)){
                    found = true;
                }
            }
            assertTrue("User does not have required authority " + requiredAuth,found);
        }
        System.out.println("end");
    }
    @After
	public void tearDown(){
	}
}
