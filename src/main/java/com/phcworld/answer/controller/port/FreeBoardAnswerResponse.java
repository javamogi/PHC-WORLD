package com.phcworld.answer.controller.port;


import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.api.dashboard.dto.UserResponseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FreeBoardAnswerResponse {

	private Long id;

	private UserResponseDto writer;

	private String contents;
	
	private String updateDate;

	public static FreeBoardAnswerResponse from(FreeBoardAnswer answer) {
		return FreeBoardAnswerResponse.builder()
				.id(answer.getId())
				.contents(answer.getContents())
				.writer(UserResponseDto.of(answer.getWriter()))
				.updateDate(answer.getFormattedUpdateDate())
				.build();
	}
}
