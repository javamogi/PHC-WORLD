package com.phcworld.domain.timeline;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;
import com.phcworld.service.answer.DiaryAnswerServiceImpl;
import com.phcworld.service.answer.FreeBoardAnswerServiceImpl;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.service.board.FreeBoardServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TimelineServiceImplTest {
	
	@Mock
	private TimelineServiceImpl timelineService;
	
	@Mock
	private DiaryServiceImpl diaryService;
	
	@Mock
	private DiaryAnswerServiceImpl diaryAnswerService;
	
	@Mock
	private FreeBoardServiceImpl freeBoardService;
	
	@Mock
	private FreeBoardAnswerServiceImpl freeBoardAnswerService;
	
	@Test
	public void createDiaryTimeline() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Timeline diaryTimeline = Timeline.builder()
				.type("diary")
				.icon("edit")
				.diary(diary)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		when(timelineService.createTimeline(diaryTimeline))
		.thenReturn(diaryTimeline);
		Timeline createdDiaryTimeline = timelineService.createTimeline(diaryTimeline);
		assertThat("diary", is(createdDiaryTimeline.getType()));
		assertThat("edit", is(createdDiaryTimeline.getIcon()));
	}
	
	@Test
	public void createDiaryAnswerTimeline() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.build();
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.writer(user)
				.diary(diary)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		Timeline diaryAnswerTimeline = Timeline.builder()
				.type("diary answer")
				.icon("comment")
				.diaryAnswer(diaryAnswer)
				.user(user)
				.saveDate(diaryAnswer.getCreateDate())
				.build();
		when(timelineService.createTimeline(diaryAnswerTimeline))
		.thenReturn(diaryAnswerTimeline);
		Timeline createdDiaryAnswerTimeline = timelineService.createTimeline(diaryAnswerTimeline);
		assertThat("diary answer", is(createdDiaryAnswerTimeline.getType()));
		assertThat("comment", is(createdDiaryAnswerTimeline.getIcon()));
	}
	
	@Test
	public void createFreeBoardTimeline() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.badge("")
				.createDate(LocalDateTime.now())
				.build();
		Timeline freeBoardTimeline = Timeline.builder()
				.type("free board")
				.icon("list-alt")
				.freeBoard(freeBoard)
				.user(user)
				.saveDate(freeBoard.getCreateDate())
				.build();
		when(timelineService.createTimeline(freeBoardTimeline))
		.thenReturn(freeBoardTimeline);
		Timeline createdDiaryTimeline = timelineService.createTimeline(freeBoardTimeline);
		assertThat("free board", is(createdDiaryTimeline.getType()));
		assertThat("list-alt", is(createdDiaryTimeline.getIcon()));
	}
	
	@Test
	public void createFreeBoardAnswerTimeline() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.badge("")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.writer(user)
				.freeBoard(freeBoard)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		Timeline freeBoardAnswerTimeline = Timeline.builder()
				.type("freeBoard answer")
				.icon("comment")
				.freeBoardAnswer(freeBoardAnswer)
				.user(user)
				.saveDate(freeBoardAnswer.getCreateDate())
				.build();
		when(timelineService.createTimeline(freeBoardAnswerTimeline))
		.thenReturn(freeBoardAnswerTimeline);
		Timeline createdDiaryTimeline = timelineService.createTimeline(freeBoardAnswerTimeline);
		assertThat("freeBoard answer", is(createdDiaryTimeline.getType()));
		assertThat("comment", is(createdDiaryTimeline.getIcon()));
	}
	
	@Test
	public void getOneTimeline() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Timeline diaryTimeline = Timeline.builder()
				.id(1L)
				.type("diary")
				.icon("edit")
				.diary(diary)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		when(timelineService.getOneTimeline(diaryTimeline.getId()))
		.thenReturn(diaryTimeline);
		Timeline selectedTimeline = timelineService.getOneTimeline(diaryTimeline.getId());
		assertThat(diaryTimeline, is(selectedTimeline));
	}

	@Test
	@Transactional
	public void findPageTimelineByUser() throws Exception {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Timeline diaryTimeline = Timeline.builder()
				.type("diary")
				.icon("edit")
				.diary(diary)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.badge("")
				.createDate(LocalDateTime.now())
				.build();
		Timeline freeBoardTimeline = Timeline.builder()
				.type("free board")
				.icon("list-alt")
				.freeBoard(freeBoard)
				.user(user)
				.saveDate(freeBoard.getCreateDate())
				.build();
		
		List<Timeline> list = new ArrayList<Timeline>();
		list.add(diaryTimeline);
		list.add(freeBoardTimeline);
		Page<Timeline> page = new PageImpl<Timeline>(list);
		when(timelineService.findPageTimelineByUser(user))
		.thenReturn(page);		
		Page<Timeline> timelinePage = timelineService.findPageTimelineByUser(user);
		List<Timeline> timelineList = timelinePage.getContent();
		assertThat(timelineList, hasItems(diaryTimeline, freeBoardTimeline));
	}
	
	@Test
	@Transactional
	public void findTimelineList() throws Exception {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Timeline diaryTimeline = Timeline.builder()
				.type("diary")
				.icon("edit")
				.diary(diary)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.badge("")
				.createDate(LocalDateTime.now())
				.build();
		Timeline freeBoardTimeline = Timeline.builder()
				.type("free board")
				.icon("list-alt")
				.freeBoard(freeBoard)
				.user(user)
				.saveDate(freeBoard.getCreateDate())
				.build();
		List<Timeline> list = new ArrayList<Timeline>();
		list.add(diaryTimeline);
		list.add(freeBoardTimeline);
		when(timelineService.findTimelineList(0, user))
		.thenReturn(list);
		List<Timeline> timelineList = timelineService.findTimelineList(0, user);
		assertThat(timelineList, hasItems(diaryTimeline, freeBoardTimeline));
	}
}
