package com.phcworld.domain.user;

import com.phcworld.domain.email.EmailAuth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class LoginRequestUser {

	private String email;
	
	private String password;
	
}
