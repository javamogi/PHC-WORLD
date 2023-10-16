package com.phcworld.domain.board.dto;

import java.util.List;

import com.phcworld.domain.api.model.response.DiaryAnswerApiResponse;
import com.phcworld.domain.user.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TempDiaryResponse {

	private Long id;

	private User writer;

	private String title;

	private String contents;

	private String thumbnail;

	private String countOfAnswers;

	private Integer countOfGood;

	private String updateDate;
	
	private List<DiaryAnswerApiResponse> diaryAnswerList;
}
