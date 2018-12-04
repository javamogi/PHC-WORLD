package com.phcworld.domain.email;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EmailAuth {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String email;
	
	private String authKey;
	
	private String confirm;
	
	public EmailAuth() {
	}

	public EmailAuth(String email, String authKey) {
		this.email = email;
		this.authKey = authKey;
		this.confirm = "N";
	}

	public boolean auth() {
		if(this.confirm.equals("N")) {
			return false;
		}
		return this.confirm.equals("Y");
	}

	public boolean matchAuthKey(String authKey) {
		if(authKey == null) {
			return false;
		}
		return authKey.equals(this.authKey);
	}
	
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	
}
