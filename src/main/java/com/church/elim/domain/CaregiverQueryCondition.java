package com.church.elim.domain;

import com.church.elim.domain.views.CaregiverAdminView;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class CaregiverQueryCondition implements Serializable {
	
	private CaregiverAdminView.Field field;
	private String value;
	
	public CaregiverQueryCondition(){
		
	}
	
	public CaregiverQueryCondition(CaregiverAdminView.Field field, String value){
		this.field = field;
		this.value = value;
	}
	
	@JsonProperty
	public CaregiverAdminView.Field getField(){
		return field;
	}
	
	public void setField (CaregiverAdminView.Field field){
		this.field = field;
	}
	
	@JsonProperty
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return "[" + this.field + " " + this.value + "]";
	}
}
