package com.phcworld.answer.controller.port;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.api.dashboard.dto.UserResponseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FreeBoardAnswerResponse {

	private Long id;

	private UserResponseDto writer;

	private String contents;
	
	private String updateDate;

	private String titleOfBoard;
	private Long boardId;

	public static FreeBoardAnswerResponse from(FreeBoardAnswer answer) {
		return FreeBoardAnswerResponse.builder()
				.id(answer.getId())
				.contents(answer.getContents())
				.writer(answer.getWriter() != null ?
						UserResponseDto.of(answer.getWriter()) : null)
				.updateDate(answer.getFormattedUpdateDate())
				.titleOfBoard(answer.getFreeBoard() != null ?
						answer.getFreeBoard().getTitle() : null)
				.boardId(answer.getFreeBoard() != null ?
						answer.getFreeBoard().getId() : null)
				.build();
	}
}
