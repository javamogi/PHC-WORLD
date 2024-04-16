package com.phcworld.answer.service;

import java.util.List;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.domain.dto.FreeBoardAnswerUpdateRequest;
import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.answer.infrastructure.FreeBoardAnswerRepository;
import com.phcworld.answer.service.port.FreeBoardAnswerService;
import com.phcworld.common.infrastructure.LocalDateTimeHolder;
import com.phcworld.domain.common.SaveType;
import com.phcworld.exception.model.AnswerNotFoundException;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.service.port.FreeBoardRepository;
import com.phcworld.user.domain.User;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.answer.controller.port.FreeBoardAnswerResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.answer.infrastructure.FreeBoardAnswerJpaRepository;
import com.phcworld.freeboard.infrastructure.FreeBoardJpaRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Builder
public class FreeBoardAnswerServiceImpl implements FreeBoardAnswerService {
	
	private final FreeBoardJpaRepository freeBoardRepositoryImpl;
	private final FreeBoardAnswerJpaRepository freeBoardAnswerRepositoryImpl;

	private final FreeBoardRepository freeBoardRepository;
	private final FreeBoardAnswerRepository freeBoardAnswerRepository;
	private final LocalDateTimeHolder localDateTimeHolder;

//	private final AlertServiceImpl alertService;
//
//	private final TimelineServiceImpl timelineService;
	
	public FreeBoardAnswerResponse create(UserEntity loginUser, Long freeboardId, FreeBoardAnswerRequest request) {
		FreeBoardEntity freeBoard = freeBoardRepositoryImpl.findById(freeboardId)
				.orElseThrow(NotFoundException::new);
		FreeBoardAnswerEntity freeBoardAnswer = FreeBoardAnswerEntity.builder()
				.writer(loginUser)
				.freeBoard(freeBoard)
				.contents(request.getContents().replace("\r\n", "<br>"))
				.build();
		
		FreeBoardAnswerEntity createdFreeBoardAnswer = freeBoardAnswerRepositoryImpl.save(freeBoardAnswer);
		
//		timelineService.createTimeline(createdFreeBoardAnswer);
		
//		if(!freeBoard.matchUser(loginUser)) {
//			alertService.createAlert(createdFreeBoardAnswer);
//		}
		
		return FreeBoardAnswerResponse.of(createdFreeBoardAnswer);
	}

	@Transactional
	public FreeBoardAnswerResponse read(Long id, UserEntity loginUser) {
		FreeBoardAnswerEntity freeBoardAnswer = freeBoardAnswerRepositoryImpl.findById(id)
				.orElseThrow(NotFoundException::new);
		if(!freeBoardAnswer.isSameWriter(loginUser)) {
			throw new NotMatchUserException();
		}
		return FreeBoardAnswerResponse.of(freeBoardAnswer);
	}

	@Transactional
	public FreeBoardAnswerResponse update(FreeBoardAnswerRequest request, UserEntity loginUser) {
		FreeBoardAnswerEntity answer = freeBoardAnswerRepositoryImpl.findById(request.getId())
				.orElseThrow(NotFoundException::new);
		if(!answer.isSameWriter(loginUser)) {
			throw new NotMatchUserException();
		}
		answer.update(request.getContents().replace("\r\n", "<br>"));
		FreeBoardAnswerEntity updatedFreeBoardAnswer = freeBoardAnswerRepositoryImpl.save(answer);
		return FreeBoardAnswerResponse.of(updatedFreeBoardAnswer);
	}

	@Transactional
	public SuccessResponse delete(Long id, UserEntity loginUser) {
		FreeBoardAnswerEntity freeBoardAnswer = freeBoardAnswerRepositoryImpl.findById(id)
				.orElseThrow(NotFoundException::new);
		if(!freeBoardAnswer.isSameWriter(loginUser)) {
			throw new NotMatchUserException();
		}

//		timelineService.deleteTimeline(SaveType.FREE_BOARD_ANSWER, id);
//		alertService.deleteAlert(SaveType.FREE_BOARD_ANSWER, id);
		freeBoardAnswerRepositoryImpl.deleteById(id);
		
		return SuccessResponse.builder()
				.success("삭제성공")
				.build();
	}
	
	public List<FreeBoardAnswerEntity> findFreeBoardAnswerListByWriter(UserEntity loginUser) {
		return freeBoardAnswerRepositoryImpl.findByWriter(loginUser);
	}

	@Transactional
	@Override
	public FreeBoardAnswer register(long freeBoardId, User user, FreeBoardAnswerRequest request) {
		FreeBoard freeBoard = freeBoardRepository.findById(freeBoardId);
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.from(freeBoard, user, request, localDateTimeHolder);
		FreeBoardAnswer savedFreeBoardAnswer = freeBoardAnswerRepository.save(freeBoardAnswer);

//		timelineService.createTimeline(savedFreeBoardAnswer);
//
//		if(!freeBoard.matchWriter(UserEntity.from(user))) {
//			alertService.createAlert(savedFreeBoardAnswer);
//		}

		return savedFreeBoardAnswer;
	}

	@Override
	public FreeBoardAnswer getById(long id, UserEntity loginUser) {
		FreeBoardAnswer answer = freeBoardAnswerRepository.findById(id)
				.orElseThrow(AnswerNotFoundException::new);
		if(!answer.matchWriter(loginUser)) {
			throw new NotMatchUserException();
		}
		return answer;
	}

	@Override
	public FreeBoardAnswer update(FreeBoardAnswerUpdateRequest request, UserEntity loginUser) {
		FreeBoardAnswer answer = freeBoardAnswerRepository.findById(request.getId())
				.orElseThrow(AnswerNotFoundException::new);
		if(!answer.matchWriter(loginUser)) {
			throw new NotMatchUserException();
		}
		answer = answer.update(request, localDateTimeHolder);
		return freeBoardAnswerRepository.save(answer);
	}
}
