package com.phcworld.domain.timeline;

import org.springframework.stereotype.Component;

import com.phcworld.domain.board.BasicBoard;

@Component
public class TempTimelineFactory {
	
	public TempTimeline createTimeline(String type, BasicBoard board) {
		TempTimeline timeline = null;
		if(type.equals("board")) {
			timeline = TempTimeline.builder()
					.type(type)
					.icon("list-alt")
					.user(board.getWriter())
					.saveDate(board.getCreateDate())
					.build();
		} else if(type.equals("diary")) {
			timeline = TempTimeline.builder()
					.type(type)
					.icon("edit")
					.user(board.getWriter())
					.saveDate(board.getCreateDate())
					.build();
		} 
		return timeline;
	}

}
