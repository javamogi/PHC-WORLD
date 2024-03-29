package com.phcworld.web.answer;

import javax.servlet.http.HttpSession;

import com.phcworld.exception.model.BadRequestException;
import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
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
import com.phcworld.domain.exception.LoginNotUserException;
import com.phcworld.domain.exception.MatchNotUserException;
import com.phcworld.service.answer.FreeBoardAnswerServiceImpl;
import com.phcworld.utils.HttpSessionUtils;


@RestController
@RequestMapping("/freeboards/{freeboardId}/answers")
@RequiredArgsConstructor
public class FreeBoardAnswerController {
	
	private final FreeBoardAnswerServiceImpl freeBoardAnswerService;
	
	@PostMapping("")
	@ResponseStatus(value = HttpStatus.CREATED)
	public FreeBoardAnswerApiResponse create(@PathVariable Long freeboardId, FreeBoardAnswerRequest request, HttpSession session) {
		if(request.isContentsEmpty()) {
			throw new BadRequestException();
		}
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new NotMatchUserException();
		}
		UserEntity loginUser = HttpSessionUtils.getUserFromSession(session);
		
		return freeBoardAnswerService.create(loginUser, freeboardId, request);
	}
	
	@GetMapping("/{id}")
	public FreeBoardAnswerApiResponse read(@PathVariable Long id, HttpSession session) 
			throws LoginNotUserException, MatchNotUserException {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new NotMatchUserException();
		}
		UserEntity loginUser = HttpSessionUtils.getUserFromSession(session);
		return freeBoardAnswerService.read(id, loginUser);
	}
	
	@PatchMapping("")
	public FreeBoardAnswerApiResponse update(FreeBoardAnswerRequest request, HttpSession session) 
			throws LoginNotUserException, MatchNotUserException {
		if(request.isContentsEmpty()) {
			throw new BadRequestException();
		}
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new NotMatchUserException();
		}
		UserEntity loginUser = HttpSessionUtils.getUserFromSession(session);
		return freeBoardAnswerService.update(request, loginUser);
	}
	
	@DeleteMapping("/{id}")
	public SuccessResponse delete(@PathVariable Long id, HttpSession session) 
			throws LoginNotUserException, MatchNotUserException {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new NotMatchUserException();
		}
		UserEntity loginUser = HttpSessionUtils.getUserFromSession(session);
		
		return freeBoardAnswerService.delete(id, loginUser);
	}

}
