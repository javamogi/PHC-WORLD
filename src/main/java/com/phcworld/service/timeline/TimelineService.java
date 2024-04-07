package com.phcworld.service.timeline;

import java.util.List;

import com.phcworld.domain.timeline.Timeline;
import com.phcworld.user.infrastructure.UserEntity;

public interface TimelineService {
	List<Timeline> findTimelineList(Integer timelinePageNum, UserEntity user);
	
	Timeline getOneTimeline(Long id);
}
