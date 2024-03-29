package com.phcworld.user.controller;

import java.util.List;

import com.phcworld.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phcworld.domain.timeline.Timeline;
import com.phcworld.service.timeline.TimelineServiceImpl;
import com.phcworld.user.service.UserServiceImpl;

@RestController
@RequiredArgsConstructor
public class UserRestController {
	
	private final UserServiceImpl userService;
	
	private final TimelineServiceImpl timelineService;

	@RequestMapping("/users/{id}/timelineList")
	public List<Timeline> listTimeline(@PathVariable Long id, @RequestParam(defaultValue = "1") Integer timelinePageNum) {
		UserEntity user = userService.findUserById(id);
		return timelineService.findTimelineList(timelinePageNum, user);
	}
}
