package com.phcworld.service.timeline;

import java.util.List;

import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;

public interface TimelineService {
	List<Timeline> findTimelineList(Integer timelinePageNum, User user);
	
	Timeline getOneTimeline(Long id);
}
