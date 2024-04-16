package com.phcworld.answer.controller;

import javax.servlet.http.HttpSession;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.service.port.FreeBoardAnswerService;
import com.phcworld.exception.model.BadRequestException;
import com.phcworld.exception.model.EmptyContentsException;
import com.phcworld.exception.model.EmptyLoginUserException;
import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.answer.controller.port.FreeBoardAnswerResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.exception.LoginNotUserException;
import com.phcworld.domain.exception.MatchNotUserException;
import com.phcworld.answer.service.FreeBoardAnswerServiceImpl;
import com.phcworld.utils.HttpSessionUtils;


@RestController
@RequestMapping("/freeboards/{freeboardId}/answers")
@RequiredArgsConstructor
@Builder
public class FreeBoardAnswerApiController {
	
	private final FreeBoardAnswerServiceImpl freeBoardAnswerServiceImpl;
	private final FreeBoardAnswerService freeBoardAnswerService;

	@PostMapping("")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<FreeBoardAnswerResponse> create(@PathVariable Long freeboardId, FreeBoardAnswerRequest request, HttpSession session) {
		if(request.isContentsEmpty()) {
			throw new EmptyContentsException();
		}
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new EmptyLoginUserException();
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);

		FreeBoardAnswer answer = freeBoardAnswerService.register(freeboardId, loginUser.toModel(), request);
		return ResponseEntity
				.status(201)
				.body(FreeBoardAnswerResponse.from(answer));
	}
	
	@GetMapping("/{id}")
	public FreeBoardAnswerResponse read(@PathVariable Long id, HttpSession session)
			throws LoginNotUserException, MatchNotUserException {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new NotMatchUserException();
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		return freeBoardAnswerServiceImpl.read(id, loginUser);
	}
	
	@PatchMapping("")
	public FreeBoardAnswerResponse update(FreeBoardAnswerRequest request, HttpSession session)
			throws LoginNotUserException, MatchNotUserException {
		if(request.isContentsEmpty()) {
			throw new BadRequestException();
		}
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new NotMatchUserException();
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		return freeBoardAnswerServiceImpl.update(request, loginUser);
	}
	
	@DeleteMapping("/{id}")
	public SuccessResponse delete(@PathVariable Long id, HttpSession session) 
			throws LoginNotUserException, MatchNotUserException {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new NotMatchUserException();
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		
		return freeBoardAnswerServiceImpl.delete(id, loginUser);
	}

}
