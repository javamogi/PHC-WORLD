package com.phcworld.answer.controller;

import com.phcworld.answer.controller.port.FreeBoardAnswerPageResponse;
import com.phcworld.answer.controller.port.FreeBoardAnswerResponse;
import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.answer.domain.dto.FreeBoardAnswerUpdateRequest;
import com.phcworld.answer.controller.port.FreeBoardAnswerService;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.exception.model.EmptyContentsException;
import com.phcworld.exception.model.EmptyLoginUserException;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.utils.HttpSessionUtils;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@Builder
public class FreeBoardAnswerApiController {
	
	private final FreeBoardAnswerService freeBoardAnswerService;

	@PostMapping("/freeboards/{freeboardId}/answers")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<FreeBoardAnswerResponse> create(@PathVariable Long freeboardId,
														  FreeBoardAnswerRequest request,
														  HttpSession session) {
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
	
	@GetMapping("/freeboards/{freeboardId}/answers/{id}")
	public ResponseEntity<FreeBoardAnswerResponse> read(@PathVariable Long id, HttpSession session){
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new EmptyLoginUserException();
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerService.getById(id, loginUser);
		return ResponseEntity
				.status(200)
				.body(FreeBoardAnswerResponse.from(freeBoardAnswer));
	}
	
	@PatchMapping("/freeboards/{freeboardId}/answers")
	public ResponseEntity<FreeBoardAnswerResponse> update(FreeBoardAnswerUpdateRequest request, HttpSession session){
		if(request.isContentsEmpty()) {
			throw new EmptyContentsException();
		}
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new EmptyLoginUserException();
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerService.update(request, loginUser);
		return ResponseEntity
				.status(200)
				.body(FreeBoardAnswerResponse.from(freeBoardAnswer));
	}
	
	@DeleteMapping("/freeboards/{freeboardId}/answers/{id}")
	public ResponseEntity<SuccessResponse> delete(@PathVariable Long id, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new EmptyLoginUserException();
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		return ResponseEntity
				.status(200)
				.body(freeBoardAnswerService.delete(id, loginUser));
	}

	@GetMapping("/freeboards/{freeboardId}/answers")
	public ResponseEntity<FreeBoardAnswerPageResponse> getList(@PathVariable Long freeboardId,
																 @RequestParam(defaultValue = "1") int pageNum){
		Page<FreeBoardAnswer> freeBoardAnswer = freeBoardAnswerService.getListByFreeBoard(freeboardId, pageNum);
		FreeBoardAnswerPageResponse response = FreeBoardAnswerPageResponse.from(freeBoardAnswer);
		return ResponseEntity
				.status(200)
				.body(response);
	}

	@GetMapping("/answers/users/{userId}")
	public ResponseEntity<List<FreeBoardAnswerResponse>> getListByUser(@PathVariable Long userId,
											  @RequestParam(required = false) Long answerId){
		List<FreeBoardAnswerResponse> list = freeBoardAnswerService.getListByUser(userId, answerId)
				.stream()
				.map(FreeBoardAnswerResponse::from)
				.collect(Collectors.toList());
		return ResponseEntity
				.status(200)
				.body(list);
	}
}
