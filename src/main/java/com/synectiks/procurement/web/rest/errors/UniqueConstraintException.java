package com.synectiks.procurement.web.rest.errors;

public class UniqueConstraintException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public UniqueConstraintException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
