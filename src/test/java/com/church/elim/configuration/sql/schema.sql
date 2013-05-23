CREATE SCHEMA elim;
CREATE MEMORY TABLE person (
  id_person INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,
  First_Name varchar(45) default NULL,
  Last_Name varchar(45) default NULL,
  Address varchar(200) default NULL,
  CNP varchar(45) default NULL,
  Phone varchar(45) default NULL,
  Email varchar(45) default NULL,
  Workplace varchar(45) default NULL,
  Marriage_Date date default NULL,
  Marital_Status varchar(20) default NULL,
  Spouse_Religion varchar(45) default NULL,
  Studies varchar(45) default NULL,
  Image varchar(45) default NULL,
  Company varchar(45) default NULL,
  Job varchar(45) default NULL,
  Birth_Date date default NULL,
  photo BLOB,
  spouse_name varchar(45) default NULL,
  PRIMARY KEY (id_person)
) ;

CREATE MEMORY TABLE caregiver ( idCaregiver INTEGER NOT NULL, 
	id_person INTEGER NOT NULL, 
	id BIGINT NOT NULL, 
	PRIMARY KEY  (idCaregiver), 
	FOREIGN KEY (id_person) REFERENCES Person(id_person)
)
CREATE MEMORY TABLE children (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1),
  id_child BIGINT default NULL,
  id_parent BIGINT default NULL,
  PRIMARY KEY  (id)
);
CREATE MEMORY TABLE parishioner (
  id_parishioner INTEGER NOT NULL,
  Baptism_Date date default NULL,
  Holy_Spirit_Baptism date default NULL,
  Baptist INTEGER default NULL,
  Church varchar(45) default NULL,
  PRIMARY KEY  (id_parishioner),
  FOREIGN KEY (Baptist) REFERENCES person (id_person),
  FOREIGN KEY (id_parishioner) REFERENCES person (id_person)
);

CREATE TABLE user (
  username varchar(45) NOT NULL,
  access INTEGER default NULL,
  password varchar(45) default NULL,
  PRIMARY KEY  (username)
) ;
