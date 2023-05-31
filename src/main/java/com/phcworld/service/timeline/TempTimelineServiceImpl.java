package com.phcworld.service.timeline;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.good.TempGood;
import com.phcworld.domain.parent.BasicBoardAndAnswer;
import com.phcworld.domain.timeline.TempTimeline;
import com.phcworld.domain.timeline.TempTimelineFactory;
import com.phcworld.repository.timeline.TempTimelineRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TempTimelineServiceImpl {

	private TempTimelineRepository tempTimelineRepository;

	private TempTimelineFactory factory;

//	@Override
//	public List<Timeline> findTimelineList(Integer timelinePageNum, User user) {
//		PageRequest pageRequest = PageRequest.of(timelinePageNum, 5, new Sort(Direction.DESC, "id"));
//		Page<Timeline> timelineList = timelineRepository.findByUser(user, pageRequest);
//		return timelineList.getContent();
//	}

	public TempTimeline createTimeline(String type, BasicBoardAndAnswer board, Long id) {
		TempTimeline timeline = factory.createTimeline(type, board, id);
		return tempTimelineRepository.save(timeline);
	}
	
	public TempTimeline createTimeline(String type, TempGood good, Long id) {
		TempTimeline timeline = factory.createTimeline(type, good, id);
		return tempTimelineRepository.save(timeline);
	}
	
	public void deleteTimeline(BasicBoardAndAnswer board) {
		TempTimeline timeline = tempTimelineRepository.findByBoard(board);
		tempTimelineRepository.delete(timeline);
	}
	
	public void deleteTimeline(TempGood good) {
		TempTimeline timeline = tempTimelineRepository.findByGood(good);
		tempTimelineRepository.delete(timeline);
	}

}
