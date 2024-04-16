package com.phcworld.answer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class FreeBoardAnswerUpdateRequest {
	
	private Long id;
	
	private String contents;

	public boolean isContentsEmpty() {
		return this.getContents().isEmpty();
	}
}
