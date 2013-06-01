package com.church.elim;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.church.elim.configuration.TestDataConfiguration;
import com.church.elim.controller.DataTablesRequest;
import com.church.elim.repository.ParishionerRepository;
import com.church.elim.repository.PersonRepository;
import com.church.elim.service.CustomUserDetailsService;
import com.church.elim.utils.ElimMessage;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.ServletWebRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"default", "dev"})
@ContextConfiguration(classes={TestDataConfiguration.class})
public class ElimTest {
    @Autowired
    protected
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
	ElimMessage messageSource;
	@Autowired
	ParishionerRepository parishionerRepo;
	@Autowired
	CustomUserDetailsService userService;
	@Autowired
	PersonRepository personRepo;
	@Autowired
	RestTemplate restTemplate;
	String baseURI = "http://localhost:9999/elim";
	//@Autowired
	protected DaoAuthenticationProvider authenticationProvider = Mockito.mock(DaoAuthenticationProvider.class);
	String adminSessionID;
	String jsession="";
	protected void login(){
		if(adminSessionID == null || adminSessionID.isEmpty()){
			adminSessionID = getJSessionID("elimUser","elimUser");
		}
		jsession = ";jsessionid="+adminSessionID;
		// authenticate user 
		/*UserDetails user = getAdminUser();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, "exod15:27", user.getAuthorities());
                Authentication auth = authenticationProvider.authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(auth);*/
	}
	protected void logout(){
		jsession = "";
		// SecurityContextHolder.clearContext();
	}

	protected URI getUri(String relativeURI) throws URISyntaxException{
		URI uri = new URI(baseURI + relativeURI+jsession); 
		return uri;
	}

	private String getJSessionID(String user, String password){
		final String username = user;
		final String pwd = password;
		final String elimUri = baseURI;
		String jsessionid = restTemplate.execute(elimUri + "/j_spring_security_check", HttpMethod.POST,
				new RequestCallback() {
			@Override
			public void doWithRequest(ClientHttpRequest request) throws IOException {
				String req = "j_username="+username+"&j_password="+pwd;
				request.getBody().write(req.getBytes());
			}
		},
		new ResponseExtractor<String>() {
			@Override
			public String extractData(ClientHttpResponse response) throws IOException {
				List<String> cookies = response.getHeaders().get("Cookie");
				// assuming only one cookie with jsessionid as the only value
				if (cookies == null) {
					cookies = response.getHeaders().get("Set-Cookie");
				}
				String cookie = cookies.get(cookies.size() - 1);
				int start = cookie.indexOf('=');
				int end = cookie.indexOf(';');
				return cookie.substring(start + 1, end);
			}
		});
		return jsessionid;
	}

	public UserDetails getAdminUser(){
		return userService.loadUserByUsername("admin");
	}

	@Test
	public void testMessageSource(){
		//assert messageSource.getMessage("parishioner.delete.success", "Pop Ionel").contains("was permanently removed from the database!");
	}


	public RestTemplate getRestTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new FormHttpMessageConverter());
		messageConverters.add(new StringHttpMessageConverter());
		messageConverters.add(new MappingJacksonHttpMessageConverter());
		restTemplate.setMessageConverters(messageConverters);
		return restTemplate;
	}

	public HttpEntity createFormEntity(MultiValueMap<String, ?> map){
		HttpHeaders requestHeaders = new HttpHeaders();
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

		requestHeaders.setAccept(acceptableMediaTypes);
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		return new HttpEntity<Object>(map,requestHeaders);  
	}

	public Object createDataTableEntity (DataTablesRequest dataTable) {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<Object>(dataTable,requestHeaders);
	}

	public HttpEntity createEntityAcceptAll(Object request){
		HttpHeaders requestHeaders = new HttpHeaders();
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.ALL);
		return new HttpEntity<Object>(request,requestHeaders);  
	}
	
	public String toJSON(Object object) throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper(); 
		String json = mapper.writeValueAsString(object);
		return json;
	}
}