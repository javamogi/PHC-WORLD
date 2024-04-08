package com.phcworld.freeboard.controller.port;

import java.util.List;

import com.phcworld.domain.api.model.response.FreeBoardAnswerApiResponse;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.user.infrastructure.UserEntity;

import com.phcworld.freeboard.infrastructure.dto.FreeBoardSelectDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FreeBoardResponse {
	
	private Long id;
	
	private UserEntity writer;
	
	private String title;
	
	private String contents;
	
	private String icon;
	
	private String badge;
	
	private String createDate;
	
	private Integer count;
	
	private String countOfAnswer;
	
	private List<FreeBoardAnswerApiResponse> freeBoardAnswerList;

	public static FreeBoardResponse of(FreeBoardEntity freeBoard){
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

	public static FreeBoardResponse of(FreeBoardSelectDto freeBoard){
		return FreeBoardResponse.builder()
				.id(freeBoard.getId())
				.writer(freeBoard.getWriter())
				.title(freeBoard.getTitle())
				.contents(freeBoard.getContents())
				.icon(freeBoard.getIcon())
				.badge(freeBoard.getBadge())
				.createDate(freeBoard.getFormattedCreateDate())
				.count(freeBoard.getCount())
				.countOfAnswer(freeBoard.getCountOfAnswer().toString())
				.build();
	}

	public boolean matchUser(UserEntity loginUser) {
		return this.writer.equals(loginUser);
	}

    public Long getWriterId() {
		return this.writer.getId();
    }
}
