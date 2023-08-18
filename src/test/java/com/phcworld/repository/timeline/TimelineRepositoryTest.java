package com.phcworld.repository.timeline;

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
import com.phcworld.util.DiaryFactory;
import com.phcworld.util.FreeBoardFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

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
	private GoodRepository goodRepository;

	private User user;
	private Diary diary;
	private DiaryAnswer diaryAnswer;
	private FreeBoard freeBoard;
	private FreeBoardAnswer freeBoardAnswer;

	@Before
	public void setup(){
		user = User.builder()
				.id(1L)
				.build();
		diary = diaryRepository.save(DiaryFactory.getDiaryEntity(user));
		diaryAnswer = diaryAnswerRepository.save(DiaryFactory.getDiaryAnswerEntity(user, diary));
		freeBoard = freeBoardRepository.save(FreeBoardFactory.getFreeBoardEntity(user));
		freeBoardAnswer = freeBoardAnswerRepository.save(FreeBoardFactory.getFreeBoardAnswerEntity(user, freeBoard));
	}

	@Test
	public void createDiaryTimeline() {
		Timeline diaryTimeline = Timeline.builder()
				.type("diary")
				.icon("edit")
				.diary(diary)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		Timeline createdTimeline = timelineRepository.save(diaryTimeline);
		assertNotNull(createdTimeline);
	}
	
	@Test
	public void createDiaryAnswerTimeline() {
		Timeline diaryAnswerTimeline = Timeline.builder()
				.type("diary answer")
				.icon("comment")
				.diaryAnswer(diaryAnswer)
				.user(user)
				.saveDate(diaryAnswer.getCreateDate())
				.build();
		Timeline createdDiaryAnswerTimeline = timelineRepository.save(diaryAnswerTimeline);
		assertNotNull(createdDiaryAnswerTimeline);
	}
	
	@Test
	public void createFreeBoardTimeline() {
		Timeline freeBoardTimeline = Timeline.builder()
				.type("free board")
				.icon("list-alt")
				.freeBoard(freeBoard)
				.user(user)
				.saveDate(freeBoard.getCreateDate())
				.build();
		Timeline createdFreeBoardTimeline = timelineRepository.save(freeBoardTimeline);
		assertNotNull(createdFreeBoardTimeline);
	}
	
	@Test
	public void createFreeBoardAnswerTimeline() {
		Timeline freeBoardAnswerTimeline = Timeline.builder()
				.type("freeBoard answer")
				.icon("comment")
				.freeBoardAnswer(freeBoardAnswer)
				.user(user)
				.saveDate(freeBoardAnswer.getCreateDate())
				.build();
		Timeline createdFreeBoardAnswerTimeline = timelineRepository.save(freeBoardAnswerTimeline);
		assertNotNull(createdFreeBoardAnswerTimeline);
	}
	

	public void createGood() {
		Good good = Good.builder()
				.diary(diary)
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
		assertThat(createdTimeline).isEqualTo(readTimeline);
	}
	
	@Test
	public void readDiaryTimeline() {
		Timeline diaryTimeline = Timeline.builder()
				.type("diary")
				.icon("edit")
				.diary(diary)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		Timeline createdTimeline = timelineRepository.save(diaryTimeline);
		Timeline readTimeline = timelineRepository.findByDiary(diary);
		assertThat(createdTimeline).isEqualTo(readTimeline);
	}
	
	@Test
	public void readDiaryAnswerTimeline() {
		Timeline diaryAnswerTimeline = Timeline.builder()
				.type("diary answer")
				.icon("comment")
				.diaryAnswer(diaryAnswer)
				.user(user)
				.saveDate(diaryAnswer.getCreateDate())
				.build();
		Timeline createdDiaryAnswerTimeline = timelineRepository.save(diaryAnswerTimeline);
		Timeline readTimeline = timelineRepository.findByDiaryAnswer(diaryAnswer);
		assertThat(createdDiaryAnswerTimeline).isEqualTo(readTimeline);
	}
	
	@Test
	public void readFreeBoardTimeline() {
		Timeline freeBoardTimeline = Timeline.builder()
				.type("free board")
				.icon("list-alt")
				.freeBoard(freeBoard)
				.user(user)
				.saveDate(freeBoard.getCreateDate())
				.build();
		Timeline createdFreeBoardTimeline = timelineRepository.save(freeBoardTimeline);
		Timeline readTimeline = timelineRepository.findByFreeBoard(freeBoard);
		assertThat(createdFreeBoardTimeline).isEqualTo(readTimeline);
	}
	
	@Test
	public void readFreeBoardAnswerTimeline() {
		Timeline freeBoardAnswerTimeline = Timeline.builder()
				.type("freeBoard answer")
				.icon("comment")
				.freeBoardAnswer(freeBoardAnswer)
				.user(user)
				.saveDate(freeBoardAnswer.getCreateDate())
				.build();
		Timeline createdFreeBoardAnswerTimeline = timelineRepository.save(freeBoardAnswerTimeline);
		Timeline readTimeline = timelineRepository.findByFreeBoardAnswer(freeBoardAnswer);
		assertThat(createdFreeBoardAnswerTimeline).isEqualTo(readTimeline);
	}
	
	@Test
	public void readGood() {
		User user2 = User.builder()
				.id(2L)
				.build();
		Good good = Good.builder()
				.diary(diary)
				.user(user2)
				.createDate(LocalDateTime.now())
				.build();
		Good good2 = Good.builder()
				.diary(diary)
				.user(user)
				.createDate(LocalDateTime.now())
				.build();
		Good createdGood = goodRepository.save(good);
		Good createdGood2 = goodRepository.save(good2);
//		List<Good> list = new ArrayList<Good>();
//		list.add(createdGood);
//		list.add(createdGood2);
//		diary.setGoodPushedUser(list);
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
		assertThat(createdTimeline).isEqualTo(readTimeline);
	}
	
	@Test
	public void deleteDiaryTimeline() {
		Timeline diaryTimeline = Timeline.builder()
				.type("diary")
				.icon("edit")
				.diary(diary)
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
		Timeline diaryAnswerTimeline = Timeline.builder()
				.type("diary answer")
				.icon("comment")
				.diaryAnswer(diaryAnswer)
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
		Timeline freeBoardTimeline = Timeline.builder()
				.type("free board")
				.icon("list-alt")
				.freeBoard(freeBoard)
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
		Timeline freeBoardAnswerTimeline = Timeline.builder()
				.type("freeBoard answer")
				.icon("comment")
				.freeBoardAnswer(freeBoardAnswer)
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
		Good good = Good.builder()
				.diary(diary)
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
		assertThat(createdTimeline).isEqualTo(readTimeline);
	}
	
	@Test
	public void findByUserPage() {
		Timeline freeBoardTimeline = Timeline.builder()
				.type("free board")
				.icon("list-alt")
				.freeBoard(freeBoard)
				.user(user)
				.saveDate(freeBoard.getCreateDate())
				.build();
		Timeline createdFreeBoardTimeline = timelineRepository.save(freeBoardTimeline);
//		PageRequest pageRequest = PageRequest.of(0, 5, new Sort(Direction.DESC, "id"));
		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("id").descending());
		Page<Timeline> timelinePage = timelineRepository.findByUser(user, pageRequest);
		List<Timeline> timelineList = timelinePage.getContent();
		assertThat(timelineList).contains(createdFreeBoardTimeline);
	}

}
