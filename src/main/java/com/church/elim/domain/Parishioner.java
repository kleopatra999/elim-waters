package com.church.elim.domain;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.junit.Ignore;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

@JsonIgnoreProperties(ignoreUnknown = true) 
@Entity
@Table(name = "parishioner")
@PrimaryKeyJoinColumn(name="idParishioner", referencedColumnName="id")
public class Parishioner extends Person implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "Baptism_Date")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(style = "M-")
	private Date baptismDate;

	@Column(name = "Holy_Spirit_Baptism")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(style = "M-")
	private Date holySpiritBaptism;

	@OneToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="baptist")
	@Valid
	private Person baptist;

	@Column(name = "Church", length = 45)
	private String church;
	@Transient
	private String searchableString;

	public Person getPerson(){
		return this;
	}
	public void setPerson(Person person) {
		BeanUtils.copyProperties(person, this);
	}

	public Date getBaptismDate() {
		return baptismDate;
	}

	public void setBaptismDate(Date baptismDate) {
		this.baptismDate = baptismDate;
	}

	public Date getHolySpiritBaptism() {
		return holySpiritBaptism;
	}

	public void setHolySpiritBaptism(Date holySpiritBaptism) {
		this.holySpiritBaptism = holySpiritBaptism;
	}

	public Person getBaptist() {
		return baptist;
	}

	public void setBaptist(Person baptist) {
		this.baptist = baptist;
	}

	public String getChurch() {
		return church;
	}

	public void setChurch(String church) {
		this.church = church;
	}

	/* Concatenate all fields which are searchable (e.g. firstName is searchable, 
	 * while id is not as it is not exposed to the user) and return the result.
	 * The result can be further used in contains method and not only.
	 */
	@Ignore
	@JsonIgnore
	public String getSearchableString(){
		StringBuilder sb = new StringBuilder();
		sb.append(String.valueOf(this.getBaptismDate()));
		sb.append(String.valueOf(this.getChurch()));
			sb.append(String.valueOf(getAddress()));
			sb.append(String.valueOf(getCnp()));
			sb.append(String.valueOf(getEmail()));
			sb.append(String.valueOf(getFirstName()));
			sb.append(String.valueOf(getLastName()));
			sb.append(String.valueOf(getMaritalStatus()));
			sb.append(String.valueOf(getPhone()));
			sb.append(String.valueOf(getStudies()));
			sb.append(String.valueOf(getWorkplace()));
		return sb.toString();
	}

	@Override
	public String toString(){
		return getFirstName() + " " + getLastName();
	}
	// concatenates all the fields of this object and searches for a text in them
	public Boolean contains(String searchText){
		Boolean contains = true;
		String search = searchText.toLowerCase();
		String parishioner = this.getSearchableString();
		String[] searches = search.split("\\s+");
		for(String s:searches)
			if(!parishioner.toLowerCase().contains(s)){
				contains = false;
			}
		return contains;
	}

	@Override 
	public boolean equals(Object p){
		Parishioner parishioner = (Parishioner) p;
		return this.getSearchableString().equals(parishioner.getSearchableString());
	}
	
}
