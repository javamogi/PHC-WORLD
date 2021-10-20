package com.phcworld.service.timeline;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phcworld.domain.answer.TempDiaryAnswer;
import com.phcworld.domain.answer.TempFreeBoardAnswer;
import com.phcworld.domain.board.TempDiary;
import com.phcworld.domain.board.TempFreeBoard;
import com.phcworld.domain.timeline.TempTimeline;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class TempTimelineServiceTest {
	
	@Mock
	private TempTimelineServiceImpl timelineService;
	
	@Test
	public void createTempDiaryTimeline() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		
		TempDiary diary = TempDiary.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("contents")
				.thumbnail("thumbnail")
				.build();
		TempTimeline diaryTimeline = TempTimeline.builder()
				.type("diary")
				.icon("edit")
				.url("redirect:/diaries/1")
				.board(diary)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		when(timelineService.createTimeline("diary", diary, diary.getId()))
		.thenReturn(diaryTimeline);
		TempTimeline createdDiaryTimeline = timelineService.createTimeline("diary", diary, diary.getId());
		log.info("timeline : {}", createdDiaryTimeline);
		assertThat("diary", is(createdDiaryTimeline.getType()));
		assertThat("edit", is(createdDiaryTimeline.getIcon()));
	}
	
	@Test
	public void createTempDiaryAnswerTimeline() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		
		TempDiary diary = TempDiary.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("contents")
				.thumbnail("thumbnail")
				.build();
		
		TempDiaryAnswer diaryAnswer = TempDiaryAnswer.builder()
				.id(1L)
				.writer(user)
				.contents("contents")
				.tempDiary(diary)
				.build();
		TempTimeline diaryAnswerTimeline = TempTimeline.builder()
				.type("diary answer")
				.icon("comment")
				.url("redirect:/diaries/1")
				.board(diaryAnswer)
				.user(diaryAnswer.getWriter())
				.saveDate(diary.getCreateDate())
				.build();
		
		when(timelineService.createTimeline("diary answer", diaryAnswer, diary.getId()))
		.thenReturn(diaryAnswerTimeline);
		TempTimeline createdDiaryTimeline = timelineService.createTimeline("diary answer", diaryAnswer, diary.getId());
		log.info("timeline : {}", createdDiaryTimeline);
		assertThat("diary answer", is(createdDiaryTimeline.getType()));
		assertThat("comment", is(createdDiaryTimeline.getIcon()));
	}
	
	@Test
	public void createTempFreeBoardTimeline() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		TempFreeBoard board = TempFreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("contents")
				.count(0)
				.badge("")
				.build();
		TempTimeline boardTimeline = TempTimeline.builder()
				.type("free board")
				.icon("list-alt")
				.url("redirect:/diaries/1")
				.board(board)
				.user(board.getWriter())
				.saveDate(board.getCreateDate())
				.build();
		
		when(timelineService.createTimeline("free board", board, board.getId()))
		.thenReturn(boardTimeline);
		TempTimeline createdBoardTimeline = timelineService.createTimeline("free board", board, board.getId());
		log.info("timeline : {}", createdBoardTimeline);
		assertThat("free board", is(createdBoardTimeline.getType()));
		assertThat("list-alt", is(createdBoardTimeline.getIcon()));
	}
	
	@Test
	public void createTempFreeBoardAnswerTimeline() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		TempFreeBoard board = TempFreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("contents")
				.count(0)
				.badge("")
				.build();
		
		TempFreeBoardAnswer boardAnswer = TempFreeBoardAnswer.builder()
				.id(1L)
				.writer(user)
				.contents("contents")
				.tempFreeBoard(board)
				.build();
		
		TempTimeline boardAnswerTimeline = TempTimeline.builder()
				.type("freeBoard answer")
				.icon("comment")
				.url("redirect:/freeboards/1")
				.board(boardAnswer)
				.user(boardAnswer.getWriter())
				.saveDate(boardAnswer.getCreateDate())
				.build();
		
		when(timelineService.createTimeline("freeBoard answer", boardAnswer, board.getId()))
		.thenReturn(boardAnswerTimeline);
		TempTimeline createdBoardTimeline = timelineService.createTimeline("freeBoard answer", boardAnswer, board.getId());
		log.info("timeline : {}", createdBoardTimeline);
		assertThat("freeBoard answer", is(createdBoardTimeline.getType()));
		assertThat("comment", is(createdBoardTimeline.getIcon()));
	}
	
