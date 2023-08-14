package com.phcworld.repository.alert;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.good.Good;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class AlertRepositoryTest {
	
	@Autowired
	private AlertRepository alertRepository;
	
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
	public void createDiaryAnswerAlert() {
		Alert alert = Alert.builder()
				.type("Diary")
				.diaryAnswer(diaryAnswer)
				.postWriter(diaryAnswer.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		assertNotNull(createdAlert);
	}
	
	@Test
	public void createFreeBoardAnswerAlert() {
		Alert alert = Alert.builder()
				.type("FreeBoard")
				.freeBoardAnswer(freeBoardAnswer)
				.postWriter(freeBoardAnswer.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		assertNotNull(createdAlert);
	}
	
	@Test
	public void createGoodAlert() {
		User user2 = User.builder()
				.id(2L)
				.build();
		Good good = Good.builder()
				.diary(diary)
				.user(user2)
				.createDate(LocalDateTime.now())
				.build();
		Alert alert = Alert.builder()
				.type("Diary")
				.good(good)
				.postWriter(good.getUser())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		assertNotNull(createdAlert);
	}
	
	@Test
	public void findByPostWriterPage() {
		Alert alert = Alert.builder()
				.type("FreeBoard")
				.freeBoardAnswer(freeBoardAnswer)
				.postWriter(freeBoardAnswer.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		Alert alert2 = Alert.builder()
				.type("Diary")
				.diaryAnswer(diaryAnswer)
				.postWriter(diaryAnswer.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert2 = alertRepository.save(alert2);
//		PageRequest pageRequest = PageRequest.of(0, 5, new Sort(Direction.DESC, "id"));
		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("id").descending());
		Page<Alert> page = alertRepository.findByPostWriter(user, pageRequest);
		List<Alert> list = page.getContent();
		assertThat(list).contains(createdAlert).contains(createdAlert2);
	}
	
	@Test
	public void deleteGoodAlert() {
		User user2 = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Good good = Good.builder()
				.diary(diary)
				.user(user2)
				.createDate(LocalDateTime.now())
				.build();
		Alert alert = Alert.builder()
				.type("Diary")
				.good(good)
				.postWriter(good.getUser())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		alertRepository.delete(createdAlert);
		Optional<Alert> selectAlert = alertRepository.findById(createdAlert.getId());
		assertFalse(selectAlert.isPresent());
	}
	
	@Test
	public void deleteDiaryAnswerAlert() {
		Alert alert = Alert.builder()
				.type("Diary")
				.diaryAnswer(diaryAnswer)
				.postWriter(diaryAnswer.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		alertRepository.delete(createdAlert);
		Optional<Alert> selectAlert = alertRepository.findById(createdAlert.getId());
		assertFalse(selectAlert.isPresent());
	}
	
	@Test
	public void deleteFreeBoardAnswerAlert() {
		Alert alert = Alert.builder()
				.type("FreeBoard")
				.freeBoardAnswer(freeBoardAnswer)
				.postWriter(freeBoardAnswer.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		alertRepository.delete(createdAlert);
		Optional<Alert> selectAlert = alertRepository.findById(createdAlert.getId());
		assertFalse(selectAlert.isPresent());
	}
	
	@Test
	public void findDiaryAlert() {
		User user2 = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Good good = Good.builder()
				.diary(diary)
				.user(user2)
				.createDate(LocalDateTime.now())
				.build();
		Good createdGood = goodRepository.save(good);
		Alert alert = Alert.builder()
				.type("Diary")
				.good(createdGood)
				.postWriter(createdGood.getUser())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		Alert selectAlert = alertRepository.findByGood(good);
		assertThat(createdAlert).isEqualTo(selectAlert);
	}
	
	@Test
	public void findDiaryAnswerAlert() {
		Alert alert = Alert.builder()
				.type("Diary")
				.diaryAnswer(diaryAnswer)
				.postWriter(diaryAnswer.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		Alert selectAlert = alertRepository.findByDiaryAnswer(diaryAnswer);
		assertThat(createdAlert).isEqualTo(selectAlert);
	}
	
	@Test
	public void findFreeBoardAnswerAlert() {
		Alert alert = Alert.builder()
				.type("FreeBoard")
				.freeBoardAnswer(freeBoardAnswer)
				.postWriter(freeBoardAnswer.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		Alert selectAlert = alertRepository.findByFreeBoardAnswer(freeBoardAnswer);
		assertThat(createdAlert).isEqualTo(selectAlert);
	}
	
}
