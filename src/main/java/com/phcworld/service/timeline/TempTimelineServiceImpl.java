package com.phcworld.service.timeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.board.BasicBoard;
import com.phcworld.domain.timeline.TempTimeline;
import com.phcworld.domain.timeline.TempTimelineFactory;
import com.phcworld.repository.timeline.TempTimelineRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class TempTimelineServiceImpl {

	@Autowired
	private TempTimelineRepository temp2TimelineRepository;

	@Autowired
	private TempTimelineFactory factory;

//	@Override
//	public List<Timeline> findTimelineList(Integer timelinePageNum, User user) {
//		PageRequest pageRequest = PageRequest.of(timelinePageNum, 5, new Sort(Direction.DESC, "id"));
//		Page<Timeline> timelineList = timelineRepository.findByUser(user, pageRequest);
//		return timelineList.getContent();
//	}

//	@Override
//	public Timeline getOneTimeline(Long id) {
//		return timelineRepository.getOne(id);
//	}

	public TempTimeline createTimeline(String type, BasicBoard board) {
		TempTimeline timeline = factory.createTimeline(type, board);
		return temp2TimelineRepository.save(timeline);
	}

//	public Timeline createTimeline(DiaryAnswer diaryAnswer) {
//		Timeline diaryAnswerTimeline = Timeline.builder()
//				.type("diary answer")
//				.icon("comment")
//				.diaryAnswer(diaryAnswer)
//				.user(diaryAnswer.getWriter())
//				.saveDate(diaryAnswer.getCreateDate())
//				.build();
//		return timelineRepository.save(diaryAnswerTimeline);
//	}

//	public Timeline createTimeline(FreeBoard freeBoard) {
//		Timeline freeBoardTimeline = Timeline.builder()
//				.type("free board")
//				.icon("list-alt")
//				.freeBoard(freeBoard)
//				.user(freeBoard.getWriter())
//				.saveDate(freeBoard.getCreateDate())
//				.build();
//		return timelineRepository.save(freeBoardTimeline);
//	}

//	public Timeline createTimeline(FreeBoardAnswer freeBoardAnswer) {
//		Timeline freeBoardAnswerTimeline = Timeline.builder()
//				.type("freeBoard answer")
//				.icon("comment")
//				.freeBoardAnswer(freeBoardAnswer)
//				.user(freeBoardAnswer.getWriter())
//				.saveDate(freeBoardAnswer.getCreateDate())
//				.build();
//		return timelineRepository.save(freeBoardAnswerTimeline);
//	}
//	
//	public Timeline createTimeline(Good good) {
//		Timeline timeline = Timeline.builder()
//				.type("good")
//				.icon("thumbs-up")
//				.good(good)
//				.user(good.getUser())
//				.saveDate(LocalDateTime.now())
//				.build();
//		return timelineRepository.save(timeline);
//	}
//	
//	public void deleteTimeline(Diary diary) {
//		Timeline timeline = timelineRepository.findByDiary(diary);
//		timelineRepository.delete(timeline);
//	}
//	
//	public void deleteTimeline(DiaryAnswer diaryAnswer) {
//		Timeline timeline = timelineRepository.findByDiaryAnswer(diaryAnswer);
//		timelineRepository.delete(timeline);
//	}
//	
//	public void deleteTimeline(FreeBoard freeBoard) {
//		Timeline timeline = timelineRepository.findByFreeBoard(freeBoard);
//		timelineRepository.delete(timeline);
//	}
//	
//	public void deleteTimeline(FreeBoardAnswer freeBoardAnswer) {
//		Timeline timeline = timelineRepository.findByFreeBoardAnswer(freeBoardAnswer);
//		timelineRepository.delete(timeline);
//	}
//	
//	public void deleteTimeline(Good good) {
//		Timeline timeline = timelineRepository.findByGood(good);
//		timelineRepository.delete(timeline);
//	}

}
