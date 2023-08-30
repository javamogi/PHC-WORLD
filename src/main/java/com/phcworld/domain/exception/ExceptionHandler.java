package com.phcworld.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.phcworld.domain.api.model.response.ErrorResponse;

@ControllerAdvice
@RestController
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(value = LoginNotUserException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public ErrorResponse handlerLoginNotUserException(LoginNotUserException e) {
		return ErrorResponse.builder()
				.status("401")
				.error(e.getMessage())
				.build();
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = MatchNotUserException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public ErrorResponse handlerMatchNotUserException(MatchNotUserException e) {
		return ErrorResponse.builder()
				.status("403")
				.error(e.getMessage())
				.build();
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = UserNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ErrorResponse handlerUserNotFoundException(UserNotFoundException e) {
		return ErrorResponse.builder()
				.status("404")
				.error(e.getMessage())
				.build();
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = ContentsEmptyException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorResponse handlerContentsEmptyException(ContentsEmptyException e) {
		return ErrorResponse.builder()
				.status("400")
				.error(e.getMessage())
				.build();
	}
	
}
