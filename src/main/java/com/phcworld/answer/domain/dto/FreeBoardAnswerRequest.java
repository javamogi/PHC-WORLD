package com.phcworld.answer.domain.dto;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
public class FreeBoardAnswerRequest {
	
	private String contents;

	public boolean isContentsEmpty() {
		return this.getContents().isEmpty();
	}

}
