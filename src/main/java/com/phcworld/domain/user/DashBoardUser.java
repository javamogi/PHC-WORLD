package com.phcworld.domain.user;

import java.util.List;

import com.phcworld.domain.timeline.Timeline;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashBoardUser {
	
	private User user;
	
	private Integer countOfAnswer;
	
	private Integer countOfFreeBoard;
	
	private Integer countOfDiary;
	
	private Integer countOfAlert;
	
	private List<Timeline> timelineList;
}
