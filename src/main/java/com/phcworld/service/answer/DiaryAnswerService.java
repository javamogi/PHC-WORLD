package com.phcworld.service.answer;

import java.util.List;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.user.User;

public interface DiaryAnswerService {
	DiaryAnswer createDiaryAnswer(User user, Diary diary, String saveContents);
	
	String deleteDiaryAnswer(Long id, User loginUser, Long diaryId);
	
	List<DiaryAnswer> findDiaryAnswerListByWriter(User loginUser);
}
