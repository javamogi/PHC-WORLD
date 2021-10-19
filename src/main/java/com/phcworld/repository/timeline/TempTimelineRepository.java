package com.phcworld.repository.timeline;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.parent.BasicBoardAndAnswer;
import com.phcworld.domain.timeline.TempTimeline;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;

public interface TempTimelineRepository extends JpaRepository<TempTimeline, Long> {
	Page<TempTimeline> findByUser(User user, Pageable Pageable);
	
//	Timeline findByBoard(BasicBoard board);
	
//	Timeline findByDiary(Diary diary);
//	
//	Timeline findByFreeBoardAnswer(FreeBoardAnswer freeBoardAnswer);
//	
//	Timeline findByDiaryAnswer(DiaryAnswer diaryAnswer);
//	
//	Timeline findByGood(Good good);
}
