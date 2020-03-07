package com.phcworld.service.board;


import java.util.List;

import org.springframework.data.domain.Page;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.user.User;

public interface DiaryService {
	Page<Diary> findPageDiary(User loginUser, Integer pageNum, User requestUser);
	
	Diary createDiary(User user, Diary diary);
	
	Diary getOneDiary(Long id);
	
	Diary updateDiary(Diary diary, Diary inputDiary);
	
	void deleteDiary(Diary diary);
	
	List<Diary> findDiaryListByWriter(User loginUser);
}
