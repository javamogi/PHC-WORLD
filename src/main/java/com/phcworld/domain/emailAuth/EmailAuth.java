package com.phcworld.domain.emailAuth;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailAuth {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String email;
	
	private String authKey;
	
	private boolean authenticate;

	public boolean matchAuthKey(String authKey) {
		if(authKey == null) {
			return false;
		}
		return authKey.equals(this.authKey);
	}
	
}
