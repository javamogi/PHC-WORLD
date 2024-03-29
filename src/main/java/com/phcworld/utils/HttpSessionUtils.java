package com.phcworld.utils;

import javax.servlet.http.HttpSession;

import com.phcworld.user.infrastructure.UserEntity;

public class HttpSessionUtils {

	public static final String USER_SESSION_KEY = "sessionedUser";
	
	public static boolean isLoginUser(HttpSession session) {
		Object sessionedUser = session.getAttribute(USER_SESSION_KEY);
		if(sessionedUser == null) {
			return false;
		}
		return true;
	}
	
	public static UserEntity getUserFromSession(HttpSession session) {
		if(!isLoginUser(session)) {
			return null;
		}
		return (UserEntity)session.getAttribute(USER_SESSION_KEY);
	}
}
