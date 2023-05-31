package com.phcworld.web.answer;

import javax.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.phcworld.domain.api.model.request.FreeBoardAnswerRequest;
import com.phcworld.domain.api.model.response.FreeBoardAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.exception.ContentsEmptyException;
import com.phcworld.domain.exception.LoginNotUserException;
import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.user.User;
import com.phcworld.service.answer.FreeBoardAnswerServiceImpl;
import com.phcworld.utils.HttpSessionUtils;


@RestController
@RequestMapping("/freeboards/{freeboardId}/answers")
@RequiredArgsConstructor
public class FreeBoardAnswerController {
	
	private final FreeBoardAnswerServiceImpl freeBoardAnswerService;
	
	@PostMapping("")
	@ResponseStatus(value = HttpStatus.CREATED)
	public FreeBoardAnswerApiResponse create(@PathVariable Long freeboardId, FreeBoardAnswerRequest request, HttpSession session) 
			throws LoginNotUserException, ContentsEmptyException {
		if(request.getContents().equals("")) {
			throw new ContentsEmptyException("내용을 입력하세요.");
		}
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		
		return freeBoardAnswerService.create(loginUser, freeboardId, request);
	}
	
	@GetMapping("/{id}")
	public FreeBoardAnswerApiResponse read(@PathVariable Long id, HttpSession session) 
			throws LoginNotUserException, MatchNotUserExceptioin {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		return freeBoardAnswerService.read(id, loginUser);
	}
	
	@PatchMapping("")
	public FreeBoardAnswerApiResponse update(FreeBoardAnswerRequest request, HttpSession session) 
			throws LoginNotUserException, MatchNotUserExceptioin {
		if(request.getContents().equals("")) {
			throw new ContentsEmptyException("내용을 입력하세요.");
		}
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		return freeBoardAnswerService.update(request, loginUser);
	}
	
	@DeleteMapping("/{id}")
	public SuccessResponse delete(@PathVariable Long id, HttpSession session) 
			throws LoginNotUserException, MatchNotUserExceptioin {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		
		return freeBoardAnswerService.delete(id, loginUser);
	}

}
