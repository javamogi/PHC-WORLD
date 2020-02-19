package com.phcworld.domain.answer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.alert.AlertRepository;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.timeline.TimelineRepository;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.FreeBoardRepository;

@Service
@Transactional
public class FreeBoardAnswerServiceImpl implements FreeBoardAnswerService {
	
	private static final Logger log = LoggerFactory.getLogger(FreeBoardAnswerServiceImpl.class);
	
	@Autowired
	private FreeBoardRepository freeBoardRepository;
	
	@Autowired
	private FreeBoardAnswerRepository freeBoardAnswerRepository;
	
	@Autowired
	private TimelineRepository timelineRepository;
	
	@Autowired
	private AlertRepository alertRepository;
	
	@Override
	public FreeBoardAnswer createFreeBoardAnswer(User loginUser, FreeBoard freeBoard, String contents) {
		FreeBoardAnswer freeBoardAnswer = new FreeBoardAnswer(loginUser, freeBoard, contents.replace("\r\n", "<br>"));
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
	public String deleteFreeBoardAnswer(Long id, User loginUser, Long freeboardId) {
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerRepository.getOne(id);
		if(!freeBoardAnswer.isSameWriter(loginUser)) {
			throw new MatchNotUserExceptioin("본인이 작성한 글만 삭제 가능합니다.");
		}
		freeBoardAnswerRepository.deleteById(id);
		
		FreeBoard freeBoard = freeBoardRepository.getOne(freeboardId);
		freeBoard.deleteAnswer();
		freeBoard = freeBoardRepository.save(freeBoard);
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
