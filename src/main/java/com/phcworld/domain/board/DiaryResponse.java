package com.phcworld.domain.board;

import java.util.List;

import com.phcworld.domain.api.model.response.DiaryAnswerApiResponse;
import com.phcworld.domain.user.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiaryResponse {

	private Long id;

	private User writer;

	private String title;

	private String contents;

	private String thumbnail;

	private String countOfAnswers;

	private Integer countOfGood;

	private String updateDate;
	
	private List<DiaryAnswerApiResponse> diaryAnswerList;

	public static DiaryResponse of(Diary diary){
		return DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.countOfAnswers(diary.getCountOfAnswer())
				.countOfGood(diary.getCountOfGood())
				.updateDate(diary.getFormattedUpdateDate())
				.build();
	}
}
