package com.phcworld.service.answer;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.alert.AlertRepository;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.timeline.TimelineRepository;
import com.phcworld.domain.user.User;
import com.phcworld.repository.answer.FreeBoardAnswerRepository;
import com.phcworld.service.board.FreeBoardServiceImpl;

@Service
@Transactional
public class FreeBoardAnswerServiceImpl implements FreeBoardAnswerService {
	
	private static final Logger log = LoggerFactory.getLogger(FreeBoardAnswerServiceImpl.class);
	
	@Autowired
	private FreeBoardAnswerRepository freeBoardAnswerRepository;
	
	@Autowired
	private TimelineRepository timelineRepository;
	
	@Autowired
	private AlertRepository alertRepository;
	
	@Autowired
	private FreeBoardServiceImpl freeBoardService;
	
	@Override
	public FreeBoardAnswer createFreeBoardAnswer(User loginUser, Long freeboardId, String contents) {
		FreeBoard freeBoard = freeBoardService.addFreeBoardAnswer(freeboardId);
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.writer(loginUser)
				.freeBoard(freeBoard)
				.contents(contents.replace("\r\n", "<br>"))
				.createDate(LocalDateTime.now())
				.build();
		freeBoardAnswerRepository.save(freeBoardAnswer);
		
		Timeline timeline = new Timeline("FreeBoard Answer", "comment", freeBoard, freeBoardAnswer, loginUser, freeBoardAnswer.getCreateDate());
		timelineRepository.save(timeline);
		
		freeBoardAnswer.setTimeline(timeline);
		
		if(!freeBoard.matchUser(loginUser)) {
			Alert alert = new Alert("FreeBoard", freeBoardAnswer, freeBoard.getWriter(), freeBoardAnswer.getCreateDate());
			alertRepository.save(alert);
			freeBoardAnswer.setAlert(alert);
		}
		
		return freeBoardAnswerRepository.save(freeBoardAnswer);
	}
	
	@Override
	public String deleteFreeBoardAnswer(Long id, User loginUser) {
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerRepository.getOne(id);
		if(!freeBoardAnswer.isSameWriter(loginUser)) {
			throw new MatchNotUserExceptioin("본인이 작성한 글만 삭제 가능합니다.");
		}
		freeBoardAnswerRepository.deleteById(id);
		
		FreeBoard freeBoard = freeBoardService.deleteAnswer(freeBoardAnswer.getFreeBoard().getId());
		log.debug("countOfAnswer : {}", freeBoard.getCountOfAnswer());
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
