package com.phcworld.user.controller.port;

import com.phcworld.api.dashboard.dto.UserResponseDto;
import com.phcworld.domain.timeline.dto.TimelineResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashBoardUser {
	
	private UserResponseDto user;
	
	private Long countOfAnswer;
	
	private Long countOfFreeBoard;
	
	private Long countOfDiary;
	
	private Long countOfAlert;
	
//	private List<Timeline> timelineList;

	private List<TimelineResponseDto> timelineList;
}
