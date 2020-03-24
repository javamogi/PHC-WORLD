package com.phcworld.web.api.board;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.exception.LoginNotUserException;
import com.phcworld.domain.user.User;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.utils.HttpSessionUtils;


@RestController
@RequestMapping("/api/diary")
public class DiaryRestController {
	
	@Autowired
	private DiaryServiceImpl diaryService;
	
	@PutMapping("/{diaryId}/good")
	public SuccessResponse updateGoodCount(@PathVariable Long diaryId, HttpSession session) 
			throws LoginNotUserException {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		return diaryService.updateGood(diaryId, loginUser);
	}
}
