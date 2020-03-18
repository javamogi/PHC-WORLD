package com.phcworld.domain.api.model.response;


import com.phcworld.domain.user.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FreeBoardAnswerApiResponse {

	private Long id;

	private User writer;

	private String contents;
	
	private String countOfAnswers;

	private String createDate;
	
}
