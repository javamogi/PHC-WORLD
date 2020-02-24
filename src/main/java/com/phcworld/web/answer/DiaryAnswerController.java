package com.phcworld.web.answer;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.exception.LoginNotUserException;
import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.user.User;
import com.phcworld.service.answer.DiaryAnswerServiceImpl;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.web.HttpSessionUtils;

@RestController
@RequestMapping("/diary/{diaryId}/answer")
public class DiaryAnswerController {
	
	@Autowired
	private DiaryServiceImpl diaryService;
	
	@Autowired
	private DiaryAnswerServiceImpl diaryAnswerService;
	
	@PostMapping("")
	@ResponseStatus(value = HttpStatus.CREATED)
	public DiaryAnswer create(@PathVariable Long diaryId, String contents, HttpSession session) throws LoginNotUserException {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		Diary diary = diaryService.getOneDiary(diaryId);
		
		return diaryAnswerService.createDiaryAnswer(loginUser, diary, contents);
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long diaryId, @PathVariable Long id, HttpSession session, Model model) throws LoginNotUserException, MatchNotUserExceptioin {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		return diaryAnswerService.deleteDiaryAnswer(id, loginUser, diaryId);
	}
	
}
