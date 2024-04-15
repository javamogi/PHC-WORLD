package com.phcworld.answer.service;

import java.util.List;

import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.answer.infrastructure.FreeBoardAnswerRepository;
import com.phcworld.answer.service.port.FreeBoardAnswerService;
import com.phcworld.domain.common.SaveType;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.answer.controller.port.FreeBoardAnswerResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.ifs.CrudInterface;
import com.phcworld.answer.infrastructure.FreeBoardAnswerJpaRepository;
import com.phcworld.freeboard.infrastructure.FreeBoardJpaRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
@Transactional
@RequiredArgsConstructor
public class FreeBoardAnswerServiceImpl implements FreeBoardAnswerService {
	
	private final FreeBoardJpaRepository freeBoardRepository;

	private final FreeBoardAnswerJpaRepository freeBoardAnswerRepository;
	
	private final AlertServiceImpl alertService;
	
	private final TimelineServiceImpl timelineService;
	
	public FreeBoardAnswerResponse create(UserEntity loginUser, Long freeboardId, FreeBoardAnswerRequest request) {
		FreeBoardEntity freeBoard = freeBoardRepository.findById(freeboardId)
				.orElseThrow(NotFoundException::new);
		FreeBoardAnswerEntity freeBoardAnswer = FreeBoardAnswerEntity.builder()
				.writer(loginUser)
				.freeBoard(freeBoard)
				.contents(request.getContents().replace("\r\n", "<br>"))
				.build();
		
		FreeBoardAnswerEntity createdFreeBoardAnswer = freeBoardAnswerRepository.save(freeBoardAnswer);
		
		timelineService.createTimeline(createdFreeBoardAnswer);
		
		if(!freeBoard.matchUser(loginUser)) {
			alertService.createAlert(createdFreeBoardAnswer);
		}
		
		return FreeBoardAnswerResponse.of(createdFreeBoardAnswer);
	}
	
	public FreeBoardAnswerResponse read(Long id, UserEntity loginUser) {
		FreeBoardAnswerEntity freeBoardAnswer = freeBoardAnswerRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		if(!freeBoardAnswer.isSameWriter(loginUser)) {
			throw new NotMatchUserException();
		}
		return FreeBoardAnswerResponse.of(freeBoardAnswer);
	}
	
	public FreeBoardAnswerResponse update(FreeBoardAnswerRequest request, UserEntity loginUser) {
		FreeBoardAnswerEntity answer = freeBoardAnswerRepository.findById(request.getId())
				.orElseThrow(NotFoundException::new);
		if(!answer.isSameWriter(loginUser)) {
			throw new NotMatchUserException();
		}
		answer.update(request.getContents().replace("\r\n", "<br>"));
		FreeBoardAnswerEntity updatedFreeBoardAnswer = freeBoardAnswerRepository.save(answer);
		return FreeBoardAnswerResponse.of(updatedFreeBoardAnswer);
	}
	
	public SuccessResponse delete(Long id, UserEntity loginUser) {
		FreeBoardAnswerEntity freeBoardAnswer = freeBoardAnswerRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		if(!freeBoardAnswer.isSameWriter(loginUser)) {
			throw new NotMatchUserException();
		}

		timelineService.deleteTimeline(SaveType.FREE_BOARD_ANSWER, id);
		alertService.deleteAlert(SaveType.FREE_BOARD_ANSWER, id);
		freeBoardAnswerRepository.deleteById(id);
		
		return SuccessResponse.builder()
				.success("삭제성공")
				.build();
	}
	
	public List<FreeBoardAnswerEntity> findFreeBoardAnswerListByWriter(UserEntity loginUser) {
		return freeBoardAnswerRepository.findByWriter(loginUser);
	}
}
