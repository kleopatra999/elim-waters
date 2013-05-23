package com.church.elim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.church.elim.domain.Parishioner;
import com.church.elim.domain.views.ParishionerAdminView;

public interface ParishionerRepository extends JpaRepository<Parishioner, Long>{
	public final static String adminSelectString = ParishionerAdminView.getSelectString(); 
	@Query("update Parishioner p set p.church = :church where p.id = :id")
	public void updateChurch(Long id, String church);
	
	@Query("from Parishioner p " +
			"where p.firstName=:firstName and p.lastName=:lastName")
	public List<Parishioner> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
	
	@Query("from Parishioner p " +
			"where p.firstName like :firstName and p.lastName like :lastName")
	public List<Parishioner> findByConditions(@Param("firstName") String firstName, @Param("lastName") String lastName);
	
	@Modifying
	@Query("delete from Parishioner parishioner where parishioner.id>:startId")
	public void deleteStartingFrom(@Param("startId") Long id);
}
