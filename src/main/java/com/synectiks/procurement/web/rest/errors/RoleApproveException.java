package com.synectiks.procurement.web.rest.errors;

public class RoleApproveException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public RoleApproveException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
