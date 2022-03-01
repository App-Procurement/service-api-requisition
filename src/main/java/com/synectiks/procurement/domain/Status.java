package com.synectiks.procurement.domain;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Status {

	private int code;
	private String type;
	private String message;
	private Object object;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	
}
