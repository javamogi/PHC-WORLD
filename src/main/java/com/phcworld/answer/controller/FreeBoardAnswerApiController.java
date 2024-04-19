package com.phcworld.answer.controller;

import com.phcworld.answer.controller.port.FreeBoardAnswerPageResponse;
import com.phcworld.answer.controller.port.FreeBoardAnswerResponse;
import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.answer.domain.dto.FreeBoardAnswerUpdateRequest;
import com.phcworld.answer.service.port.FreeBoardAnswerService;
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
@RequestMapping("/freeboards/{freeboardId}/answers")
@RequiredArgsConstructor
@Builder
public class FreeBoardAnswerApiController {
	
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
	
	@PatchMapping("")
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
	
	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse> delete(@PathVariable Long id, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new EmptyLoginUserException();
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		return ResponseEntity
				.status(200)
				.body(freeBoardAnswerService.delete(id, loginUser));
	}

	@GetMapping("")
//	public ResponseEntity<List<FreeBoardAnswerResponse>> getList(@PathVariable Long freeboardId,
	public ResponseEntity<FreeBoardAnswerPageResponse> getList(@PathVariable Long freeboardId,
																 @RequestParam(defaultValue = "1") int pageNum){
//		List<FreeBoardAnswer> freeBoardAnswer = freeBoardAnswerService.getListByFreeBoard(freeboardId, pageNum);
//		return ResponseEntity
//				.status(200)
//				.body(freeBoardAnswer
//						.stream()
//						.map(FreeBoardAnswerResponse::from)
//						.collect(Collectors.toList()));
		Page<FreeBoardAnswer> freeBoardAnswer = freeBoardAnswerService.getListByFreeBoard(freeboardId, pageNum);
		FreeBoardAnswerPageResponse response = FreeBoardAnswerPageResponse.from(freeBoardAnswer);
		return ResponseEntity
				.status(200)
				.body(response);
	}
}
