package com.phcworld.service.answer;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.FreeBoardAnswer;
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
	public FreeBoardAnswer createFreeBoardAnswer(User loginUser, Long freeboardId, String contents) {
		FreeBoard freeBoard = freeBoardRepository.getOne(freeboardId);
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.writer(loginUser)
				.freeBoard(freeBoard)
				.contents(contents.replace("\r\n", "<br>"))
				.createDate(LocalDateTime.now())
				.build();
		
		FreeBoardAnswer createdFreeBoardAnswer = freeBoardAnswerRepository.save(freeBoardAnswer);
		timelineService.createTimeline(createdFreeBoardAnswer);
		
		if(!freeBoard.matchUser(loginUser)) {
			alertService.createAlert(createdFreeBoardAnswer);
		}
		
		return createdFreeBoardAnswer;
	}
	
	@Override
	public String deleteFreeBoardAnswer(Long id, User loginUser) {
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerRepository.getOne(id);
		if(!freeBoardAnswer.isSameWriter(loginUser)) {
			throw new MatchNotUserExceptioin("본인이 작성한 글만 삭제 가능합니다.");
		}

		timelineService.deleteTimeline(freeBoardAnswer);
		if(!freeBoardAnswer.isSameWriter(loginUser)) {
			alertService.deleteAlert(freeBoardAnswer);
		}
		freeBoardAnswerRepository.deleteById(id);
		
		FreeBoard freeBoard = freeBoardRepository.getOne(freeBoardAnswer.getFreeBoard().getId());
		freeBoard.getFreeBoardAnswers().remove(freeBoardAnswer);
		log.info("countOfAnswer : {}", freeBoard.getCountOfAnswer());
		return "{\"success\":\"" + freeBoard.getCountOfAnswer() +"\"}";
	}
	
	@Override
	public List<FreeBoardAnswer> findFreeBoardAnswerListByWriter(User loginUser) {
		return freeBoardAnswerRepository.findByWriter(loginUser);
	}

	public FreeBoardAnswer getOneFreeBoardAnswer(Long id) {
		return freeBoardAnswerRepository.getOne(id);
	}
}
