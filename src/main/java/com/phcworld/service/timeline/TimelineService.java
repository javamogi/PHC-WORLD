package com.phcworld.service.timeline;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;

public interface TimelineService {
	Page<Timeline> findPageTimelineByUser(User user);
	
	List<Timeline> findTimelineList(Integer timelinePageNum, User user);
	
	Timeline getOneTimeline(Long id);
}
