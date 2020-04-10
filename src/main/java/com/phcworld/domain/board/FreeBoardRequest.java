package com.phcworld.domain.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreeBoardRequest {

	private Long id;

	private String title;

	private String contents;

	private String icon;

}
