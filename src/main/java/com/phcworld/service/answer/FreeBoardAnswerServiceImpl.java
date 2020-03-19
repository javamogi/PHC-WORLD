package com.phcworld.service.answer;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.api.model.response.FreeBoardAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.user.User;
import com.phcworld.repository.answer.FreeBoardAnswerRepository;
import com.phcworld.repository.board.FreeBoardRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
@Transactional
public class FreeBoardAnswerServiceImpl implements FreeBoardAnswerService {
	
	private static final Logger log = LoggerFactory.getLogger(FreeBoardAnswerServiceImpl.class);
	
	@Autowired
	private FreeBoardRepository freeBoardRepository;

	@Autowired
	private FreeBoardAnswerRepository freeBoardAnswerRepository;
	
	@Autowired
	private AlertServiceImpl alertService;
	
	@Autowired
	private TimelineServiceImpl timelineService;
	
	@Override
	public FreeBoardAnswerApiResponse createFreeBoardAnswer(User loginUser, Long freeboardId, String contents) {
		FreeBoard freeBoard = freeBoardRepository.getOne(freeboardId);
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.writer(loginUser)
				.freeBoard(freeBoard)
				.contents(contents.replace("\r\n", "<br>"))
				.createDate(LocalDateTime.now())
				.build();
		
		FreeBoardAnswer createdFreeBoardAnswer = freeBoardAnswerRepository.save(freeBoardAnswer);
		
		FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = FreeBoardAnswerApiResponse.builder()
				.id(createdFreeBoardAnswer.getId())
				.writer(createdFreeBoardAnswer.getWriter())
				.contents(createdFreeBoardAnswer.getContents())
				.freeBoardId(freeboardId)
				.countOfAnswers(createdFreeBoardAnswer.getFreeBoard().getCountOfAnswer())
				.createDate(createdFreeBoardAnswer.getFormattedCreateDate())
				.build();
		
		timelineService.createTimeline(createdFreeBoardAnswer);
		
		if(!freeBoard.matchUser(loginUser)) {
			alertService.createAlert(createdFreeBoardAnswer);
		}
		
		return freeBoardAnswerApiResponse;
	}
	
	@Override
	public SuccessResponse deleteFreeBoardAnswer(Long id, User loginUser) {
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerRepository.getOne(id);
		if(!freeBoardAnswer.isSameWriter(loginUser)) {
			throw new MatchNotUserExceptioin("본인이 작성한 글만 삭제 가능합니다.");
		}

		timelineService.deleteTimeline(freeBoardAnswer);
		if(freeBoardAnswer.isSameWriter(loginUser)) {
			alertService.deleteAlert(freeBoardAnswer);
		}
		freeBoardAnswerRepository.deleteById(id);
		
		FreeBoard freeBoard = freeBoardRepository.getOne(freeBoardAnswer.getFreeBoard().getId());
		freeBoard.getFreeBoardAnswers().remove(freeBoardAnswer);
		log.info("countOfAnswer : {}", freeBoard.getCountOfAnswer());
		return SuccessResponse.builder()
				.success(freeBoard.getCountOfAnswer())
				.build();
	}
	
	@Override
	public List<FreeBoardAnswer> findFreeBoardAnswerListByWriter(User loginUser) {
		return freeBoardAnswerRepository.findByWriter(loginUser);
	}
	
	public FreeBoardAnswerApiResponse readFreeBoardAnswer(Long id, User loginUser) {
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerRepository.getOne(id);
		if(!freeBoardAnswer.isSameWriter(loginUser)) {
			throw new MatchNotUserExceptioin("본인이 작성한 글만 수정 가능합니다.");
		}
		FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = 
				FreeBoardAnswerApiResponse.builder()
				.id(freeBoardAnswer.getId())
				.writer(loginUser)
				.contents(freeBoardAnswer.getContents().replace("<br>", "\r\n"))
				.freeBoardId(freeBoardAnswer.getFreeBoard().getId())
				.countOfAnswers(freeBoardAnswer.getFreeBoard().getCountOfAnswer())
				.createDate(freeBoardAnswer.getFormattedCreateDate())
				.build();
		return freeBoardAnswerApiResponse;
	}
	
	public FreeBoardAnswerApiResponse updateFreeBoardAnswer(Long id, String contents, User loginUser) {
		FreeBoardAnswer answer = freeBoardAnswerRepository.getOne(id);
		if(!answer.isSameWriter(loginUser)) {
			throw new MatchNotUserExceptioin("본인이 작성한 글만 수정 가능합니다.");
		}
		answer.update(contents.replace("\r\n", "<br>"));
		FreeBoardAnswer updatedFreeBoardAnswer = freeBoardAnswerRepository.save(answer);
		FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = 
				FreeBoardAnswerApiResponse.builder()
				.id(updatedFreeBoardAnswer.getId())
				.writer(updatedFreeBoardAnswer.getWriter())
				.contents(updatedFreeBoardAnswer.getContents())
				.freeBoardId(updatedFreeBoardAnswer.getFreeBoard().getId())
				.countOfAnswers(updatedFreeBoardAnswer.getFreeBoard().getCountOfAnswer())
				.createDate(updatedFreeBoardAnswer.getFormattedCreateDate())
				.build();
		return freeBoardAnswerApiResponse;
	}
}
