package com.phcworld.domain.exception;

public class LoginNotUserException extends RuntimeException {
	
	private String message;
	
	public LoginNotUserException(String errorMessage) {
		this.message = errorMessage;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
