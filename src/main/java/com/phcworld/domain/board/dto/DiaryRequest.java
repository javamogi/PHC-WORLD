package com.phcworld.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaryRequest {
	
	private Long id;
	
	private String title;
	
	private String contents;
	
	private String thumbnail;

}
