package com.phcworld.service.board;


import java.util.List;

import com.phcworld.repository.board.dto.DiarySelectDto;
import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.domain.Page;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.dto.DiaryRequest;
import com.phcworld.domain.board.dto.DiaryResponse;

public interface DiaryService {
	Page<DiarySelectDto> findPageDiary(UserEntity loginUser, Integer pageNum, UserEntity requestUser, String searchKeyword);
	
	DiaryResponse createDiary(UserEntity user, DiaryRequest diaryRequest);
	
	DiaryResponse getOneDiary(Long id);
	
	DiaryResponse updateDiary(DiaryRequest diaryRequest);
	
	void deleteDiary(Long id);
	
	List<Diary> findDiaryListByWriter(UserEntity loginUser);
}
