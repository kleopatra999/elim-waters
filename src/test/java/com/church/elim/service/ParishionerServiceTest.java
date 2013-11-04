package com.church.elim.service;

import com.church.elim.ElimTest;
import com.church.elim.domain.Parishioner;
import com.church.elim.domain.Person;
import com.church.elim.repository.ParishionerRepository;
import com.church.elim.repository.PersonRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
@Ignore
public class ParishionerServiceTest extends ElimTest{
	@PersistenceContext
	EntityManager entityManager;
	@Autowired 
	ParishionerRepository parishionerRepo;
	@Autowired 
	PersonRepository personRepo;
	@Autowired
	private ApplicationContext applicationContext;
	//@Autowired 
	ParishionerService service;
	
	@Before
	public void setup(){
		service = applicationContext.getBean(ParishionerService.class);
	}
	@Test
	public void testParishionerCRUD(){
		// Test CREATE
		Parishioner ionel = save("Ionel", "Pop");
		
		// Test READ
		Long ionelId = ionel.getId();
		assert ionel != null;
		Parishioner added = parishionerRepo.findOne(ionelId);
		System.out.println("The added parishioner:" + added);
		assert added!= null;
		assert ionel.getPerson().getFirstName().equals(added.getPerson().getFirstName());
		
		// Test UPDATE 
		System.out.println("The parishioner exists: " + parishionerRepo.exists(added.getId()));
		String secondFirstName = "Gheorghe";
		added.getPerson().setFirstName(added.getPerson().getFirstName()+" "+secondFirstName);
		added.setBaptismDate(new Date());
		parishionerRepo.saveAndFlush(added);
		Parishioner modified = parishionerRepo.findOne(ionelId);
		System.out.println("The modified parishioner:" + modified);
		assert modified.getPerson().getFirstName().contains(added.getPerson().getFirstName());
		assert modified.getPerson().getFirstName().contains(secondFirstName);
		assert modified.getBaptismDate().after(added.getBaptismDate());
		
		// Test DELETE
		personRepo.delete(modified.getPerson());
		added = parishionerRepo.findOne(ionelId);
		assert added==null;
		System.out.println("The parishioner exists: " + parishionerRepo.exists(modified.getId()));
	}

	public void testFindByFirstNameAndLastName(){
		Parishioner ionel = save("Pop", "Ionica");
		Long ionelId = ionel.getId();
		assert ionel !=null;
		List<Parishioner> parishioners= parishionerRepo.findByFirstNameAndLastName(ionel.getPerson().getFirstName(),
				ionel.getPerson().getLastName());
		Parishioner p=null;
		for(Parishioner par:parishioners){
			if(par.getId()==ionel.getId()){
				p=par;
			}
		}
		System.out.println("The added parishioner:" + p);
		assert p!=null;
		assert ionel.getPerson().getFirstName().
		equals(p.getPerson().getFirstName());
		personRepo.delete(p.getPerson());
		p = parishionerRepo.findOne(ionelId);
		assert p==null;
	}

	@Test
	public void testUpdateChurch() {
		Parishioner ionut = save("Pop", "Ionut");
		assert ionut != null;
		assert ionut.getChurch() == null;
		String churchName = "ChurchName";
		ionut.setChurch(churchName);
		parishionerRepo.saveAndFlush(ionut);
		Parishioner modified = parishionerRepo.findOne(ionut.getId());
		assert modified != null;
		assert modified.getChurch() != null;
		assert modified.getChurch().contentEquals(churchName);
	}
	
	@Test
	public void testDeleteByIdGreatherThan(){
		Parishioner p = new Parishioner();
		Person person = new Person();
		person.setFirstName("FirstName");
		person.setLastName("LastName");
		p.setPerson(person);
		p = parishionerRepo.saveAndFlush(p);
		assertNotNull(p);
		service.deleteParishionersByIdGreatherThan((long) 1);
		assert parishionerRepo.findOne(p.getId())==null;
	}
	
	private Parishioner save(String firstName, String lastName) {
		Person person = new Person();
		person.setFirstName(lastName);
		person.setLastName(firstName);
		Parishioner parishioner = new Parishioner();
		parishioner.setPerson(person);
		parishioner.setBaptismDate(new Date());
		Parishioner ionel = parishionerRepo.saveAndFlush(parishioner);
		return ionel;
	}
	

}
