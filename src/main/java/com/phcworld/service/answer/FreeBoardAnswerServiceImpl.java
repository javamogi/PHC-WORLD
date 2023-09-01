package com.phcworld.service.answer;

import java.util.List;

import com.phcworld.exception.model.NotFoundException;
import com.phcworld.exception.model.NotMatchUserException;
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
import com.phcworld.domain.exception.MatchNotUserException;
import com.phcworld.domain.user.User;
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
	public FreeBoardAnswerApiResponse create(User loginUser, Long freeboardId, FreeBoardAnswerRequest request) {
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
	public FreeBoardAnswerApiResponse read(Long id, User loginUser) {
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		if(!freeBoardAnswer.isSameWriter(loginUser)) {
			throw new NotMatchUserException();
		}
		return FreeBoardAnswerApiResponse.of(freeBoardAnswer);
	}
	
	@Override
	public FreeBoardAnswerApiResponse update(FreeBoardAnswerRequest request, User loginUser) {
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
	public SuccessResponse delete(Long id, User loginUser) {
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		if(!freeBoardAnswer.isSameWriter(loginUser)) {
			throw new NotMatchUserException();
		}

		timelineService.deleteTimeline(freeBoardAnswer);
		if(freeBoardAnswer.isSameWriter(loginUser)) {
			alertService.deleteAlert(freeBoardAnswer);
		}
		freeBoardAnswerRepository.deleteById(id);
		
		FreeBoard freeBoard = freeBoardRepository.findById(freeBoardAnswer.getFreeBoard().getId())
				.orElseThrow(NotFoundException::new);
		freeBoard.getFreeBoardAnswers().remove(freeBoardAnswer);
		log.info("countOfAnswer : {}", freeBoard.getCountOfAnswer());
		return SuccessResponse.builder()
				.success(freeBoard.getCountOfAnswer())
				.build();
	}
	
	public List<FreeBoardAnswer> findFreeBoardAnswerListByWriter(User loginUser) {
		return freeBoardAnswerRepository.findByWriter(loginUser);
	}
}
