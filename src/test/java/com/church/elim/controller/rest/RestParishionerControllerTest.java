package com.church.elim.controller.rest;

import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.church.elim.ElimTest;
import com.church.elim.controller.DataTablesRequest;
import com.church.elim.controller.DataTablesResponse;
import com.church.elim.domain.Parishioner;
import com.church.elim.domain.ParishionerQueryCondition;
import com.church.elim.domain.Person;
import com.church.elim.domain.views.ParishionerAdminView.Field;
import com.church.elim.repository.ParishionerRepository;
import com.church.elim.repository.PersonRepository;
import com.church.elim.service.CustomUserDetailsService;
import com.church.elim.service.ParishionerService;
import com.church.elim.utils.ElimMessage;
public class RestParishionerControllerTest extends ElimTest{
        @Autowired 
        ElimMessage messageSource;
        @Autowired
        ParishionerRepository parishionerRepo;
        @Autowired
        ParishionerService parishionerService;

        @Autowired
        CustomUserDetailsService userService;
        @Autowired
        PersonRepository personRepo;
        @Autowired
        RestTemplate restTemplate;
        String baseURI = "http://localhost:9999/elim";
        Parishioner popIonel;
        @Autowired
        protected DaoAuthenticationProvider authenticationProvider;
        String jsession="";
        
        public static final String EXISTING_FIRST_NAME = "Laurian";
        public static final String EXISTING_LAST_NAME = "Rognean";
        
        public static final String NON_EXISTING_FIRST_NAME = "Nonexistingfirstname";
        public static final String NON_EXISTING_LAST_NAME = "Nonexistinglastname";
        
        public static final String CONTAINING_FIRST_NAME = "%i%";
        public static final String STARTING_LAST_NAME = "Ro%";
        
        public static final String SEARCH_QUERY = "Maria";
        
        public static final String MARRIED_MARITAL_STATUS = "married";
        
        @Before
        public void setUp() {
                login();
                restTemplate = getRestTemplate();
                Person person = new Person();
                person.setFirstName("Pop");
                person.setLastName("Ionel");
                popIonel = new Parishioner();
                popIonel.setPerson(person);
                popIonel.setBaptismDate(new Date());
        }

        @Test
        public void testParishionerCRUD() throws URISyntaxException{
                // create
                URI uri = getUri("/rest/parishioners/create");
                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                map.add("person.firstName", popIonel.getPerson().getFirstName());
                map.add("person.lastName", popIonel.getPerson().getLastName());
                System.out.println(restTemplate.postForEntity(uri.toString(), createFormEntity(map), String.class));
                List<Parishioner> parishioners = parishionerRepo.findByFirstNameAndLastName(popIonel.getPerson().getFirstName(), 
                                popIonel.getPerson().getLastName());
                assert parishioners.size() > 0;
                Parishioner ionel = parishioners.get(0);

                assertNotNull(ionel);

                // create
                Long ionelId = ionel.getId(); 
                System.out.println("Ionel id:" + ionelId);
                uri = getUri("/rest/parishioners/"+ionelId);
                ResponseEntity<Parishioner> parishioner = restTemplate.getForEntity(uri, Parishioner.class);
                assertNotNull(parishioner);
                assert parishioner.getBody().getPerson().getFirstName().equals(
                                popIonel.getPerson().getFirstName()) 
                                && parishioner.getBody().getPerson().getLastName().equals(
                                                popIonel.getPerson().getLastName());

                // update
                map = new LinkedMultiValueMap<String, Object>();
                Parishioner updatedIonel = setUpdatedIonel(parishioner.getBody(), 
                                map);
                uri = getUri("/rest/parishioners/update");
                ResponseEntity<Map> response = restTemplate.postForEntity(uri.toString(), createFormEntity(map), Map.class);
                assert response.getStatusCode().equals(HttpStatus.OK);
                System.out.println(response);
                Map<String, Object> results = (Map<String, Object>) response.getBody();
                for(String key:results.keySet()){
                        System.out.println(response.getBody().get(key));
                }
                assert response.getBody().get("success").toString().equals("true");
                uri = getUri("/rest/parishioners/"+parishioner.getBody().getId().toString());
                System.out.println("the id:" + parishioner.getBody().getId());
                System.out.println("Get URI:" + uri);
                parishioner = restTemplate.getForEntity(uri, Parishioner.class);
                assertNotNull(parishioner.getBody());
                System.out.println("Updated parishioner1"  + parishionerRepo.findByFirstNameAndLastName(
                                updatedIonel.getPerson().getFirstName(), 
                                updatedIonel.getPerson().getLastName()));
                System.out.println("Updated parishioner"  + parishioner.getBody());
                assert updatedIonel.equals(parishioner.getBody());

                // delete
                uri = getUri("/rest/parishioners/delete/"+ionelId);
                String deleteResponse = restTemplate.postForObject(uri, createEntityAcceptAll(null), String.class);
                System.out.println("Delete response:" + deleteResponse);
                ionel = parishionerRepo.findOne(ionelId);
                assert ionel==null;
        }
        
