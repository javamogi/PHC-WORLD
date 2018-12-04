package com.phcworld.domain.timeline;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.DiaryAnswerServiceImpl;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.answer.FreeBoardAnswerServiceImpl;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryServiceImpl;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.board.FreeBoardServiceImpl;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.good.GoodRepository;
import com.phcworld.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TimelineServiceImplTest {
	
	@Autowired
	private TimelineServiceImpl timelineService;
	
	@Autowired
	private DiaryServiceImpl diaryService;
	
	@Autowired
	private DiaryAnswerServiceImpl diaryAnswerService;
	
	@Autowired
	private FreeBoardServiceImpl freeBoardService;
	
	@Autowired
	private FreeBoardAnswerServiceImpl freeBoardAnswerService;
	
	@Autowired
	private GoodRepository goodRepository;

	@Test
	@Transactional
	public void createTimeline() {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		Diary diary = diaryService.createDiary(user, "test3", "test3", "no-image-icon.gif");
		Timeline diaryTimeline = new Timeline("diary", "edit", diary, user, diary.getCreateDate());
		Timeline createdDiaryTimeline = timelineService.createTimeline(diaryTimeline);
		Timeline actual = timelineService.getOneTimeline(createdDiaryTimeline.getId());
		assertThat(actual, is(createdDiaryTimeline));
		
		DiaryAnswer diaryAnswer = diaryAnswerService.createDiaryAnswer(user, diary, "test");
		Timeline diaryAnswerTimeline = new Timeline("Diary Answer", "comment", diary, diaryAnswer, user, diaryAnswer.getCreateDate());
		Timeline createdDiaryAnswer = timelineService.createTimeline(diaryAnswerTimeline);
		actual = timelineService.getOneTimeline(createdDiaryAnswer.getId());
		assertThat(actual, is(createdDiaryAnswer));
		
		FreeBoard freeBoard = freeBoardService.createFreeBoard(user, "test", "test", "");
		Timeline freeBoardTimeline = new Timeline("free board", "list-alt", freeBoard, user, freeBoard.getCreateDate());
		Timeline createdFreeBoard = timelineService.createTimeline(freeBoardTimeline);
		actual = timelineService.getOneTimeline(createdFreeBoard.getId());
		assertThat(actual, is(createdFreeBoard));
		
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerService.createFreeBoardAnswer(user, freeBoard, "test");
		Timeline freeBoardAnswerTimeline = new Timeline("FreeBoard Answer", "comment", freeBoard, freeBoardAnswer, user, freeBoardAnswer.getCreateDate());
		Timeline createdFreeBoardAnswer = timelineService.createTimeline(freeBoardAnswerTimeline);
		actual = timelineService.getOneTimeline(createdFreeBoardAnswer.getId());
		assertThat(actual, is(createdFreeBoardAnswer));
		
		Good good = new Good(diary, user);
		Timeline goodTimeline = new Timeline("good", "thumbs-up", diary, good, user, good.getSaveDate());
		Timeline createdGoodTimeline = timelineService.createTimeline(goodTimeline);
		actual = timelineService.getOneTimeline(createdGoodTimeline.getId());
		assertThat(actual, is(createdGoodTimeline));
	}

	@Test
	@Transactional
	public void findPageTimelineByUser() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		Diary diary = diaryService.createDiary(user, "test3", "test3", "no-image-icon.gif");
		Timeline diaryTimeline = timelineService.getOneTimeline(diary.getTimeline().getId());
		
		DiaryAnswer diaryAnswer = diaryAnswerService.createDiaryAnswer(user, diary, "test");
		Timeline diaryAnswerTimline = timelineService.getOneTimeline(diaryAnswer.getTimeline().getId());
		
		FreeBoard freeBoard = freeBoardService.createFreeBoard(user, "test", "test", "");
		Timeline freeBoardTimeline = timelineService.getOneTimeline(freeBoard.getTimeline().getId());
		
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerService.createFreeBoardAnswer(user, freeBoard, "test");
		Timeline freeBoardAnswerTimeline = timelineService.getOneTimeline(freeBoardAnswer.getTimeline().getId());
		
		Good good = new Good(diary, user);
		goodRepository.save(good);
		Timeline goodTimeline = new Timeline("good", "thumbs-up", diary, good, user, good.getSaveDate());
		Timeline createdGoodTimeline = timelineService.createTimeline(goodTimeline);
		
		Page<Timeline> timelinePage = timelineService.findPageTimelineByUser(user);
		List<Timeline> timelineList = timelinePage.getContent();
		
		assertThat(timelineList, hasItems(diaryTimeline, diaryAnswerTimline, freeBoardTimeline, freeBoardAnswerTimeline, createdGoodTimeline));
	}
	
	@Test
	@Transactional
	public void findTimelineList() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		Diary diary = diaryService.createDiary(user, "test3", "test3", "no-image-icon.gif");
		Timeline diaryTimeline = timelineService.getOneTimeline(diary.getTimeline().getId());
		
		DiaryAnswer diaryAnswer = diaryAnswerService.createDiaryAnswer(user, diary, "test");
		Timeline diaryAnswerTimline = timelineService.getOneTimeline(diaryAnswer.getTimeline().getId());
		
		FreeBoard freeBoard = freeBoardService.createFreeBoard(user, "test", "test", "");
		Timeline freeBoardTimeline = timelineService.getOneTimeline(freeBoard.getTimeline().getId());
		
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerService.createFreeBoardAnswer(user, freeBoard, "test");
		Timeline freeBoardAnswerTimeline = timelineService.getOneTimeline(freeBoardAnswer.getTimeline().getId());
		
		Good good = new Good(diary, user);
		goodRepository.save(good);
		Timeline goodTimeline = new Timeline("good", "thumbs-up", diary, good, user, good.getSaveDate());
		Timeline createdGoodTimeline = timelineService.createTimeline(goodTimeline);
		
		List<Timeline> timelineList = timelineService.findTimelineList(0, user);
		assertThat(timelineList, hasItems(diaryTimeline, diaryAnswerTimline, freeBoardTimeline, freeBoardAnswerTimeline, createdGoodTimeline));
	}
}
