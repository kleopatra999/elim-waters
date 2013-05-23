package com.church.elim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.church.elim.domain.Children;
import com.church.elim.domain.Parishioner;
import com.church.elim.domain.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{
	@Modifying
	@Query("delete from Person p where p.id>:id")
	public void deleteByIdGreatherThan(@Param("id") Long id);
	
	@Query("select p.id,p.firstName,p.lastName,p.birthDate from Person p order by lastName,firstName")
	List<Person> findNames();
	
	@Query("from Person p " +
			"where p.firstName=:firstName and p.lastName=:lastName")
	public List<Person> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

	//public void deleteByIdGreatherThan(Long id);
}
