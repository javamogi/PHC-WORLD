package com.phcworld.domain.api.model.response;


import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.user.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FreeBoardAnswerApiResponse {

	private Long id;

	private User writer;

	private String contents;
	
	private Long freeBoardId;
	
	private String countOfAnswers;

	private String updateDate;

	public static FreeBoardAnswerApiResponse of(FreeBoardAnswer answer){
		return FreeBoardAnswerApiResponse.builder()
				.id(answer.getId())
				.contents(answer.getContents())
				.countOfAnswers(answer.getFreeBoard().getCountOfAnswer())
				.freeBoardId(answer.getFreeBoard().getId())
				.writer(answer.getWriter())
				.updateDate(answer.getFormattedUpdateDate())
				.build();
	}
}
