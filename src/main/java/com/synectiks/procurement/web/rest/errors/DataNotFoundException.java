package com.synectiks.procurement.web.rest.errors;

public class DataNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public DataNotFoundException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
