package com.phcworld.service.timeline;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.api.model.request.DiaryAnswerRequest;
import com.phcworld.domain.api.model.response.DiaryAnswerApiResponse;
import com.phcworld.domain.board.*;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.timeline.SaveType;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;
import com.phcworld.exception.model.CustomException;
import com.phcworld.repository.answer.DiaryAnswerRepository;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.repository.board.FreeBoardRepository;
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
	private final DiaryRepository diaryRepository;
	private final FreeBoardRepository freeBoardRepository;
	private final DiaryAnswerRepository diaryAnswerRepository;

//	@Override
//	public List<Timeline> findTimelineList(Integer timelinePageNum, User user) {
////		PageRequest pageRequest = PageRequest.of(timelinePageNum, 5, new Sort(Direction.DESC, "id"));
//		PageRequest pageRequest = PageRequest.of(timelinePageNum, 5, Sort.by("id").descending());
////		PageRequest pageRequest = PageRequest.of(timelinePageNum, 5, new Sort(Direction.DESC, "saveDate"));
//		Page<Timeline> timelineList = timelineRepository.findByUser(user, pageRequest);
//		return timelineList.getContent();
//	}

	@Override
	public List<Timeline> findTimelineList(Integer timelinePageNum, User user) {
		PageRequest pageRequest = PageRequest.of(timelinePageNum, 5, Sort.by("id").descending());
		Page<Timeline> timelineList = timelineRepository.findByUser(user, pageRequest);
		return timelineList.getContent();
	}
	
	@Override
	public Timeline getOneTimeline(Long id) {
		return timelineRepository.findById(id)
				.orElseThrow(() -> new CustomException("400", "타임라인이 존재하지 않습니다."));
	}
	
	public Timeline createTimeline(Diary diary) {
		Timeline diaryTimeline = Timeline.builder()
				.saveType(SaveType.DIARY)
				.postId(diary.getId())
				.redirectId(diary.getId())
				.user(diary.getWriter())
				.saveDate(diary.getCreateDate())
				.build();
		return timelineRepository.save(diaryTimeline);
	}

	public Timeline createTimeline(DiaryAnswer diaryAnswer) {
		Timeline diaryAnswerTimeline = Timeline.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.user(diaryAnswer.getWriter())
				.saveDate(diaryAnswer.getCreateDate())
				.build();
		return timelineRepository.save(diaryAnswerTimeline);
	}

	public Timeline createTimeline(FreeBoard freeBoard) {
		Timeline freeBoardTimeline = Timeline.builder()
				.saveType(SaveType.FREE_BOARD)
				.postId(freeBoard.getId())
				.redirectId(freeBoard.getId())
				.user(freeBoard.getWriter())
				.saveDate(freeBoard.getCreateDate())
				.build();
		return timelineRepository.save(freeBoardTimeline);
	}

	public Timeline createTimeline(FreeBoardAnswer freeBoardAnswer) {
		Timeline freeBoardAnswerTimeline = Timeline.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.user(freeBoardAnswer.getWriter())
				.saveDate(freeBoardAnswer.getCreateDate())
				.build();
		return timelineRepository.save(freeBoardAnswerTimeline);
	}
	
	public Timeline createTimeline(Good good) {
		Timeline timeline = Timeline.builder()
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.user(good.getUser())
				.saveDate(LocalDateTime.now())
				.build();
		return timelineRepository.save(timeline);
	}
	
	public void deleteTimeline(Diary diary) {
		Timeline timeline = timelineRepository.findBySaveTypeAndPostIdAndRedirectId(SaveType.DIARY, diary.getId(), diary.getId());
		timelineRepository.delete(timeline);
	}

	public void deleteTimeline(DiaryAnswer diaryAnswer) {
		Timeline timeline = timelineRepository.findBySaveTypeAndPostIdAndRedirectId(SaveType.DIARY_ANSWER, diaryAnswer.getId(), diaryAnswer.getDiary().getId());
		timelineRepository.delete(timeline);
	}

	public void deleteTimeline(FreeBoard freeBoard) {
		Timeline timeline = timelineRepository.findBySaveTypeAndPostIdAndRedirectId(SaveType.FREE_BOARD, freeBoard.getId(), freeBoard.getId());
		timelineRepository.delete(timeline);
	}

	public void deleteTimeline(FreeBoardAnswer freeBoardAnswer) {
		Timeline timeline = timelineRepository.findBySaveTypeAndPostIdAndRedirectId(SaveType.FREE_BOARD_ANSWER, freeBoardAnswer.getId(), freeBoardAnswer.getFreeBoard().getId());
		timelineRepository.delete(timeline);
	}

	public void deleteTimeline(Good good) {
		Timeline timeline = timelineRepository.findBySaveTypeAndPostIdAndRedirectId(SaveType.GOOD, good.getId(), good.getDiary().getId());
		timelineRepository.delete(timeline);
	}

	public void deleteTimeline(Long timelineId){
		Timeline timeline = timelineRepository.findById(timelineId)
				.orElseThrow(() -> new CustomException("400", "타임라인이 존재하지 않습니다."));
		timelineRepository.delete(timeline);
	}
	
}
