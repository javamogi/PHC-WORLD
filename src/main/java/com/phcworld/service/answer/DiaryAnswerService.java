package com.phcworld.service.answer;

import java.util.List;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.api.model.response.DiaryAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.user.User;

public interface DiaryAnswerService {
//	DiaryAnswer createDiaryAnswer(User user, Long diaryId, String saveContents);
	DiaryAnswerApiResponse createDiaryAnswer(User user, Long diaryId, String saveContents);
	
//	String deleteDiaryAnswer(Long id, User loginUser, Long diaryId);
	SuccessResponse deleteDiaryAnswer(Long id, User loginUser, Long diaryId);
	
	List<DiaryAnswer> findDiaryAnswerListByWriter(User loginUser);
}
