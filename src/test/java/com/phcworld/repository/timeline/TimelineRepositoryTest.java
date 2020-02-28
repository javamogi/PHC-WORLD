package com.phcworld.repository.timeline;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;
import com.phcworld.repository.answer.DiaryAnswerRepository;
import com.phcworld.repository.answer.FreeBoardAnswerRepository;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.repository.board.FreeBoardRepository;
import com.phcworld.repository.good.GoodRepository;
import com.phcworld.repository.user.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class TimelineRepositoryTest {
	
	@Autowired
	private TimelineRepository timelineRepository;
	
	@Autowired
	private DiaryRepository diaryRepository;
	
	@Autowired
	private DiaryAnswerRepository diaryAnswerRepository;
	
	@Autowired
	private FreeBoardRepository freeBoardRepository;
	
	@Autowired
	private FreeBoardAnswerRepository freeBoardAnswerRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GoodRepository goodRepository;

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
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Diary createdDiary = diaryRepository.save(diary);
		Timeline diaryTimeline = Timeline.builder()
				.type("diary")
				.icon("edit")
				.diary(createdDiary)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		Timeline createdTimeline = timelineRepository.save(diaryTimeline);
		assertNotNull(createdTimeline);
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
		Diary createdDiary = diaryRepository.save(diary);
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.writer(user)
				.diary(createdDiary)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer createdDiaryAnswer = diaryAnswerRepository.save(diaryAnswer);
		Timeline diaryAnswerTimeline = Timeline.builder()
				.type("diary answer")
				.icon("comment")
				.diaryAnswer(createdDiaryAnswer)
				.user(user)
				.saveDate(diaryAnswer.getCreateDate())
				.build();
		Timeline createdDiaryAnswerTimeline = timelineRepository.save(diaryAnswerTimeline);
		assertNotNull(createdDiaryAnswerTimeline);
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
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.badge("")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard createdFreeBoard = freeBoardRepository.save(freeBoard);
		Timeline freeBoardTimeline = Timeline.builder()
				.type("free board")
				.icon("list-alt")
				.freeBoard(createdFreeBoard)
				.user(user)
				.saveDate(freeBoard.getCreateDate())
				.build();
		Timeline createdFreeBoardTimeline = timelineRepository.save(freeBoardTimeline);
		assertNotNull(createdFreeBoardTimeline);
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
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.badge("")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard createdFreeBoard = freeBoardRepository.save(freeBoard);
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.writer(user)
				.freeBoard(createdFreeBoard)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardAnswer createdFreeBoardAnswer = freeBoardAnswerRepository.save(freeBoardAnswer);
		Timeline freeBoardAnswerTimeline = Timeline.builder()
				.type("freeBoard answer")
				.icon("comment")
				.freeBoardAnswer(createdFreeBoardAnswer)
				.user(user)
				.saveDate(freeBoardAnswer.getCreateDate())
				.build();
		Timeline createdFreeBoardAnswerTimeline = timelineRepository.save(freeBoardAnswerTimeline);
		assertNotNull(createdFreeBoardAnswerTimeline);
	}
	

	public void createGood() {
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
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Diary createdDiary = diaryRepository.save(diary);
		Good good = Good.builder()
				.diary(createdDiary)
				.user(user)
				.createDate(LocalDateTime.now())
				.build();
		Good newGood = goodRepository.save(good);
		Timeline diaryTimeline = Timeline.builder()
				.type("good")
				.icon("thumbs-up")
				.good(newGood)
				.user(newGood.getUser())
				.saveDate(diary.getCreateDate())
				.build();
		Timeline createdTimeline = timelineRepository.save(diaryTimeline);
		Timeline readTimeline = timelineRepository.findByGood(newGood);
		assertThat(createdTimeline, is(readTimeline));
	}
	
	@Test
	public void readDiaryTimeline() {
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
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Diary createdDiary = diaryRepository.save(diary);
		Timeline diaryTimeline = Timeline.builder()
				.type("diary")
				.icon("edit")
				.diary(createdDiary)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		Timeline createdTimeline = timelineRepository.save(diaryTimeline);
		Timeline readTimeline = timelineRepository.findByDiary(createdDiary);
		assertThat(createdTimeline, is(readTimeline));
	}
	
	@Test
	public void readDiaryAnswerTimeline() {
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
		Diary createdDiary = diaryRepository.save(diary);
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.writer(user)
				.diary(createdDiary)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer createdDiaryAnswer = diaryAnswerRepository.save(diaryAnswer);
		Timeline diaryAnswerTimeline = Timeline.builder()
				.type("diary answer")
				.icon("comment")
				.diaryAnswer(createdDiaryAnswer)
				.user(user)
				.saveDate(diaryAnswer.getCreateDate())
				.build();
		Timeline createdDiaryAnswerTimeline = timelineRepository.save(diaryAnswerTimeline);
		Timeline readTimeline = timelineRepository.findByDiaryAnswer(createdDiaryAnswer);
		assertThat(createdDiaryAnswerTimeline, is(readTimeline));
	}
	
	@Test
	public void readFreeBoardTimeline() {
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
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.badge("")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard createdFreeBoard = freeBoardRepository.save(freeBoard);
		Timeline freeBoardTimeline = Timeline.builder()
				.type("free board")
				.icon("list-alt")
				.freeBoard(createdFreeBoard)
				.user(user)
				.saveDate(freeBoard.getCreateDate())
				.build();
		Timeline createdFreeBoardTimeline = timelineRepository.save(freeBoardTimeline);
		Timeline readTimeline = timelineRepository.findByFreeBoard(createdFreeBoard);
		assertThat(createdFreeBoardTimeline, is(readTimeline));
	}
	
	@Test
	public void readFreeBoardAnswerTimeline() {
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
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.badge("")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard createdFreeBoard = freeBoardRepository.save(freeBoard);
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.writer(user)
				.freeBoard(createdFreeBoard)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardAnswer createdFreeBoardAnswer = freeBoardAnswerRepository.save(freeBoardAnswer);
		Timeline freeBoardAnswerTimeline = Timeline.builder()
				.type("freeBoard answer")
				.icon("comment")
				.freeBoardAnswer(createdFreeBoardAnswer)
				.user(user)
				.saveDate(freeBoardAnswer.getCreateDate())
				.build();
		Timeline createdFreeBoardAnswerTimeline = timelineRepository.save(freeBoardAnswerTimeline);
		Timeline readTimeline = timelineRepository.findByFreeBoardAnswer(createdFreeBoardAnswer);
		assertThat(createdFreeBoardAnswerTimeline, is(readTimeline));
	}
	
	@Test
	public void readGood() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		User user2 = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		User user3 = User.builder()
				.id(3L)
				.email("test5@test.test")
				.password("test5")
				.name("테스트5")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		userRepository.save(user3);
		Diary diary = Diary.builder()
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Diary createdDiary = diaryRepository.save(diary);
		Good good = Good.builder()
				.diary(createdDiary)
				.user(user2)
				.createDate(LocalDateTime.now())
				.build();
		Good good2 = Good.builder()
				.diary(createdDiary)
				.user(user3)
				.createDate(LocalDateTime.now())
				.build();
		Good createdGood = goodRepository.save(good);
		Good createdGood2 = goodRepository.save(good2);
		List<Good> list = new ArrayList<Good>();
		list.add(createdGood);
		list.add(createdGood2);
		diary.setGoodPushedUser(list);
		Timeline diaryTimeline = Timeline.builder()
				.type("good")
				.icon("thumbs-up")
				.good(good)
				.user(good.getUser())
				.saveDate(diary.getCreateDate())
				.build();
		Timeline createdTimeline = timelineRepository.save(diaryTimeline);
		Timeline diaryTimeline2 = Timeline.builder()
				.type("good")
				.icon("thumbs-up")
				.good(good2)
				.user(good2.getUser())
				.saveDate(diary.getCreateDate())
				.build();
		timelineRepository.save(diaryTimeline2);
		Timeline readTimeline = timelineRepository.findByGood(good);
		assertThat(createdTimeline, is(readTimeline));
	}
	
	@Test
	public void deleteDiaryTimeline() {
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
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Diary createdDiary = diaryRepository.save(diary);
		Timeline diaryTimeline = Timeline.builder()
				.type("diary")
				.icon("edit")
				.diary(createdDiary)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		Timeline createdTimeline = timelineRepository.save(diaryTimeline);
		timelineRepository.delete(createdTimeline);
		Optional<Timeline> deletedTimeline = timelineRepository.findById(createdTimeline.getId());
		assertFalse(deletedTimeline.isPresent());
	}
	
	@Test
	public void deleteDiaryAnswerTimeline() {
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
		Diary createdDiary = diaryRepository.save(diary);
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.writer(user)
				.diary(createdDiary)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer createdDiaryAnswer = diaryAnswerRepository.save(diaryAnswer);
		Timeline diaryAnswerTimeline = Timeline.builder()
				.type("diary answer")
				.icon("comment")
				.diaryAnswer(createdDiaryAnswer)
				.user(user)
				.saveDate(diaryAnswer.getCreateDate())
				.build();
		Timeline createdDiaryAnswerTimeline = timelineRepository.save(diaryAnswerTimeline);
		timelineRepository.delete(createdDiaryAnswerTimeline);
		Optional<Timeline> deletedTimeline = timelineRepository.findById(createdDiaryAnswerTimeline.getId());
		assertFalse(deletedTimeline.isPresent());
	}
	
	@Test
	public void deleteFreeBoardTimeline() {
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
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.badge("")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard createdFreeBoard = freeBoardRepository.save(freeBoard);
		Timeline freeBoardTimeline = Timeline.builder()
				.type("free board")
				.icon("list-alt")
				.freeBoard(createdFreeBoard)
				.user(user)
				.saveDate(freeBoard.getCreateDate())
				.build();
		Timeline createdFreeBoardTimeline = timelineRepository.save(freeBoardTimeline);
		timelineRepository.delete(createdFreeBoardTimeline);
		Optional<Timeline> deletedTimeline = timelineRepository.findById(createdFreeBoardTimeline.getId());
		assertFalse(deletedTimeline.isPresent());
	}
	
	@Test
	public void deleteFreeBoardAnswerTimeline() {
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
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.badge("")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard createdFreeBoard = freeBoardRepository.save(freeBoard);
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.writer(user)
				.freeBoard(createdFreeBoard)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardAnswer createdFreeBoardAnswer = freeBoardAnswerRepository.save(freeBoardAnswer);
		Timeline freeBoardAnswerTimeline = Timeline.builder()
				.type("freeBoard answer")
				.icon("comment")
				.freeBoardAnswer(createdFreeBoardAnswer)
				.user(user)
				.saveDate(freeBoardAnswer.getCreateDate())
				.build();
		Timeline createdFreeBoardAnswerTimeline = timelineRepository.save(freeBoardAnswerTimeline);
		timelineRepository.delete(createdFreeBoardAnswerTimeline);
		Optional<Timeline> deletedTimeline = timelineRepository.findById(createdFreeBoardAnswerTimeline.getId());
		assertFalse(deletedTimeline.isPresent());
	}
	
	@Test
	public void deleteGood() {
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
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Diary createdDiary = diaryRepository.save(diary);
		Good good = Good.builder()
				.diary(createdDiary)
				.user(user)
				.createDate(LocalDateTime.now())
				.build();
		Good createdGood = goodRepository.save(good);
		List<Good> list = new ArrayList<Good>();
		list.add(createdGood);
		diary.setGoodPushedUser(list);
		Timeline diaryTimeline = Timeline.builder()
				.type("good")
				.icon("thumbs-up")
				.good(good)
				.user(good.getUser())
				.saveDate(diary.getCreateDate())
				.build();
		Timeline createdTimeline = timelineRepository.save(diaryTimeline);
		Timeline readTimeline = timelineRepository.findByGood(good);
		assertThat(createdTimeline, is(readTimeline));
	}
	
	@Test
	public void findByUserPage() {
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
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.badge("")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard createdFreeBoard = freeBoardRepository.save(freeBoard);
		Timeline freeBoardTimeline = Timeline.builder()
				.type("free board")
				.icon("list-alt")
				.freeBoard(createdFreeBoard)
				.user(user)
				.saveDate(freeBoard.getCreateDate())
				.build();
		Timeline createdFreeBoardTimeline = timelineRepository.save(freeBoardTimeline);
		PageRequest pageRequest = PageRequest.of(0, 5, new Sort(Direction.DESC, "id"));
		Page<Timeline> timelinePage = timelineRepository.findByUser(user, pageRequest);
		List<Timeline> timelineList = timelinePage.getContent();
		assertThat(timelineList, hasItems(createdFreeBoardTimeline));
	}

}
