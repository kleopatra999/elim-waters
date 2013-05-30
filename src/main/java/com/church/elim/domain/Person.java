package com.church.elim.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import java.sql.Blob;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "person")
@Inheritance(strategy=InheritanceType.JOINED)
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	/* Ignore this field when serializing it using Jackson, otherwise you'll get infinit loop because it's also referred in children table
	 */
	@JsonIgnore
	@OneToMany (mappedBy="parent", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Set<Children> children = new HashSet<Children>();
	/*@OneToMany(cascade = CascadeType.ALL)
	private List<Children> children;*/ 

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name="Photo")
	@Lob
	private Blob photo;
	@Column(name = "First_Name", length = 45)
	@Size(min=1)
	private String firstName;
	@Column(name = "Last_Name", length = 45)
	@Size(min=1)
	private String lastName;
	@Column(name = "Address", length = 200)
	private String address;
	@Column(name = "Spouse_Name", length = 45)
	private String spouseName;

	@Column(name = "CNP", length = 45)
	@Size(min=1)
	private String cnp;
	@Column(name = "Phone", length = 45)
	private String phone;
	@Column(name = "Email", length = 45)
	private String email;
	@Column(name = "Workplace", length = 45)
	private String workplace;
	@Column(name = "Marriage_Date")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(style = "M-")
	private Date marriageDate;
	@Column(name = "Birth_Date")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(style = "M-")
	private Date birthDate;

	@Column(name = "Marital_Status", length = 20)
	private String maritalStatus;
	@Column(name = "Spouse_Religion", length = 45)
	private String spouseReligion;
	@Column(name = "Studies", length = 45)
	private String studies;
	@Column(name = "Image", length = 45)
	private String image;
	@Column(name = "Company", length = 45)
	private String company;
	@Column(name = "Job", length = 45)
	private String job;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSpouseName() {
		return spouseName;
	}

	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@JsonIgnore
	public Blob getPhoto() {
		return photo;
	}

	public void setPhoto(Blob photo){
		this.photo = photo;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCnp() {
		return cnp;
	}

	public void setCnp(String cnp) {
		this.cnp = cnp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWorkplace() {
		return workplace;
	}

	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}

	public Date getMarriageDate() {
		return marriageDate;
	}

	public void setMarriageDate(Date marriageDate) {
		this.marriageDate = marriageDate;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getSpouseReligion() {
		return spouseReligion;
	}

	public void setSpouseReligion(String spouseReligion) {
		this.spouseReligion = spouseReligion;
	}

	public String getStudies() {
		return studies;
	}

	public void setStudies(String studies) {
		this.studies = studies;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@JsonIgnore
	public Set<Children> getChildren() {
		return children;
	}

	public void setChildren(Set<Children> children) {
		this.children = children;
	}

	public Person(){

	}

	public Person(String firstName, String lastName){
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	protected void setFields(Person person){
		
	}
}
