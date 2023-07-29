package com.phcworld.domain.board;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.api.model.response.FreeBoardAnswerApiResponse;
import com.phcworld.domain.user.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FreeBoardResponse {
	
	private Long id;
	
	private User writer;
	
	private String title;
	
	private String contents;
	
	private String icon;
	
	private String badge;
	
	private String createDate;
	
	private Integer count;
	
	private String countOfAnswer;
	
	private List<FreeBoardAnswerApiResponse> freeBoardAnswerList;

	public static FreeBoardResponse of(FreeBoard freeBoard){
		return FreeBoardResponse.builder()
				.id(freeBoard.getId())
				.writer(freeBoard.getWriter())
				.title(freeBoard.getTitle())
				.contents(freeBoard.getContents())
				.icon(freeBoard.getIcon())
				.badge(freeBoard.getBadge())
				.createDate(freeBoard.getFormattedCreateDate())
				.count(freeBoard.getCount())
				.countOfAnswer(freeBoard.getCountOfAnswer())
				.build();
	}

}
