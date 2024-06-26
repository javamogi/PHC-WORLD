package com.phcworld.medium.service.timeline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.embedded.PostInfo;
import com.phcworld.domain.good.Good;
import com.phcworld.freeboard.service.FreeBoardServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;
import com.phcworld.user.domain.Authority;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.user.infrastructure.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.service.answer.DiaryAnswerServiceImpl;
import com.phcworld.answer.service.FreeBoardAnswerServiceImpl;
import com.phcworld.service.board.DiaryServiceImpl;
import org.springframework.transaction.annotation.Transactional;

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

	private UserEntity user;
	private Diary diary;
	private DiaryAnswer diaryAnswer;
	private FreeBoardEntity freeBoard;
	private FreeBoardAnswerEntity freeBoardAnswer;

	@Before
	public void setup(){
		user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		diaryAnswer = DiaryAnswer.builder()
				.id(1L)
				.writer(user)
				.diary(diary)
				.contents("test")
				.build();
		freeBoard = FreeBoardEntity.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.createDate(LocalDateTime.now())
				.build();
		freeBoardAnswer = FreeBoardAnswerEntity.builder()
				.id(1L)
				.writer(user)
				.freeBoard(freeBoard)
				.contents("test")
				.build();
	}
	
	@Test
	public void createDiaryTimeline() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY)
				.postId(diary.getId())
				.redirectId(diary.getId())
				.build();
		Timeline diaryTimeline = Timeline.builder()
				.postInfo(postInfo)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		when(timelineService.createTimeline(diary))
		.thenReturn(diaryTimeline);
		Timeline createdDiaryTimeline = timelineService.createTimeline(diary);
		assertThat(SaveType.DIARY).isEqualTo(createdDiaryTimeline.getPostInfo().getSaveType());
	}
	
	@Test
	public void createDiaryAnswerTimeline() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diary.getId())
				.build();
		Timeline diaryAnswerTimeline = Timeline.builder()
				.postInfo(postInfo)
				.user(user)
				.saveDate(diaryAnswer.getCreateDate())
				.build();
		when(timelineService.createTimeline(diaryAnswer))
		.thenReturn(diaryAnswerTimeline);
		Timeline createdDiaryAnswerTimeline = timelineService.createTimeline(diaryAnswer);
		assertThat(SaveType.DIARY_ANSWER).isEqualTo(createdDiaryAnswerTimeline.getPostInfo().getSaveType());
	}

	@Test
	public void createFreeBoardTimeline() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD)
				.postId(freeBoard.getId())
				.redirectId(freeBoard.getId())
				.build();
		Timeline freeBoardTimeline = Timeline.builder()
				.postInfo(postInfo)
				.user(user)
				.saveDate(freeBoard.getCreateDate())
				.build();
		when(timelineService.createTimeline(freeBoard))
		.thenReturn(freeBoardTimeline);
		Timeline createdFreeBoardTimeline = timelineService.createTimeline(freeBoard);
		assertThat(SaveType.FREE_BOARD).isEqualTo(createdFreeBoardTimeline.getPostInfo().getSaveType());
	}

	@Test
	public void createFreeBoardAnswerTimeline() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		Timeline freeBoardAnswerTimeline = Timeline.builder()
				.postInfo(postInfo)
				.user(user)
				.saveDate(freeBoardAnswer.getCreateDate())
				.build();
		when(timelineService.createTimeline(freeBoardAnswer))
		.thenReturn(freeBoardAnswerTimeline);
		Timeline createdFreeBoardAnswerTimeline = timelineService.createTimeline(freeBoardAnswer);
		assertThat(SaveType.FREE_BOARD_ANSWER).isEqualTo(createdFreeBoardAnswerTimeline.getPostInfo().getSaveType());
	}

	@Test
	public void createGoodTimeline() {
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.createDate(LocalDateTime.now())
				.build();
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
		when(timelineService.createTimeline(good))
		.thenReturn(timeline);
		Timeline createdGoodTimeline = timelineService.createTimeline(good);
		assertThat(SaveType.GOOD).isEqualTo(createdGoodTimeline.getPostInfo().getSaveType());
	}

	@Test
	public void getOneTimeline() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY)
				.postId(diary.getId())
				.redirectId(diary.getId())
				.build();
		Timeline diaryTimeline = Timeline.builder()
				.id(1L)
				.postInfo(postInfo)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		when(timelineService.getOneTimeline(diaryTimeline.getId()))
		.thenReturn(diaryTimeline);
		Timeline selectedTimeline = timelineService.getOneTimeline(diaryTimeline.getId());
		assertThat(diaryTimeline).isEqualTo(selectedTimeline);
	}

	@Test(expected = NotFoundException.class)
	public void getOneTimeline_exception() {
		Timeline diaryTimeline = Timeline.builder()
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		when(timelineService.getOneTimeline(diaryTimeline.getId()))
				.thenThrow(NotFoundException.class);
		timelineService.getOneTimeline(diaryTimeline.getId());
	}

	@Test
	@Transactional
	public void findTimelineList() throws Exception {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY)
				.postId(diary.getId())
				.redirectId(diary.getId())
				.build();
		Timeline diaryTimeline = Timeline.builder()
				.postInfo(postInfo)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		PostInfo postInfo2 = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD)
				.postId(freeBoard.getId())
				.redirectId(freeBoard.getId())
				.build();
		Timeline freeBoardTimeline = Timeline.builder()
				.postInfo(postInfo2)
				.user(user)
				.saveDate(freeBoard.getCreateDate())
				.build();
		List<Timeline> list = new ArrayList<Timeline>();
		list.add(diaryTimeline);
		list.add(freeBoardTimeline);
		when(timelineService.findTimelineList(0, user))
		.thenReturn(list);
		List<Timeline> timelineList = timelineService.findTimelineList(0, user);
		assertThat(timelineList)
				.contains(diaryTimeline)
				.contains(freeBoardTimeline);
	}

	@Test
	public void deleteDiaryTimeline() {
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.createDate(LocalDateTime.now())
				.build();
		timelineService.deleteTimeline(SaveType.GOOD, good.getId());
		verify(timelineService, times(1)).deleteTimeline(SaveType.GOOD, good.getId());
	}

	@Test
	public void deleteDiaryAnswerTimeline() {
		timelineService.deleteTimeline(SaveType.DIARY_ANSWER, diaryAnswer.getId());
		verify(timelineService, times(1)).deleteTimeline(SaveType.DIARY_ANSWER, diaryAnswer.getId());
	}

	@Test
	public void deleteFreeBoardTimeline() {
		timelineService.deleteTimeline(SaveType.FREE_BOARD, freeBoard.getId());
		verify(timelineService, times(1)).deleteTimeline(SaveType.FREE_BOARD, freeBoard.getId());
	}

	@Test
	public void deleteFreeBoardAnswerTimeline() {
		timelineService.deleteTimeline(SaveType.FREE_BOARD_ANSWER, freeBoardAnswer.getId());
		verify(timelineService, times(1)).deleteTimeline(SaveType.FREE_BOARD_ANSWER, freeBoardAnswer.getId());
	}

	@Test
	public void deleteGood() {
		UserEntity user2 = UserEntity.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		Good good = Good.builder()
				.diary(diary)
				.user(user2)
				.build();
		timelineService.deleteTimeline(SaveType.GOOD, good.getId());
		verify(timelineService, times(1)).deleteTimeline(SaveType.GOOD, good.getId());
	}
}
