package com.phcworld.domain.user;

import java.util.List;

import com.phcworld.domain.timeline.Timeline;

import com.phcworld.domain.timeline.dto.TimelineResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashBoardUser {
	
	private User user;
	
	private Long countOfAnswer;
	
	private Long countOfFreeBoard;
	
	private Long countOfDiary;
	
	private Long countOfAlert;
	
//	private List<Timeline> timelineList;

	private List<TimelineResponseDto> timelineList;
}
