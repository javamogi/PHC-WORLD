package com.phcworld.medium.repository.timeline;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.embedded.PostInfo;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.medium.util.DiaryFactory;
import com.phcworld.medium.util.FreeBoardFactory;
import com.phcworld.repository.timeline.TimelineRepository;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.exception.model.CustomException;
import com.phcworld.repository.answer.DiaryAnswerRepository;
import com.phcworld.repository.answer.FreeBoardAnswerRepository;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.repository.board.FreeBoardRepository;
import com.phcworld.repository.good.GoodRepository;
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

import javax.persistence.EntityManager;
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

	private UserEntity user;
	private Diary diary;
	private DiaryAnswer diaryAnswer;
	private FreeBoard freeBoard;
	private FreeBoardAnswer freeBoardAnswer;

	@Autowired
	private EntityManager em;

	@Before
	public void setup(){
		user = UserEntity.builder()
				.id(1L)
				.build();
		diary = diaryRepository.save(DiaryFactory.getDiaryEntity(user));
		diaryAnswer = diaryAnswerRepository.save(DiaryFactory.getDiaryAnswerEntity(user, diary));
		freeBoard = freeBoardRepository.save(FreeBoardFactory.getFreeBoardEntity(user));
		freeBoardAnswer = freeBoardAnswerRepository.save(FreeBoardFactory.getFreeBoardAnswerEntity(user, freeBoard));
	}

	@Test
	public void createDiaryTimeline() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY)
				.postId(diary.getId())
				.redirectId(diary.getId())
				.build();
		Timeline diaryTimeline = Timeline.builder()
//				.type("diary")
//				.icon("edit")
//				.diary(diary)
				.postInfo(postInfo)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		Timeline createdTimeline = timelineRepository.save(diaryTimeline);
		assertNotNull(createdTimeline);
	}
	
	@Test
	public void createDiaryAnswerTimeline() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.build();

		Timeline diaryAnswerTimeline = Timeline.builder()
//				.type("diary answer")
//				.icon("comment")
//				.diaryAnswer(diaryAnswer)
				.postInfo(postInfo)
				.user(user)
				.saveDate(diaryAnswer.getCreateDate())
				.build();
		Timeline createdDiaryAnswerTimeline = timelineRepository.save(diaryAnswerTimeline);
		assertNotNull(createdDiaryAnswerTimeline);
	}
	
	@Test
	public void createFreeBoardTimeline() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD)
				.postId(freeBoard.getId())
				.redirectId(freeBoard.getId())
				.build();
		Timeline freeBoardTimeline = Timeline.builder()
//				.type("free board")
//				.icon("list-alt")
//				.freeBoard(freeBoard)
				.postInfo(postInfo)
				.user(user)
				.saveDate(freeBoard.getCreateDate())
				.build();
		Timeline createdFreeBoardTimeline = timelineRepository.save(freeBoardTimeline);
		assertNotNull(createdFreeBoardTimeline);
	}
	
	@Test
	public void createFreeBoardAnswerTimeline() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		Timeline freeBoardAnswerTimeline = Timeline.builder()
//				.type("freeBoard answer")
//				.icon("comment")
//				.freeBoardAnswer(freeBoardAnswer)
				.postInfo(postInfo)
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
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.build();
		Timeline diaryTimeline = Timeline.builder()
//				.type("good")
//				.icon("thumbs-up")
//				.good(newGood)
				.postInfo(postInfo)
				.user(newGood.getUser())
				.saveDate(diary.getCreateDate())
				.build();
		Timeline createdTimeline = timelineRepository.save(diaryTimeline);
		Timeline readTimeline = timelineRepository.findById(createdTimeline.getId())
				.orElseThrow(() -> new CustomException("400", "게시물 정보가 없습니다."));
		assertThat(createdTimeline).isEqualTo(readTimeline);
	}
	
	@Test
	public void readDiaryTimeline() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY)
				.postId(diary.getId())
				.redirectId(diary.getId())
				.build();
		Timeline diaryTimeline = Timeline.builder()
//				.type("diary")
//				.icon("edit")
//				.diary(diary)
				.postInfo(postInfo)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		Timeline createdTimeline = timelineRepository.save(diaryTimeline);
		Timeline readTimeline = timelineRepository.findById(createdTimeline.getId())
				.orElseThrow(() -> new CustomException("400", "게시물 정보가 없습니다."));
		assertThat(createdTimeline).isEqualTo(readTimeline);
	}
	
	@Test
	public void readDiaryAnswerTimeline() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.build();
		Timeline diaryAnswerTimeline = Timeline.builder()
//				.type("diary answer")
//				.icon("comment")
//				.diaryAnswer(diaryAnswer)
				.postInfo(postInfo)
				.user(user)
				.saveDate(diaryAnswer.getCreateDate())
				.build();
		Timeline createdDiaryAnswerTimeline = timelineRepository.save(diaryAnswerTimeline);
		Timeline readTimeline = timelineRepository.findById(createdDiaryAnswerTimeline.getId())
				.orElseThrow(() -> new CustomException("400", "게시물 정보가 없습니다."));
		assertThat(createdDiaryAnswerTimeline).isEqualTo(readTimeline);
	}
	
	@Test
	public void readFreeBoardTimeline() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD)
				.postId(freeBoard.getId())
				.redirectId(freeBoard.getId())
				.build();
		Timeline freeBoardTimeline = Timeline.builder()
