package com.phcworld.domain.api.model.response;

import com.phcworld.domain.user.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiaryAnswerApiResponse {

	private Long id;
	
	private User writer;

	private String contents;
	
	private Long diaryId;
	
	private String countOfAnswers;

	private String createDate;
	
}
