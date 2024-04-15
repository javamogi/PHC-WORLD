package com.phcworld.answer.controller.port;


import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.user.infrastructure.UserEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FreeBoardAnswerResponse {

	private Long id;

	private UserEntity writer;

	private String contents;
	
	private Long freeBoardId;
	
	private String countOfAnswers;

	private String updateDate;

	public static FreeBoardAnswerResponse of(FreeBoardAnswerEntity answer){
		return FreeBoardAnswerResponse.builder()
				.id(answer.getId())
				.contents(answer.getContents())
				.countOfAnswers(answer.getFreeBoard().getCountOfAnswer())
				.freeBoardId(answer.getFreeBoard().getId())
				.writer(answer.getWriter())
				.updateDate(answer.getFormattedUpdateDate())
				.build();
	}
}
