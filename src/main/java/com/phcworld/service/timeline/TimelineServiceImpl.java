package com.phcworld.service.timeline;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;
import com.phcworld.repository.timeline.TimelineRepository;

@Service
@Transactional
public class TimelineServiceImpl implements TimelineService {
	
	@Autowired
	private TimelineRepository timelineRepository;

	@Override
	public Page<Timeline> findPageTimelineByUser(User user) {
		PageRequest pageRequest = PageRequest.of(0, 5, new Sort(Direction.DESC, "id"));
		return timelineRepository.findByUser(user, pageRequest);
	}

	@Override
	public List<Timeline> findTimelineList(Integer timelinePageNum, User user) {
		PageRequest pageRequest = PageRequest.of(timelinePageNum, 5, new Sort(Direction.DESC, "id"));
		Page<Timeline> timelineList = timelineRepository.findByUser(user, pageRequest);
		return timelineList.getContent();
	}
	
	@Override
	public Timeline getOneTimeline(Long id) {
		return timelineRepository.getOne(id);
	}
	
	public Timeline createTimeline(Timeline timeline) {
		return timelineRepository.save(timeline);
	}
}
