package com.phcworld.domain.api.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryAnswerRequest {
	
	private Long id;
	
	private String contents;

    public boolean isContentsEmpty() {
        return this.getContents().isEmpty();
    }
}