        @Test
        public void testParishionerFilter() throws URISyntaxException {
                URI uri = getUri("/rest/parishioners/data");
                
                DataTablesRequest request = new DataTablesRequest();
                List<ParishionerQueryCondition> searchCriteria = new ArrayList<ParishionerQueryCondition>();
                
                // Testing filter condition that returns empty collection 
                searchCriteria.add(new ParishionerQueryCondition(Field.LAST_NAME, "=" + NON_EXISTING_LAST_NAME));
                searchCriteria.add(new ParishionerQueryCondition(Field.FIRST_NAME, "=" + NON_EXISTING_FIRST_NAME));
                request.searchQuery = "";
                request.searchCriteria = searchCriteria;
                DataTablesResponse response = restTemplate.postForObject(uri.toString(), createDataTableEntity(request), DataTablesResponse.class);
                assert response.totalRecords == 0;
                assert response.data.size() == 0;
                
                // Testing filter criteria that returns single item
                searchCriteria.clear();
                searchCriteria.add(new ParishionerQueryCondition(Field.LAST_NAME, "=" + EXISTING_LAST_NAME));
                searchCriteria.add(new ParishionerQueryCondition(Field.FIRST_NAME, "=" + EXISTING_FIRST_NAME));
                request.searchCriteria = searchCriteria;
                response = restTemplate.postForObject(uri.toString(), createDataTableEntity(request), DataTablesResponse.class);
                assert response.totalRecords == 1;
                assert response.data.size() == 1;
                List<Parishioner> matches = parishionerRepo.findByFirstNameAndLastName(EXISTING_FIRST_NAME, EXISTING_LAST_NAME);
                Parishioner matchingParishioner = matches.get(0);
                assert response.data.containsAll(matches);
                ParishionerQueryCondition[] criteriaArray = searchCriteria.toArray(new ParishionerQueryCondition[searchCriteria.size()]);
                System.out.println(response.totalRecords + " results for search query " + Arrays.toString(criteriaArray));
                for (int i=0; i<response.totalRecords; i++)
                        System.out.println(response.data.get(i));

                // Testing filter criteria that returns multiple items
                searchCriteria.clear();
                searchCriteria.add(new ParishionerQueryCondition(Field.LAST_NAME, STARTING_LAST_NAME));
                searchCriteria.add(new ParishionerQueryCondition(Field.FIRST_NAME, CONTAINING_FIRST_NAME));
                request.searchCriteria = searchCriteria;
                response = restTemplate.postForObject(uri.toString(), createDataTableEntity(request), DataTablesResponse.class);
                assert response.totalRecords > 0;
                assert response.data.size() > 0;
                matches = parishionerRepo.findByConditions(CONTAINING_FIRST_NAME, STARTING_LAST_NAME);
                assert response.data.containsAll(matches);
                assert response.data.contains(matchingParishioner);
                criteriaArray = searchCriteria.toArray(new ParishionerQueryCondition[searchCriteria.size()]);
                System.out.println(response.totalRecords + " results for search query " + Arrays.toString(criteriaArray));
                for (int i=0; i<response.totalRecords; i++)
                        System.out.println(response.data.get(i));
                
                // Testing filter criteria and search query
                request.searchQuery = SEARCH_QUERY;
                response = restTemplate.postForObject(uri.toString(), createDataTableEntity(request), DataTablesResponse.class);
                assert response.totalRecords > 0 && response.totalRecords <= matches.size();
                assert response.data.size() > 0 && response.totalRecords <= matches.size();
                assert !response.data.containsAll(matches);
                assert !response.data.contains(matchingParishioner);
                List<Parishioner> searched = parishionerRepo.findByConditions("%" + SEARCH_QUERY + "%", STARTING_LAST_NAME);
                assert searched.containsAll(response.data);
                criteriaArray = searchCriteria.toArray(new ParishionerQueryCondition[searchCriteria.size()]);
                System.out.println(response.totalRecords + " results for search query " + Arrays.toString(criteriaArray));
                for (int i=0; i<response.totalRecords; i++)
                        System.out.println(response.data.get(i));
                
                // Testing filter criteria with complex filtering condition
                searchCriteria.add(new ParishionerQueryCondition(Field.MARITAL_STATUS, "!%" + MARRIED_MARITAL_STATUS + "%"));
                request.searchQuery = "";
                request.searchCriteria = searchCriteria;
                response = restTemplate.postForObject(uri.toString(), createDataTableEntity(request), DataTablesResponse.class);
                assert response.totalRecords > 0 && response.totalRecords <= matches.size();
                assert response.data.size() > 0 && response.totalRecords <= matches.size();
                List<Parishioner> notMarriedMatches = response.data;
                assert !matches.contains(matchingParishioner);
                assert matches.containsAll(notMarriedMatches);
                criteriaArray = searchCriteria.toArray(new ParishionerQueryCondition[searchCriteria.size()]);
                System.out.println(response.totalRecords + " results for search query " + Arrays.toString(criteriaArray));
                for (int i=0; i<response.totalRecords; i++)
                        System.out.println(response.data.get(i));
                
                // Testing filter criteria with complex complementary filtering condition
                searchCriteria.remove(searchCriteria.size()-1);
                searchCriteria.add(new ParishionerQueryCondition(Field.MARITAL_STATUS, "=" + MARRIED_MARITAL_STATUS + ""));
                request.searchQuery = "";
                request.searchCriteria = searchCriteria;
                response = restTemplate.postForObject(uri.toString(), createDataTableEntity(request), DataTablesResponse.class);
                assert response.totalRecords > 0 && response.totalRecords <= matches.size();
                assert response.data.size() > 0 && response.totalRecords <= matches.size();
                List<Parishioner> marriedMatches = response.data;
                assert matches.containsAll(marriedMatches);
                assert marriedMatches.contains(matchingParishioner);
                Set<Parishioner> marriedAndUnmarried = new HashSet<Parishioner>();
                marriedAndUnmarried.addAll(marriedMatches);
                marriedAndUnmarried.addAll(notMarriedMatches);
                assert marriedAndUnmarried.equals(matches);
                criteriaArray = searchCriteria.toArray(new ParishionerQueryCondition[searchCriteria.size()]);
                System.out.println(response.totalRecords + " results for search query " + Arrays.toString(criteriaArray));
                for (int i=0; i<response.totalRecords; i++)
                        System.out.println(response.data.get(i));
        }

