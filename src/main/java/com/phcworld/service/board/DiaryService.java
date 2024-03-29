package com.phcworld.service.board;


import java.util.List;

import com.phcworld.repository.board.dto.DiarySelectDto;
import org.springframework.data.domain.Page;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.dto.DiaryRequest;
import com.phcworld.domain.board.dto.DiaryResponse;
import com.phcworld.domain.user.User;

public interface DiaryService {
	Page<DiarySelectDto> findPageDiary(User loginUser, Integer pageNum, User requestUser, String searchKeyword);
	
	DiaryResponse createDiary(User user, DiaryRequest diaryRequest);
	
	DiaryResponse getOneDiary(Long id);
	
	DiaryResponse updateDiary(DiaryRequest diaryRequest);
	
	void deleteDiary(Long id);
	
	List<Diary> findDiaryListByWriter(User loginUser);
}
