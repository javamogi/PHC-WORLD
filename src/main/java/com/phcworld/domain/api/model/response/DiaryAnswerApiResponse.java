package com.phcworld.domain.api.model.response;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.user.infrastructure.UserEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiaryAnswerApiResponse {

	private Long id;
	
	private UserEntity writer;

	private String contents;
	
	private Long diaryId;
	
	private String countOfAnswers;

	private String updateDate;

	public static DiaryAnswerApiResponse of(DiaryAnswer answer){
		return DiaryAnswerApiResponse.builder()
				.id(answer.getId())
				.writer(answer.getWriter())
				.contents(answer.getContents())
				.diaryId(answer.getDiary().getId())
				.countOfAnswers(answer.getDiary().getCountOfAnswer())
				.updateDate(answer.getFormattedUpdateDate())
				.build();
	}
}
