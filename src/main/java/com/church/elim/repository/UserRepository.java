package com.church.elim.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.church.elim.domain.DbUser;

public interface UserRepository extends JpaRepository<DbUser, String>{
	public DbUser findByUsername(String name);
}
