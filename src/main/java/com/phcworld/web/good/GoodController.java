package com.phcworld.web.good;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phcworld.domain.exception.LoginNotUserException;
import com.phcworld.domain.good.GoodServiceImpl;
import com.phcworld.domain.user.User;
import com.phcworld.web.HttpSessionUtils;

@RestController
@RequestMapping("/good/{diaryId}")
public class GoodController {
	
	@Autowired
	private GoodServiceImpl goodService;
	
	@GetMapping("")
	public String upToGood(@PathVariable Long diaryId, HttpSession session) throws LoginNotUserException {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		
		return "{\"success\":\"" + goodService.upGood(diaryId, loginUser) +"\"}";
	}
}
