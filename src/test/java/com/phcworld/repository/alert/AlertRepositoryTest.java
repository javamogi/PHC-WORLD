package com.phcworld.repository.alert;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.alert.dto.AlertResponseDto;
import com.phcworld.domain.alert.dto.AlertSelectDto;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.exception.model.CustomException;
import com.phcworld.repository.answer.DiaryAnswerRepository;
import com.phcworld.repository.answer.FreeBoardAnswerRepository;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.repository.board.FreeBoardRepository;
import com.phcworld.repository.good.GoodRepository;
import com.phcworld.util.DiaryFactory;
import com.phcworld.util.FreeBoardFactory;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
@Slf4j
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
//				.type("Diary")
//				.diaryAnswer(diaryAnswer)
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.registerUser(diaryAnswer.getWriter())
				.postWriter(diaryAnswer.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.read(false)
				.build();
		Alert createdAlert = alertRepository.save(alert);
		assertNotNull(createdAlert);
	}
	
	@Test
	public void createFreeBoardAnswerAlert() {
		Alert alert = Alert.builder()
//				.type("FreeBoard")
//				.freeBoardAnswer(freeBoardAnswer)
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.registerUser(freeBoardAnswer.getWriter())
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(LocalDateTime.now())
				.read(false)
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
//				.type("Diary")
//				.good(good)
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.registerUser(good.getUser())
				.postWriter(good.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		assertNotNull(createdAlert);
	}
	
	@Test
	public void findByPostWriterPage() {
		Alert alert = Alert.builder()
//				.type("FreeBoard")
//				.freeBoardAnswer(freeBoardAnswer)
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.registerUser(freeBoardAnswer.getWriter())
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		Alert alert2 = Alert.builder()
//				.type("Diary")
//				.diaryAnswer(diaryAnswer)
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.registerUser(diaryAnswer.getWriter())
				.postWriter(diaryAnswer.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert2 = alertRepository.save(alert2);

		List<AlertResponseDto> tmp = alertRepository.findAlertListByPostWriter(user)
				.stream()
				.map(AlertResponseDto::of)
				.collect(Collectors.toList());
		log.info("tmp : {}", tmp);
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
//				.type("Diary")
//				.good(good)
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.registerUser(good.getUser())
				.postWriter(good.getDiary().getWriter())
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
//				.type("Diary")
//				.diaryAnswer(diaryAnswer)
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.registerUser(diaryAnswer.getWriter())
				.postWriter(diaryAnswer.getDiary().getWriter())
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
//				.type("FreeBoard")
//				.freeBoardAnswer(freeBoardAnswer)
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.registerUser(freeBoardAnswer.getWriter())
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
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
//				.type("Diary")
//				.good(createdGood)
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.registerUser(good.getUser())
				.postWriter(createdGood.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		Alert selectAlert = alertRepository.findBySaveTypeAndPostIdAndRegisterUser(SaveType.GOOD, good.getId(), good.getUser())
				.orElseThrow(() -> new CustomException("400", "알림이 존재하지 않습니다."));
		assertThat(createdAlert).isEqualTo(selectAlert);
	}
	
	@Test
	public void findDiaryAnswerAlert() {
		Alert alert = Alert.builder()
//				.type("Diary")
//				.diaryAnswer(diaryAnswer)
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.registerUser(diaryAnswer.getWriter())
				.postWriter(diaryAnswer.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		Alert selectAlert = alertRepository.findBySaveTypeAndPostIdAndRegisterUser(SaveType.DIARY_ANSWER, diaryAnswer.getId(), diaryAnswer.getWriter())
				.orElseThrow(() -> new CustomException("400", "알림이 존재하지 않습니다."));
		assertThat(createdAlert).isEqualTo(selectAlert);
	}
	
	@Test
	public void findFreeBoardAnswerAlert() {
		Alert alert = Alert.builder()
//				.type("FreeBoard")
//				.freeBoardAnswer(freeBoardAnswer)
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.registerUser(freeBoardAnswer.getWriter())
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert createdAlert = alertRepository.save(alert);
		Alert selectAlert = alertRepository.findBySaveTypeAndPostIdAndRegisterUser(SaveType.FREE_BOARD_ANSWER, freeBoardAnswer.getId(), freeBoardAnswer.getWriter())
				.orElseThrow(() -> new CustomException("400", "알림이 존재하지 않습니다."));
		assertThat(createdAlert).isEqualTo(selectAlert);
	}
	
}
