package com.church.elim.domain;

import com.church.elim.domain.views.ParishionerAdminView;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class ParishionerQueryCondition implements Serializable {
	
	private ParishionerAdminView.Field field;
	private String value;
	
	public ParishionerQueryCondition(){
		
	}
	
	public ParishionerQueryCondition(ParishionerAdminView.Field field, String value){
		this.field = field;
		this.value = value;
	}
	
	@JsonProperty
	public ParishionerAdminView.Field getField(){
		return field;
	}
	
	public void setField (ParishionerAdminView.Field field){
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