//	@Test
//	public void deleteTimeline() {
//		User user = User.builder()
//				.id(1L)
//				.email("test3@test.test")
//				.password("test3")
//				.name("테스트3")
//				.profileImage("blank-profile-picture.png")
//				.authority("ROLE_USER")
//				.createDate(LocalDateTime.now())
//				.build();
//		
//		TempDiary diary = TempDiary.builder()
//				.writer(user)
//				.title("title")
//				.contents("contents")
//				.thumbnail("thumbnail")
//				.build();
//		TempTimeline diaryTimeline = TempTimeline.builder()
//				.type("diary")
//				.icon("edit")
//				.user(user)
//				.saveDate(diary.getCreateDate())
//				.build();
//		when(timelineService.createTimeline("diary", diary, diary.getId()))
//		.thenReturn(diaryTimeline);
//		TempTimeline createdDiaryTimeline = timelineService.createTimeline("diary", diary, diary.getId());
//		assertThat("diary", is(createdDiaryTimeline.getType()));
//		assertThat("edit", is(createdDiaryTimeline.getIcon()));
//		
//		/*
//		 * List<TempDiaryAnswer> list = new ArrayList<TempDiaryAnswer>();
//		 * list.add(answer); diary.setTempDiaryAnswers(list);
//		 * diaryService.deleteDiary(diary.getId()); verify(diaryService,
//		 * times(1)).deleteDiary(diary.getId());
//		 */
//	}

//	@Test
//	public void getTilmelineList() {
//		List<TempTimeline> timelineList = new ArrayList<TempTimeline>();
//		User user = User.builder()
//				.id(1L)
//				.email("test3@test.test")
//				.password("test3")
//				.name("테스트3")
//				.profileImage("blank-profile-picture.png")
//				.authority("ROLE_USER")
//				.createDate(LocalDateTime.now())
//				.build();
//		Diary diary = Diary.builder()
//				.id(1L)
//				.writer(user)
//				.title("test3")
//				.contents("test3")
//				.thumbnail("no-image-icon.gif")
//				.createDate(LocalDateTime.now())
//				.build();
//		TempTimeline diaryTimeline = TempTimeline.builder()
//				.type("diary")
//				.icon("edit")
//				.url("/diaries/"+diary.getId())
//				.diary(diary)
//				.saveDate(diary.getCreateDate())
//				.build();
//		timelineList.add(diaryTimeline);
//		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
//				.writer(user)
//				.diary(diary)
//				.contents("test")
//				.createDate(LocalDateTime.now())
//				.build();
//		TempTimeline diaryAnswerTimeline = TempTimeline.builder()
//				.type("diary answer")
//				.icon("comment")
//				.url("/diaries/"+diaryAnswer.getDiary().getId())
//				.diaryAnswer(diaryAnswer)
//				.saveDate(diaryAnswer.getCreateDate())
//				.build();
//		timelineList.add(diaryAnswerTimeline);
//		
//		FreeBoard freeBoard = FreeBoard.builder()
//				.id(1L)
//				.writer(user)
//				.title("test")
//				.contents("test")
//				.count(0)
//				.badge("")
//				.createDate(LocalDateTime.now())
//				.build();
//		TempTimeline freeBoardTimeline = TempTimeline.builder()
//				.type("free board")
//				.url("/freeboards/"+freeBoard.getId())
//				.icon("list-alt")
//				.freeBoard(freeBoard)
//				.saveDate(freeBoard.getCreateDate())
//				.build();
//		timelineList.add(freeBoardTimeline);
//		
//		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
//				.writer(user)
//				.freeBoard(freeBoard)
//				.contents("test")
//				.createDate(LocalDateTime.now())
//				.build();
//		TempTimeline freeBoardAnswerTimeline = TempTimeline.builder()
//				.type("free board answer")
//				.icon("comment")
//				.url("/freeboards/"+freeBoardAnswer.getFreeBoard().getId())
//				.freeBoardAnswer(freeBoardAnswer)
//				.saveDate(freeBoardAnswer.getCreateDate())
//				.build();
//		timelineList.add(freeBoardAnswerTimeline);
//		
//		Good good = Good.builder()
//				.diary(diary)
//				.user(user)
//				.createDate(LocalDateTime.now())
//				.build();
//		TempTimeline goodTimeline = TempTimeline.builder()
//				.type("good")
//				.icon("thumbs-up")
//				.url("/diaries/"+good.getDiary().getId())
//				.good(good)
//				.saveDate(good.getCreateDate())
//				.build();
//		timelineList.add(goodTimeline);
//		
//		Comparator<TempTimeline> compTimeline = new Comparator<TempTimeline>() {
//			@Override
//			public int compare(TempTimeline a, TempTimeline b) {
//				return b.getSaveDate().compareTo(a.getSaveDate());
//			}
//		};
//		timelineList.sort(compTimeline);
//		assertThat(timelineList, hasItems(diaryTimeline, diaryAnswerTimeline, 
//				freeBoardTimeline, freeBoardAnswerTimeline, goodTimeline));
//		timelineList.stream().forEach(timeline -> {
//			log.info("Timeline : {}", timeline);
//		});
//	}

}
