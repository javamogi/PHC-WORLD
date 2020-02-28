package com.phcworld.service.timeline;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;
import com.phcworld.repository.timeline.TimelineRepository;

@Service
@Transactional
public class TimelineServiceImpl implements TimelineService {
	
	@Autowired
	private TimelineRepository timelineRepository;

	@Override
	public List<Timeline> findTimelineList(Integer timelinePageNum, User user) {
		PageRequest pageRequest = PageRequest.of(timelinePageNum, 5, new Sort(Direction.DESC, "id"));
		Page<Timeline> timelineList = timelineRepository.findByUser(user, pageRequest);
		return timelineList.getContent();
	}
	
	@Override
	public Timeline getOneTimeline(Long id) {
		return timelineRepository.getOne(id);
	}
	
	public Timeline createTimeline(Diary diary) {
		Timeline diaryTimeline = Timeline.builder()
				.type("diary")
				.icon("edit")
				.diary(diary)
				.user(diary.getWriter())
				.saveDate(diary.getCreateDate())
				.build();
		return timelineRepository.save(diaryTimeline);
	}
	
	public Timeline createTimeline(DiaryAnswer diaryAnswer) {
		Timeline diaryAnswerTimeline = Timeline.builder()
				.type("diary answer")
				.icon("comment")
				.diaryAnswer(diaryAnswer)
				.user(diaryAnswer.getWriter())
				.saveDate(diaryAnswer.getCreateDate())
				.build();
		return timelineRepository.save(diaryAnswerTimeline);
	}
	
	public Timeline createTimeline(FreeBoard freeBoard) {
		Timeline freeBoardTimeline = Timeline.builder()
				.type("free board")
				.icon("list-alt")
				.freeBoard(freeBoard)
				.user(freeBoard.getWriter())
				.saveDate(freeBoard.getCreateDate())
				.build();
		return timelineRepository.save(freeBoardTimeline);
	}
	
	public Timeline createTimeline(FreeBoardAnswer freeBoardAnswer) {
		Timeline freeBoardAnswerTimeline = Timeline.builder()
				.type("freeBoard answer")
				.icon("comment")
				.freeBoardAnswer(freeBoardAnswer)
				.user(freeBoardAnswer.getWriter())
				.saveDate(freeBoardAnswer.getCreateDate())
				.build();
		return timelineRepository.save(freeBoardAnswerTimeline);
	}
	
	public Timeline createTimeline(Good good) {
		Timeline timeline = Timeline.builder()
				.type("good")
				.icon("thumbs-up")
				.good(good)
				.user(good.getUser())
				.saveDate(LocalDateTime.now())
				.build();
		return timelineRepository.save(timeline);
	}
	
	public void deleteTimeline(Diary diary) {
		Timeline timeline = timelineRepository.findByDiary(diary);
		timelineRepository.delete(timeline);
	}
	
	public void deleteTimeline(DiaryAnswer diaryAnswer) {
		Timeline timeline = timelineRepository.findByDiaryAnswer(diaryAnswer);
		timelineRepository.delete(timeline);
	}
	
	public void deleteTimeline(FreeBoard freeBoard) {
		Timeline timeline = timelineRepository.findByFreeBoard(freeBoard);
		timelineRepository.delete(timeline);
	}
	
	public void deleteTimeline(FreeBoardAnswer freeBoardAnswer) {
		Timeline timeline = timelineRepository.findByFreeBoardAnswer(freeBoardAnswer);
		timelineRepository.delete(timeline);
	}
	
	public void deleteTimeline(Good good) {
		Timeline timeline = timelineRepository.findByGood(good);
		timelineRepository.delete(timeline);
	}
	
}
