package com.phcworld.freeboard.domain.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class FreeBoardUpdateRequest {

	private Long id;

	private String contents;

}
