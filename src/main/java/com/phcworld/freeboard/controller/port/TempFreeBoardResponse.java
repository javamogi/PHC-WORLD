package com.phcworld.freeboard.controller.port;

import java.util.List;

import com.phcworld.domain.api.model.response.FreeBoardAnswerApiResponse;
import com.phcworld.user.infrastructure.UserEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TempFreeBoardResponse {
	
	private Long id;
	
	private UserEntity writer;
	
	private String title;
	
	private String contents;
	
	private String icon;
	
	private String badge;
	
	private String createDate;
	
	private Integer count;
	
	private String countOfAnswer;
	
	private List<FreeBoardAnswerApiResponse> freeBoardAnswerList;

}
