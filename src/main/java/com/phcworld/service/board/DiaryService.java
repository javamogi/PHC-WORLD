package com.phcworld.service.board;


import java.util.List;

import org.springframework.data.domain.Page;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryRequest;
import com.phcworld.domain.board.DiaryResponse;
import com.phcworld.domain.user.User;

public interface DiaryService {
	Page<Diary> findPageDiary(User loginUser, Integer pageNum, User requestUser);
	
	DiaryResponse createDiary(User user, DiaryRequest diaryRequest);
	
	DiaryResponse getOneDiary(Long id);
	
	DiaryResponse updateDiary(DiaryRequest diaryRequest);
	
	void deleteDiary(Long id);
	
	List<Diary> findDiaryListByWriter(User loginUser);
}
