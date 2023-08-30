package com.phcworld.domain.exception;

public class MatchNotUserException extends RuntimeException {
	
	private String message;

	public MatchNotUserException(String errorMessage) {
		this.message = errorMessage;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
