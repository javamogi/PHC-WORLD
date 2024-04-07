package com.phcworld.service.answer;

import java.util.List;

import com.phcworld.domain.common.SaveType;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.api.model.request.FreeBoardAnswerRequest;
import com.phcworld.domain.api.model.response.FreeBoardAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.ifs.CrudInterface;
import com.phcworld.repository.answer.FreeBoardAnswerRepository;
import com.phcworld.repository.board.FreeBoardRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
@Transactional
@RequiredArgsConstructor
public class FreeBoardAnswerServiceImpl implements CrudInterface<FreeBoardAnswerRequest, FreeBoardAnswerApiResponse, SuccessResponse> {
	
	private static final Logger log = LoggerFactory.getLogger(FreeBoardAnswerServiceImpl.class);
	
	private final FreeBoardRepository freeBoardRepository;

	private final FreeBoardAnswerRepository freeBoardAnswerRepository;
	
	private final AlertServiceImpl alertService;
	
	private final TimelineServiceImpl timelineService;
	
	@Override
	public FreeBoardAnswerApiResponse create(UserEntity loginUser, Long freeboardId, FreeBoardAnswerRequest request) {
		FreeBoard freeBoard = freeBoardRepository.findById(freeboardId)
				.orElseThrow(NotFoundException::new);
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.writer(loginUser)
				.freeBoard(freeBoard)
				.contents(request.getContents().replace("\r\n", "<br>"))
				.build();
		
		FreeBoardAnswer createdFreeBoardAnswer = freeBoardAnswerRepository.save(freeBoardAnswer);
		
		timelineService.createTimeline(createdFreeBoardAnswer);
		
		if(!freeBoard.matchUser(loginUser)) {
			alertService.createAlert(createdFreeBoardAnswer);
		}
		
		return FreeBoardAnswerApiResponse.of(createdFreeBoardAnswer);
	}
	
	@Override
	public FreeBoardAnswerApiResponse read(Long id, UserEntity loginUser) {
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		if(!freeBoardAnswer.isSameWriter(loginUser)) {
			throw new NotMatchUserException();
		}
		return FreeBoardAnswerApiResponse.of(freeBoardAnswer);
	}
	
	@Override
	public FreeBoardAnswerApiResponse update(FreeBoardAnswerRequest request, UserEntity loginUser) {
		FreeBoardAnswer answer = freeBoardAnswerRepository.findById(request.getId())
				.orElseThrow(NotFoundException::new);
		if(!answer.isSameWriter(loginUser)) {
			throw new NotMatchUserException();
		}
		answer.update(request.getContents().replace("\r\n", "<br>"));
		FreeBoardAnswer updatedFreeBoardAnswer = freeBoardAnswerRepository.save(answer);
		return FreeBoardAnswerApiResponse.of(updatedFreeBoardAnswer);
	}
	
	@Override
	public SuccessResponse delete(Long id, UserEntity loginUser) {
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerRepository.findById(id)
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
	
	public List<FreeBoardAnswer> findFreeBoardAnswerListByWriter(UserEntity loginUser) {
		return freeBoardAnswerRepository.findByWriter(loginUser);
	}
}
