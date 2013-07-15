package com.church.elim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.church.elim.domain.Caregiver;
import com.church.elim.domain.views.CaregiverAdminView;

public interface CaregiverRepository extends JpaRepository<Caregiver, Long>{
	public final static String adminSelectString = CaregiverAdminView.getSelectString(); 
	
	@Query("from Caregiver p " +
			"where p.person.firstName=:firstName and p.person.lastName=:lastName")
	public List<Caregiver> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
	

	@Modifying
	@Query("delete from Caregiver p where p.id>:id")
	public void deleteByIdGreatherThan(@Param("id") Long id);
}
