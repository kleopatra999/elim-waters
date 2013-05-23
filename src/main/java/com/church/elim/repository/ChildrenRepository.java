package com.church.elim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.church.elim.domain.Children;

public interface ChildrenRepository extends JpaRepository<Children, Long>{
	@Query("from Children children " +
			"where children.child.id=:idChild and children.parent.id=:idParent")
	public List<Children> findByParentIdAndChildId(@Param("idParent") Long idParent, @Param("idChild") Long idChild);
}
