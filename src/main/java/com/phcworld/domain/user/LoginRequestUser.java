package com.phcworld.domain.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LoginRequestUser {

	private String email;
	
	private String password;
	
}
