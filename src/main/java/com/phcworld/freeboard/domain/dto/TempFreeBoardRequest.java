package com.phcworld.freeboard.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TempFreeBoardRequest {

	private Long id;

	private String title;

	private String contents;

	private String icon;

}
