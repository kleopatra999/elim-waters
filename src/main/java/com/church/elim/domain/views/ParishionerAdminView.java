package com.church.elim.domain.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.codehaus.jackson.annotate.JsonProperty;

import com.church.elim.domain.Parishioner;

/* This class defines the fields which are going to be displayed in a table view
 * of the parishioner. 
 */
public class ParishionerAdminView {
	public static enum Field{
		ID("id"), 
		FIRST_NAME("firstName","person"), 
		LAST_NAME("lastName","person"), 
		BIRTH_DATE("birthDate","person"),
		ADDRESS("address","person"), 
		// CNP("cnp","person"),
		PHONE("phone","person"), 
		EMAIL("email","person"),
		WORKPLACE("workplace","person"), 
		MARITAL_STATUS("maritalStatus","person"), 
		STUDIES("studies","person"), 
		BAPTISM_DATE("baptismDate"), 
		CHURCH("church");
		
		private String name;
		private String prefix = "";
		private Field(String name){
			this.name = name;
		};
		
		private Field(String name, String prefix){
			this.name = name;
			this.prefix=prefix;
		};
		public String toString(){
			return name;
		}
		public String fullName(){
			return prefix+"."+name;
		}
	}
	private Long id;
	private String firstName;
	private String lastName;
	private Date birthDate;
	private String address;
	private String cnp;
	private String phone;
	private String email;
	private String workplace;
	private String maritalStatus;
	private String studies;
	private Date baptismDate;
	private String church;
	public ParishionerAdminView(Long id, String firstName, String lastName,
			Date birthDate, String address, String phone, String email,
			String workplace, String maritalStatus, String studies,
			Date baptismDate,String church) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.workplace = workplace;
		this.maritalStatus = maritalStatus;
		this.studies = studies;
		this.baptismDate = baptismDate;
		this.church=church;
	}
	@JsonProperty
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@JsonProperty
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	@JsonProperty
	public String getCnp() {
		return cnp;
	}
	public void setCnp(String cnp) {
		this.cnp = cnp;
	}

	@JsonProperty
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@JsonProperty
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty
	public String getWorkplace() {
		return workplace;
	}
	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}

	@JsonProperty
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	@JsonProperty
	public String getStudies() {
		return studies;
	}
	public void setStudies(String studies) {
		this.studies = studies;
	}

	@JsonProperty
	public Date getBaptismDate() {
		return baptismDate;
	}
	public void setBaptismDate(Date baptismDate) {
		this.baptismDate = baptismDate;
	}

	@JsonProperty
	public String getChurch() {
		return church;
	}
	public void setChurch(String church) {
		this.church = church;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(String.valueOf(this.getAddress()));
		sb.append(String.valueOf(this.getBaptismDate()));
		sb.append(String.valueOf(this.getChurch()));
		// sb.append(String.valueOf(this.getCnp()));
		sb.append(String.valueOf(this.getEmail()));
		sb.append(String.valueOf(this.getFirstName()));
		sb.append(String.valueOf(this.getLastName()));
		sb.append(String.valueOf(this.getMaritalStatus()));
		sb.append(String.valueOf(this.getPhone()));
		sb.append(String.valueOf(this.getStudies()));
		sb.append(String.valueOf(this.getWorkplace()));
		return sb.toString();
	}

	// concatenates all the fields of this object and searches for a text in them
	public Boolean contains(String searchText){
		Boolean contains = true;
		String search = searchText.toLowerCase();
		String parishioner = this.toString();
		String[] searches = search.split("\\s+");
		for(String s:searches)
			if(!parishioner.toLowerCase().contains(s)){
				contains = false;
			}
		return contains;
	}
	
	public static String getFieldName(Field field){
		return field.toString();
	}
	
	public static String getFieldName(int index){
		return getFieldName(Field.values()[index]);
	}
	
	public static List<String> getFieldNames(){
		ArrayList<String> names = new ArrayList<String>();
		for(Field field:Field.values()){
			getFieldName(field);
		}
		return names;
	}
	
	public static Path<String> getSelectPath(Root<Parishioner> root, Field field){
		Path<String> path = null; 
		if(isParishionerField(field)){
			path = root.<String>get(getFieldName(field));
		}else{
			path = root.<String>get("person").get(getFieldName(field));
		}
		return path;
	}
	
	public static Path<String> getSelectPath(Root<Parishioner> root, String field){
		Path<String> path = null; 
		if(isParishionerField(field)){
			path = root.<String>get(field);
		}else{
			path = root.<String>get("person").get(field);
		}
		return path;
	}
	
	public static Path<String> getSelectPath(Root<Parishioner> root, Integer  fieldIndex){
		Field field = Field.values()[fieldIndex];
		return getSelectPath(root, field);
	}
	
	@SuppressWarnings("unchecked")
	public static Path<String>[] selectAllPath(Root<Parishioner> root){
		List<Path<String>> selections = new ArrayList<Path<String>>();
		for(Field f: Field.values()){
			System.out.println(getSelectPath(root, f));
			selections.add(getSelectPath(root, f));
		}
		return selections.toArray(new Path[0]);
	}
	
	@SuppressWarnings("unchecked")
	public static Path<String>[] orderBy(Root<Parishioner> root){
		List<Path<String>> selections = new ArrayList<Path<String>>();
		for(Field f: Field.values()){
			selections.add(getSelectPath(root, f));
		}
		return selections.toArray(new Path[0]);
	}
	public static Boolean isParishionerField(Field field){
		switch (field){
		case BAPTISM_DATE:
			return true;
		case CHURCH:
			return true;
		case ID:
			return true;
		default:
			return false;
		}
	}
	
	public static Boolean isParishionerField(String field){
		if (field.equalsIgnoreCase(Field.BAPTISM_DATE.toString()))
			return true;
		
		if (field.equalsIgnoreCase(Field.CHURCH.toString()))
			return true;
		
		if (field.equalsIgnoreCase(Field.ID.toString()))
			return true;
		
		return false;
	}
	
	public static Boolean isHiddenField(Field field){
		switch (field){
		case ID:
			return true;
		default:
			return false;
		}
	}

	public static String getSelectString(){
		String selectString ="";
		String delimiter = ",";
		String del = "";
		for(Field field:Field.values()){
			selectString+=del + "parishioner." + field.fullName();
			del = delimiter;
		}
		return selectString;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
}
