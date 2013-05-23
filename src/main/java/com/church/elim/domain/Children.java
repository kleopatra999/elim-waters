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
@Table(name = "children")
public class Children implements Serializable {
    private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne( fetch=FetchType.EAGER )
	@JoinColumn(name="idParent",referencedColumnName="id")
	private Person parent;

	@ManyToOne( fetch=FetchType.EAGER )
	@JoinColumn(name="idChild",referencedColumnName="id")
	private Person child;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Person getParent() {
		return parent;
	}

	public void setParent(Person parent) {
		this.parent = parent;
	}

	public Person getChild() {
		return child;
	}

	public void setChild(Person child) {
		this.child = child;
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
		if(parent!=null){
			sb.append(String.valueOf(parent.getFirstName()));
			sb.append(String.valueOf(parent.getLastName()));

		}

		if(child!=null){
			sb.append(String.valueOf(child.getFirstName()));
			sb.append(String.valueOf(child.getLastName()));

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
		String parishioner = this.getSearchableString();
		String[] searches = search.split("\\s+");
		for(String s:searches)
			if(!parishioner.toLowerCase().contains(s)){
				contains = false;
			}
		return contains;
	}

	@Override 
	public boolean equals(Object children){
        Children c = (Children) children;
		return this.getSearchableString().equals(c.getSearchableString());
	}
}
