package com.phcworld.domain.exception;

public class MatchNotUserExceptioin extends RuntimeException {
	
	private String message;

	public MatchNotUserExceptioin(String errorMessage) {
		this.message = errorMessage;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
