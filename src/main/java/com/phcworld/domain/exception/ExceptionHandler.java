package com.phcworld.domain.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(value = LoginNotUserException.class)
	public String handlerLoginNotUserException(LoginNotUserException e) {
		return "{\"error\":\"" + e.getMessage() +"\"}";
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = MatchNotUserExceptioin.class)
	public String handlerMatchNotUserException(MatchNotUserExceptioin e) {
		return "{\"error\":\"" + e.getMessage() +"\"}";
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = UserNotFoundException.class)
	public String handlerUserNotFoundException(UserNotFoundException e) {
		return "{\"error\":\"" + e.getMessage() +"\"}";
	}
	
}
