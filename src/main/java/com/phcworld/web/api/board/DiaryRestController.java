package com.phcworld.web.api.board;

import javax.servlet.http.HttpSession;

import com.phcworld.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.exception.LoginNotUserException;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.utils.HttpSessionUtils;


@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryRestController {
	
	private final DiaryServiceImpl diaryService;
	
	@PutMapping("/{diaryId}/good")
	public SuccessResponse updateGoodCount(@PathVariable Long diaryId, HttpSession session) 
			throws LoginNotUserException {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		return diaryService.updateGood(diaryId, loginUser);
	}
}
