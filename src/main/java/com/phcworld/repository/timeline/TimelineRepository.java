package com.phcworld.repository.timeline;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;

public interface TimelineRepository extends JpaRepository<Timeline, Long> {
	Page<Timeline> findByUser(User user, Pageable Pageable);
	
	Timeline findByFreeBoard(FreeBoard freeBoard);
	
	Timeline findByDiary(Diary diary);
	
	Timeline findByFreeBoardAnswer(FreeBoardAnswer freeBoardAnswer);
	
	Timeline findByDiaryAnswer(DiaryAnswer diaryAnswer);
	
//	Timeline findByTypeAndDiaryAndUser(String type, Diary diary, User user);
	
	Timeline findByGood(Good good);
}
