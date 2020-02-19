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

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.answer.FreeBoardAnswerServiceImpl;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.exception.LoginNotUserException;
import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.user.User;
import com.phcworld.service.board.FreeBoardServiceImpl;
import com.phcworld.web.HttpSessionUtils;

@RestController
@RequestMapping("/freeboard/{freeboardId}/answer")
public class FreeBoardAnswerController {
	
	@Autowired
	private FreeBoardServiceImpl freeBoardService;
	
	@Autowired
	private FreeBoardAnswerServiceImpl freeBoardAnswerService;
	
	@PostMapping("")
	@ResponseStatus(value = HttpStatus.CREATED)
	public FreeBoardAnswer create(@PathVariable Long freeboardId, String contents, HttpSession session) throws LoginNotUserException {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		
		FreeBoard freeBoard = freeBoardService.addFreeBoardAnswer(freeboardId);
		return freeBoardAnswerService.createFreeBoardAnswer(loginUser, freeBoard, contents);
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long freeboardId, @PathVariable Long id, HttpSession session, Model model) throws LoginNotUserException, MatchNotUserExceptioin {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		
		return freeBoardAnswerService.deleteFreeBoardAnswer(id, loginUser, freeboardId);
	}

}
