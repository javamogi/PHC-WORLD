package com.phcworld.freeboard.domain.dto;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
public class FreeBoardUpdateRequest {

	private Long id;

	private String contents;

}
