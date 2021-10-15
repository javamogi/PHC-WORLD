package com.phcworld.service.board;


import java.util.List;

import org.springframework.data.domain.Page;

import com.phcworld.domain.board.TempDiary;
import com.phcworld.domain.board.TempDiaryRequest;
import com.phcworld.domain.board.TempDiaryResponse;
import com.phcworld.domain.user.User;

public interface TempDiaryService {
	Page<TempDiary> findPageDiary(User loginUser, Integer pageNum, User requestUser);
	
	TempDiaryResponse createDiary(User user, TempDiaryRequest diaryRequest);
	
	TempDiaryResponse getOneDiary(Long id);
	
	TempDiaryResponse updateDiary(TempDiaryRequest diaryRequest);
	
	void deleteDiary(Long id);
	
	List<TempDiary> findDiaryListByWriter(User loginUser);
}
