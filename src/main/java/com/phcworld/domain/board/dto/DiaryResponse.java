package com.phcworld.domain.board.dto;

import java.util.ArrayList;
import java.util.List;

import com.phcworld.domain.api.model.response.DiaryAnswerApiResponse;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryHashtag;
import com.phcworld.domain.user.User;

import com.phcworld.repository.board.dto.DiarySelectDto;
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

	private Long countOfGood;

	private String updateDate;

	private String createDate;

	private List<DiaryAnswerApiResponse> diaryAnswerList;

	private List<String> hashtags;

	public static DiaryResponse of(Diary diary){
		List<DiaryHashtag> diaryHashtags = diary.getDiaryHashtags();
		List<String> hashtagList = new ArrayList<>();
		int size = diaryHashtags != null ? diaryHashtags.size() : 0;
		for (int i = 0; i < size; i++){
			DiaryHashtag diaryHashtag = diaryHashtags.get(i);
			hashtagList.add(diaryHashtag.getHashtagName());
		}
		return DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.countOfAnswers(diary.getCountOfAnswer())
				.countOfGood(diary.getCountGood())
				.updateDate(diary.getFormattedUpdateDate())
				.hashtags(hashtagList)
				.build();
	}

	public static DiaryResponse of(DiarySelectDto diary){
		return DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.thumbnail(diary.getThumbnail())
				.countOfAnswers(diary.getCountOfAnswers() != null ? diary.getCountOfAnswers().toString() : "0")
				.countOfGood(diary.getCountOfGood() != null ? diary.getCountOfGood() : 0)
				.updateDate(diary.getFormattedUpdateDate())
				.createDate(diary.getFormattedCreateDate())
				.hashtags(diary.getHashtags())
				.build();
	}
}
