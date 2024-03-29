package com.phcworld.repository.timeline;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.good.TempGood;
import com.phcworld.domain.parent.BasicBoardAndAnswer;
import com.phcworld.domain.timeline.TempTimeline;
import com.phcworld.user.infrastructure.UserEntity;

public interface TempTimelineRepository extends JpaRepository<TempTimeline, Long> {
	Page<TempTimeline> findByUser(UserEntity user, Pageable Pageable);
	
	TempTimeline findByBoard(BasicBoardAndAnswer board);
	
	TempTimeline findByGood(TempGood good);
	
}
