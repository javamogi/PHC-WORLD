package com.phcworld.repository.alert;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
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
	
	@Test
	public void createDiaryAnswerAlert() {
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
		Diary createdDiary = diaryRepository.save(diary);
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.writer(user)
				.diary(createdDiary)
				.contents("test")
				.build();
		DiaryAnswer createdDiaryAnswer = diaryAnswerRepository.save(diaryAnswer);
		Alert alert = Alert.builder()
				.type("Diary")
				.diaryAnswer(createdDiaryAnswer)
				.postWriter(createdDiary.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		assertNotNull(createdAlert);
	}
	
	@Test
	public void createFreeBoardAnswerAlert() {
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
				.build();
		FreeBoardAnswer createdFreeBoardAnswer = freeBoardAnswerRepository.save(freeBoardAnswer);
		Alert alert = Alert.builder()
				.type("FreeBoard")
				.freeBoardAnswer(createdFreeBoardAnswer)
				.postWriter(createdFreeBoard.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		assertNotNull(createdAlert);
	}
	
	@Test
	public void createGoodAlert() {
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
				.build();
		FreeBoardAnswer createdFreeBoardAnswer = freeBoardAnswerRepository.save(freeBoardAnswer);
		Alert alert = Alert.builder()
				.type("FreeBoard")
				.freeBoardAnswer(createdFreeBoardAnswer)
				.postWriter(createdFreeBoard.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Diary createdDiary = diaryRepository.save(diary);
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.writer(user)
				.diary(createdDiary)
				.contents("test")
				.build();
		DiaryAnswer createdDiaryAnswer = diaryAnswerRepository.save(diaryAnswer);
		Alert alert2 = Alert.builder()
				.type("Diary")
				.diaryAnswer(createdDiaryAnswer)
				.postWriter(createdDiary.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert2 = alertRepository.save(alert2);
		PageRequest pageRequest = PageRequest.of(0, 5, new Sort(Direction.DESC, "id"));
		Page<Alert> page = alertRepository.findByPostWriter(user, pageRequest);
		List<Alert> list = page.getContent();
		assertThat(list, hasItems(createdAlert, createdAlert2));
	}
	
	@Test
	public void deleteGoodAlert() {
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
		Diary diary = Diary.builder()
				.id(1L)
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
		Diary createdDiary = diaryRepository.save(diary);
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.writer(user)
				.diary(createdDiary)
				.contents("test")
				.build();
		DiaryAnswer createdDiaryAnswer = diaryAnswerRepository.save(diaryAnswer);
		Alert alert = Alert.builder()
				.type("Diary")
				.diaryAnswer(createdDiaryAnswer)
				.postWriter(createdDiary.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		alertRepository.delete(createdAlert);
		Optional<Alert> selectAlert = alertRepository.findById(createdAlert.getId());
		assertFalse(selectAlert.isPresent());
	}
	
	@Test
	public void deleteFreeBoardAnswerAlert() {
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
				.build();
		FreeBoardAnswer createdFreeBoardAnswer = freeBoardAnswerRepository.save(freeBoardAnswer);
		Alert alert = Alert.builder()
				.type("FreeBoard")
				.freeBoardAnswer(createdFreeBoardAnswer)
				.postWriter(createdFreeBoard.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		alertRepository.delete(createdAlert);
		Optional<Alert> selectAlert = alertRepository.findById(createdAlert.getId());
		assertFalse(selectAlert.isPresent());
	}
	
	@Test
	public void findDiaryAlert() {
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
		Diary diary = Diary.builder()
				.id(1L)
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
		Good createdGood = goodRepository.save(good);
		Alert alert = Alert.builder()
				.type("Diary")
				.good(createdGood)
				.postWriter(createdGood.getUser())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		Alert selectAlert = alertRepository.findByGood(good);
		assertThat(createdAlert, is(selectAlert));
	}
	
	@Test
	public void findDiaryAnswerAlert() {
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
		Diary createdDiary = diaryRepository.save(diary);
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.writer(user)
				.diary(createdDiary)
				.contents("test")
				.build();
		DiaryAnswer createdDiaryAnswer = diaryAnswerRepository.save(diaryAnswer);
		Alert alert = Alert.builder()
				.type("Diary")
				.diaryAnswer(createdDiaryAnswer)
				.postWriter(createdDiary.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		Alert selectAlert = alertRepository.findByDiaryAnswer(createdDiaryAnswer);
		assertThat(createdAlert, is(selectAlert));
	}
	
	@Test
	public void findFreeBoardAnswerAlert() {
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
				.build();
		FreeBoardAnswer createdFreeBoardAnswer = freeBoardAnswerRepository.save(freeBoardAnswer);
		Alert alert = Alert.builder()
				.type("FreeBoard")
				.freeBoardAnswer(createdFreeBoardAnswer)
				.postWriter(createdFreeBoard.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		Alert selectAlert = alertRepository.findByFreeBoardAnswer(createdFreeBoardAnswer);
		assertThat(createdAlert, is(selectAlert));
	}
	
}
