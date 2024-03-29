package com.phcworld.repository.timeline;

import com.phcworld.domain.embedded.PostInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.timeline.Timeline;
import com.phcworld.user.infrastructure.UserEntity;

import java.util.Optional;

public interface TimelineRepository extends JpaRepository<Timeline, Long>, TimelineRepositoryCustom {
	Page<Timeline> findByUser(UserEntity user, Pageable Pageable);

//	Timeline findBySaveTypeAndPostIdAndRedirectId(SaveType saveType, Long postId, Long redirectId);
	Optional<Timeline> findByPostInfo(PostInfo postInfo);

//	Timeline findByFreeBoard(FreeBoard freeBoard);
//
//	Timeline findByDiary(Diary diary);
//
//	Timeline findByFreeBoardAnswer(FreeBoardAnswer freeBoardAnswer);
//
//	Timeline findByDiaryAnswer(DiaryAnswer diaryAnswer);
//
////	Timeline findByTypeAndDiaryAndUser(String type, Diary diary, User user);
//
//	Timeline findByGood(Good good);
}
