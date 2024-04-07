package com.phcworld.service.board;


import java.util.List;

import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.domain.Page;

import com.phcworld.domain.board.TempDiary;
import com.phcworld.domain.board.dto.TempDiaryRequest;
import com.phcworld.domain.board.dto.TempDiaryResponse;

public interface TempDiaryService {
	Page<TempDiary> findPageDiary(UserEntity loginUser, Integer pageNum, UserEntity requestUser);
	
	TempDiaryResponse createDiary(UserEntity user, TempDiaryRequest diaryRequest);
	
	TempDiaryResponse getOneDiary(Long id);
	
	TempDiaryResponse updateDiary(TempDiaryRequest diaryRequest);
	
	void deleteDiary(Long id);
	
	List<TempDiary> findDiaryListByWriter(UserEntity loginUser);
}
