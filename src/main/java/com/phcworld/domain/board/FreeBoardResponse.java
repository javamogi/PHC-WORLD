package com.phcworld.domain.board;

import java.util.List;

import com.phcworld.domain.api.model.response.FreeBoardAnswerApiResponse;
import com.phcworld.domain.user.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FreeBoardResponse {
	
	private Long id;
	
	private User writer;
	
	private String title;
	
	private String contents;
	
	private String icon;
	
	private String badge;
	
	private String updateDate;
	
	private Integer count;
	
	private String countOfAnswer;
	
	private List<FreeBoardAnswerApiResponse> freeBoardAnswerList;

}
