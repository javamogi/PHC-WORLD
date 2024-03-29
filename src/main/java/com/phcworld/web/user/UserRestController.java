package com.phcworld.web.user;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;
import com.phcworld.service.timeline.TimelineServiceImpl;
import com.phcworld.service.user.UserService;

@RestController
@RequiredArgsConstructor
public class UserRestController {
	
	private final UserService userService;
	
	private final TimelineServiceImpl timelineService;

	@RequestMapping("/users/{id}/timelineList")
	public List<Timeline> listTimeline(@PathVariable Long id, @RequestParam(defaultValue = "1") Integer timelinePageNum) {
		User user = userService.findUserById(id);
		return timelineService.findTimelineList(timelinePageNum, user);
	}
}