        @Test
        public void testParishionerDeleteAccessDenied() throws Exception{
                logout();
                URI uri = getUri("/rest/parishioners/delete/"+1234567);
                String location = restTemplate.postForLocation(uri, null).toString();
                // if access denied it will redirect to login page
                assert location.contains("http://localhost:9999/elim/auth/login");
                ResponseEntity response = restTemplate.postForEntity(uri, null, String.class);
                System.out.println("Post result entity: "  +response);
                System.out.println("Post result entity: "  +response.getBody());
                login();
        }

        private Parishioner setUpdatedIonel(Parishioner parishioner,
                        MultiValueMap<String, Object> form){
                Parishioner uParishioner = new Parishioner();
                uParishioner.setId(parishioner.getId());
                Person person = new Person();
                person.setId(uParishioner.getId());
                person.setFirstName("newFirstName");
                person.setLastName("newLastName");
                person.setWorkplace("newWorkplace");
                person.setBirthDate(new Date("08/18/2012"));
                uParishioner.setPerson(person);
                form.add("id", uParishioner.getId().toString());
                form.add("person.id", uParishioner.getId().toString());
                form.add("person.firstName", person.getFirstName());
                form.add("person.lastName", person.getLastName());
                form.add("person.birthDate", "08/18/2012");
                form.add("person.workplace", person.getWorkplace());
                return uParishioner;
        }

        @After
        public void tearDown(){
                // clean Parishioner table for all the added data
                parishionerService.deleteParishionersByIdGreatherThan((long) 1);
        }
}
