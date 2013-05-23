package com.church.elim.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(DbUser.class)
public abstract class DbUser_ {

	public static volatile SingularAttribute<DbUser, String> username;
	public static volatile SingularAttribute<DbUser, Integer> access;
	public static volatile SingularAttribute<DbUser, String> password;

}

