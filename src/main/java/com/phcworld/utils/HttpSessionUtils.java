package com.phcworld.utils;

import javax.servlet.http.HttpSession;

import com.phcworld.user.controller.port.SessionUser;
import com.phcworld.user.domain.User;
import com.phcworld.user.infrastructure.UserEntity;

public class HttpSessionUtils {

	public static final String USER_SESSION_KEY = "sessionedUser";
	
	public static boolean isLoginUser(HttpSession session) {
		Object sessionUser = session.getAttribute(USER_SESSION_KEY);
		if(sessionUser == null) {
			return false;
		}
		return true;
	}
	
	public static UserEntity getUserEntityFromSession(HttpSession session) {
		if(!isLoginUser(session)) {
			return null;
		}
		return (UserEntity)session.getAttribute(USER_SESSION_KEY);
	}

	public static SessionUser getUserFromSession(HttpSession session) {
		if(!isLoginUser(session)) {
			return null;
		}
		return (SessionUser)session.getAttribute(USER_SESSION_KEY);
	}
}