//				.type("free board")
//				.icon("list-alt")
//				.freeBoard(freeBoard)
				.postInfo(postInfo)
				.user(user)
				.saveDate(freeBoard.getCreateDate())
				.build();
		Timeline createdFreeBoardTimeline = timelineRepository.save(freeBoardTimeline);
		Timeline readTimeline = timelineRepository.findById(createdFreeBoardTimeline.getId())
				.orElseThrow(() -> new CustomException("400", "게시물 정보가 없습니다."));
		assertThat(createdFreeBoardTimeline).isEqualTo(readTimeline);
	}
	
	@Test
	public void readFreeBoardAnswerTimeline() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		Timeline freeBoardAnswerTimeline = Timeline.builder()
//				.type("freeBoard answer")
//				.icon("comment")
//				.freeBoardAnswer(freeBoardAnswer)
				.postInfo(postInfo)
				.user(user)
				.saveDate(freeBoardAnswer.getCreateDate())
				.build();
		Timeline createdFreeBoardAnswerTimeline = timelineRepository.save(freeBoardAnswerTimeline);
		Timeline readTimeline = timelineRepository.findById(createdFreeBoardAnswerTimeline.getId())
				.orElseThrow(() -> new CustomException("400", "게시물 정보가 없습니다."));
		assertThat(createdFreeBoardAnswerTimeline).isEqualTo(readTimeline);
	}
	
	@Test
	public void readGood() {
		UserEntity user2 = UserEntity.builder()
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
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.GOOD)
				.postId(createdGood.getId())
				.redirectId(createdGood.getDiary().getId())
				.build();
		Timeline diaryTimeline = Timeline.builder()
//				.type("good")
//				.icon("thumbs-up")
//				.good(good)
				.postInfo(postInfo)
				.user(good.getUser())
				.saveDate(diary.getCreateDate())
				.build();
		timelineRepository.save(diaryTimeline);
		PostInfo postInfo2 = PostInfo.builder()
				.saveType(SaveType.GOOD)
				.postId(createdGood2.getId())
				.redirectId(createdGood2.getDiary().getId())
				.build();
		Timeline diaryTimeline2 = Timeline.builder()
//				.type("good")
//				.icon("thumbs-up")
//				.good(good2)
				.user(createdGood2.getUser())
				.saveDate(diary.getCreateDate())
				.build();
		Timeline createdTimeline2 = timelineRepository.save(diaryTimeline2);
		Timeline readTimeline = timelineRepository.findById(createdTimeline2.getId())
				.orElseThrow(() -> new CustomException("400", "게시물 정보가 없습니다."));
		assertThat(createdTimeline2).isEqualTo(readTimeline);
	}
	
	@Test
	public void deleteDiaryTimeline() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY)
				.postId(diary.getId())
				.redirectId(diary.getId())
				.build();
		Timeline diaryTimeline = Timeline.builder()
//				.type("diary")
//				.icon("edit")
//				.diary(diary)
				.postInfo(postInfo)
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
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.build();
		Timeline diaryAnswerTimeline = Timeline.builder()
//				.type("diary answer")
//				.icon("comment")
//				.diaryAnswer(diaryAnswer)
				.postInfo(postInfo)
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
		Timeline createdFreeBoardTimeline = timelineRepository.save(freeBoardTimeline);
		em.clear();
		timelineRepository.deleteTimeline(SaveType.FREE_BOARD, freeBoard.getId());
		Optional<Timeline> deletedTimeline = timelineRepository.findById(createdFreeBoardTimeline.getId());
		assertFalse(deletedTimeline.isPresent());
	}
	
	@Test
	public void deleteFreeBoardAnswerTimeline() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		Timeline freeBoardAnswerTimeline = Timeline.builder()
//				.type("freeBoard answer")
//				.icon("comment")
//				.freeBoardAnswer(freeBoardAnswer)
				.postInfo(postInfo)
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
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.build();
		Timeline diaryTimeline = Timeline.builder()
//				.type("good")
//				.icon("thumbs-up")
//				.good(good)
				.postInfo(postInfo)
				.user(good.getUser())
				.saveDate(diary.getCreateDate())
				.build();
		Timeline createdTimeline = timelineRepository.save(diaryTimeline);
		Timeline readTimeline = timelineRepository.findById(createdTimeline.getId())
				.orElseThrow(() -> new CustomException("400", "게시물 정보가 없습니다."));
		assertThat(createdTimeline).isEqualTo(readTimeline);
	}

	@Test(expected = CustomException.class)
	public void deleteGoodTimeline() {
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.createDate(LocalDateTime.now())
				.build();
		Good createdGood = goodRepository.save(good);
		List<Good> list = new ArrayList<Good>();
		list.add(createdGood);
		diary.setGoodPushedUser(list);
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.build();
		Timeline diaryTimeline = Timeline.builder()
				.postInfo(postInfo)
				.user(good.getUser())
				.saveDate(diary.getCreateDate())
				.build();
		Timeline createdTimeline = timelineRepository.save(diaryTimeline);
		em.clear();
		timelineRepository.deleteTimeline(SaveType.DIARY, diary.getId());

		timelineRepository.findById(createdTimeline.getId())
				.orElseThrow(() -> new CustomException("400", "게시물 정보가 없습니다."));
	}
	
	@Test
	public void findByUserPage() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD)
				.postId(freeBoard.getId())
				.redirectId(freeBoard.getId())
				.build();
		Timeline freeBoardTimeline = Timeline.builder()
//				.type("free board")
//				.icon("list-alt")
//				.freeBoard(freeBoard)
				.postInfo(postInfo)
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
