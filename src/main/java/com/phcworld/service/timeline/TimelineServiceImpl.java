package com.phcworld.service.timeline;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.embedded.PostInfo;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.exception.model.CustomException;
import com.phcworld.exception.model.ErrorCode;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.repository.timeline.TimelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TimelineServiceImpl implements TimelineService {
	
	private final TimelineRepository timelineRepository;

//	@Override
//	public List<Timeline> findTimelineList(Integer timelinePageNum, User user) {
////		PageRequest pageRequest = PageRequest.of(timelinePageNum, 5, new Sort(Direction.DESC, "id"));
//		PageRequest pageRequest = PageRequest.of(timelinePageNum, 5, Sort.by("id").descending());
////		PageRequest pageRequest = PageRequest.of(timelinePageNum, 5, new Sort(Direction.DESC, "saveDate"));
//		Page<Timeline> timelineList = timelineRepository.findByUser(user, pageRequest);
//		return timelineList.getContent();
//	}

	@Override
	public List<Timeline> findTimelineList(Integer timelinePageNum, UserEntity user) {
		PageRequest pageRequest = PageRequest.of(timelinePageNum - 1, 5, Sort.by("id").descending());
		Page<Timeline> timelineList = timelineRepository.findByUser(user, pageRequest);
		return timelineList.getContent();
	}
	
	@Override
	public Timeline getOneTimeline(Long id) {
		return timelineRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
	}
	
	public Timeline createTimeline(Diary diary) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY)
				.postId(diary.getId())
				.redirectId(diary.getId())
				.build();
		Timeline diaryTimeline = Timeline.builder()
				.postInfo(postInfo)
				.user(diary.getWriter())
				.saveDate(diary.getCreateDate())
				.build();
		return timelineRepository.save(diaryTimeline);
	}

	public Timeline createTimeline(DiaryAnswer diaryAnswer) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.build();
		Timeline diaryAnswerTimeline = Timeline.builder()
				.postInfo(postInfo)
				.user(diaryAnswer.getWriter())
				.saveDate(diaryAnswer.getCreateDate())
				.build();
		return timelineRepository.save(diaryAnswerTimeline);
	}

	public Timeline createTimeline(FreeBoardEntity freeBoard) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD)
				.postId(freeBoard.getId())
				.redirectId(freeBoard.getId())
				.build();
		Timeline freeBoardTimeline = Timeline.builder()
				.postInfo(postInfo)
				.user(freeBoard.getWriter())
				.saveDate(freeBoard.getCreateDate())
				.build();
		return timelineRepository.save(freeBoardTimeline);
	}

	public Timeline createTimeline(FreeBoardAnswerEntity freeBoardAnswer) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		Timeline freeBoardAnswerTimeline = Timeline.builder()
				.postInfo(postInfo)
				.user(freeBoardAnswer.getWriter())
				.saveDate(freeBoardAnswer.getCreateDate())
				.build();
		return timelineRepository.save(freeBoardAnswerTimeline);
	}
	
	public Timeline createTimeline(Good good) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.build();
		Timeline timeline = Timeline.builder()
				.postInfo(postInfo)
				.user(good.getUser())
				.saveDate(LocalDateTime.now())
				.build();
		return timelineRepository.save(timeline);
	}

	public Timeline createTimeline(FreeBoardAnswer freeBoardAnswer) {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		Timeline diaryTimeline = Timeline.builder()
				.postInfo(postInfo)
				.user(UserEntity.from(freeBoardAnswer.getWriter()))
				.saveDate(freeBoardAnswer.getCreateDate())
				.build();
		return timelineRepository.save(diaryTimeline);
	}
	
	public void deleteTimeline(Long timelineId){
		Timeline timeline = timelineRepository.findById(timelineId)
				.orElseThrow(() -> new CustomException("400", "타임라인이 존재하지 않습니다."));
		timelineRepository.delete(timeline);
	}

	public void deleteTimeline(SaveType saveType, Long id){
		timelineRepository.deleteTimeline(saveType, id);
	}
	
}
