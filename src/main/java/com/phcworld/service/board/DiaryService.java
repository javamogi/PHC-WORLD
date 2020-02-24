package com.phcworld.service.board;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.user.User;

public interface DiaryService {
	Page<Diary> findPageDiary(User loginUser, Integer pageNum, User requestUser);
	
	Diary createDiary(User user, String title, String contents, String thumbnail);
	
	Diary getOneDiary(Long id);
	
	Diary updateDiary(Diary diary, String contents, String thumbnail);
	
	void deleteDiaryById(Long id);
	
	List<Diary> findDiaryListByWriter(User loginUser);
}
