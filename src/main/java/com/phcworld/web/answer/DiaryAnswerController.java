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

import com.phcworld.domain.api.model.request.DiaryAnswerRequest;
import com.phcworld.domain.api.model.response.DiaryAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.exception.ContentsEmptyException;
import com.phcworld.domain.exception.LoginNotUserException;
import com.phcworld.domain.exception.MatchNotUserException;
import com.phcworld.service.answer.DiaryAnswerServiceImpl;
import com.phcworld.utils.HttpSessionUtils;

@RestController
@RequestMapping("/diaries/{diaryId}/answer")
@RequiredArgsConstructor
public class DiaryAnswerController {
	
	private final DiaryAnswerServiceImpl diaryAnswerService;
	
	@PostMapping("")
	@ResponseStatus(value = HttpStatus.CREATED)
	public DiaryAnswerApiResponse create(@PathVariable Long diaryId, DiaryAnswerRequest request, HttpSession session) 
			throws LoginNotUserException, ContentsEmptyException {
		if(request.isContentsEmpty()) {
			throw new BadRequestException();
		}
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new NotMatchUserException();
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		
		return diaryAnswerService.create(loginUser, diaryId, request);
	}
	
	@GetMapping("/{id}")
	public DiaryAnswerApiResponse read(@PathVariable Long id, HttpSession session) 
			throws LoginNotUserException, MatchNotUserException {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new NotMatchUserException();
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		return diaryAnswerService.read(id, loginUser);
	}
	
	@PatchMapping("")
	public DiaryAnswerApiResponse update(DiaryAnswerRequest request, HttpSession session) 
			throws LoginNotUserException, MatchNotUserException {
		if(request.isContentsEmpty()) {
			throw new BadRequestException();
		}
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new NotMatchUserException();
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		return diaryAnswerService.update(request, loginUser);
	}
	
	@DeleteMapping("/{id}")
	public SuccessResponse delete(@PathVariable Long id, HttpSession session)
			throws LoginNotUserException, MatchNotUserException {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new NotMatchUserException();
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		return diaryAnswerService.delete(id, loginUser);
	}
}
