package com.phcworld.service.timeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.parent.BasicBoardAndAnswer;
import com.phcworld.domain.timeline.TempTimeline;
import com.phcworld.domain.timeline.TempTimelineFactory;
import com.phcworld.domain.user.User;
import com.phcworld.repository.timeline.TempTimelineRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class TempTimelineServiceImpl {

	@Autowired
	private TempTimelineRepository tempTimelineRepository;

	@Autowired
	private TempTimelineFactory factory;

//	@Override
//	public List<Timeline> findTimelineList(Integer timelinePageNum, User user) {
//		PageRequest pageRequest = PageRequest.of(timelinePageNum, 5, new Sort(Direction.DESC, "id"));
//		Page<Timeline> timelineList = timelineRepository.findByUser(user, pageRequest);
//		return timelineList.getContent();
//	}

//	@Override
//	public Timeline getOneTimeline(Long id) {
//		return timelineRepository.getOne(id);
//	}

	public TempTimeline createTimeline(String type, BasicBoardAndAnswer board, Long id) {
		TempTimeline timeline = factory.createTimeline(type, board, id);
		return tempTimelineRepository.save(timeline);
	}
	
	public void deleteTimeline(BasicBoardAndAnswer board) {
		TempTimeline timeline = tempTimelineRepository.findByBoard(board);
		tempTimelineRepository.delete(timeline);
	}

//	public void deleteTimeline(Good good) {
//		Timeline timeline = timelineRepository.findByGood(good);
//		timelineRepository.delete(timeline);
//	}

}
