package com.church.elim.domain;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.junit.Ignore;
import org.springframework.format.annotation.DateTimeFormat;

@JsonIgnoreProperties(ignoreUnknown = true) 
@Entity
@Table(name = "caregiver")
public class Caregiver implements Serializable, Identifiable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER )
	@JoinColumn(name="id",referencedColumnName="id")
	private Person person;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Transient
	private String searchableString;

	/* Concatenate all fields which are searchable (e.g. firstName is searchable, 
	 * while id is not as it is not exposed to the user) and return the result.
	 * The result can be further used in contains method and not only.
	 */
	@Ignore
	@JsonIgnore
	public String getSearchableString(){
		StringBuilder sb = new StringBuilder();
		if(person!=null){
			sb.append(String.valueOf(person.getFirstName()));
			sb.append(String.valueOf(person.getLastName()));
		}
		return sb.toString();
	}

	@Override
	public String toString(){
		return getSearchableString();
	}
	// concatenates all the fields of this object and searches for a text in them
	public Boolean contains(String searchText){
		Boolean contains = true;
		String search = searchText.toLowerCase();
		String caregiver = this.getSearchableString();
		String[] searches = search.split("\\s+");
		for(String s:searches)
			if(!caregiver.toLowerCase().contains(s)){
				contains = false;
			}
		return contains;
	}

	@Override 
	public boolean equals(Object p){
		Caregiver caregiver = (Caregiver) p;
		return this.getSearchableString().equals(caregiver.getSearchableString());
	}
}
