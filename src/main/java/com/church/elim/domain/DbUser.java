package com.church.elim.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class DbUser implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @Column(name = "username", length = 45)
    private String username;
    
    @Column(name = "password", length = 45)
    private String password;
    
    @Column(name = "access", length = 45)
    private Integer access;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getAccess() {
		return access;
	}

	public void setAccess(Integer access) {
		this.access = access;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
