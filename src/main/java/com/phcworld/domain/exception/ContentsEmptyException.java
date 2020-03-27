package com.phcworld.domain.exception;

public class ContentsEmptyException extends RuntimeException {

	private String message;
	
	public ContentsEmptyException(String errorMessage) {
		this.message = errorMessage;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
