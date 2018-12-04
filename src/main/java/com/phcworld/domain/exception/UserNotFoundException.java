package com.phcworld.domain.exception;

public class UserNotFoundException extends RuntimeException {
	private String message;

	public UserNotFoundException(String errorMessage) {
		this.message = errorMessage;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
