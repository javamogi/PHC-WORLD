package com.phcworld.medium.repository.alert;

import com.phcworld.alert.infrasturcture.AlertEntity;
import com.phcworld.alert.domain.dto.AlertResponseDto;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.domain.board.Diary;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.embedded.PostInfo;
import com.phcworld.domain.good.Good;
import com.phcworld.medium.util.DiaryFactory;
import com.phcworld.medium.util.FreeBoardFactory;
import com.phcworld.alert.infrasturcture.AlertJpaJpaRepository;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.exception.model.CustomException;
import com.phcworld.repository.answer.DiaryAnswerRepository;
import com.phcworld.answer.infrastructure.FreeBoardAnswerJpaRepository;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.freeboard.infrastructure.FreeBoardJpaRepository;
import com.phcworld.repository.good.GoodRepository;
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

import javax.persistence.EntityManager;
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
public class AlertJpaRepositoryTest {
	
	@Autowired
	private AlertJpaJpaRepository alertJpaRepository;
	
	@Autowired
	private DiaryRepository diaryRepository;
	
	@Autowired
	private DiaryAnswerRepository diaryAnswerRepository;
	
	@Autowired
	private FreeBoardJpaRepository freeBoardRepository;
	
	@Autowired
	private FreeBoardAnswerJpaRepository freeBoardAnswerRepository;
	
	@Autowired
	private GoodRepository goodRepository;

	private UserEntity user;
	private Diary diary;

	private DiaryAnswer diaryAnswer;

	private FreeBoardEntity freeBoard;

	private FreeBoardAnswerEntity freeBoardAnswer;

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
	public void createDiaryAnswerAlert() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
//				.type("Diary")
//				.diaryAnswer(diaryAnswer)
				.postInfo(postInfo)
				.registerUser(diaryAnswer.getWriter())
				.postWriter(diaryAnswer.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.isRead(false)
				.build();
		AlertEntity createdAlert = alertJpaRepository.save(alert);
		assertNotNull(createdAlert);
	}
	
	@Test
	public void createFreeBoardAnswerAlert() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
//				.type("FreeBoard")
//				.freeBoardAnswer(freeBoardAnswer)
				.postInfo(postInfo)
				.registerUser(freeBoardAnswer.getWriter())
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(LocalDateTime.now())
				.isRead(false)
				.build();
		AlertEntity createdAlert = alertJpaRepository.save(alert);
		assertNotNull(createdAlert);
	}
	
	@Test
	public void createGoodAlert() {
		UserEntity user2 = UserEntity.builder()
				.id(2L)
				.build();
		Good good = Good.builder()
				.diary(diary)
				.user(user2)
				.createDate(LocalDateTime.now())
				.build();
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
//				.type("Diary")
//				.good(good)
				.postInfo(postInfo)
				.registerUser(good.getUser())
				.postWriter(good.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		AlertEntity createdAlert = alertJpaRepository.save(alert);
		assertNotNull(createdAlert);
	}
	
	@Test
	public void findByPostWriterPage() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
//				.type("FreeBoard")
//				.freeBoardAnswer(freeBoardAnswer)
				.postInfo(postInfo)
				.registerUser(freeBoardAnswer.getWriter())
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		AlertEntity createdAlert = alertJpaRepository.save(alert);
		PostInfo postInfo2 = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.build();
		AlertEntity alert2 = AlertEntity.builder()
//				.type("Diary")
//				.diaryAnswer(diaryAnswer)
				.postInfo(postInfo2)
				.registerUser(diaryAnswer.getWriter())
				.postWriter(diaryAnswer.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		AlertEntity createdAlert2 = alertJpaRepository.save(alert2);

//		PageRequest pageRequest = PageRequest.of(0, 5, new Sort(Direction.DESC, "id"));
		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("id").descending());
		Page<AlertEntity> page = alertJpaRepository.findByPostWriter(user, pageRequest);
		List<AlertEntity> list = page.getContent();
		assertThat(list).contains(createdAlert).contains(createdAlert2);
	}

	@Test
	public void findByPostWriter() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
				.postInfo(postInfo)
				.registerUser(freeBoardAnswer.getWriter())
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		alertJpaRepository.save(alert);
		PostInfo postInfo2 = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.build();
		AlertEntity alert2 = AlertEntity.builder()
				.postInfo(postInfo2)
				.registerUser(diaryAnswer.getWriter())
				.postWriter(diaryAnswer.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		alertJpaRepository.save(alert2);

		List<AlertResponseDto> list = alertJpaRepository.findAlertListByPostWriter(user)
				.stream()
				.map(AlertResponseDto::of)
				.collect(Collectors.toList());
		assertThat(list.get(0).getPostTitle()).isEqualTo(diary.getTitle());
		assertThat(list.get(1).getPostTitle()).isEqualTo(freeBoard.getTitle());
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
				.createDate(LocalDateTime.now())
				.build();
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
//				.type("Diary")
//				.good(good)
				.postInfo(postInfo)
				.registerUser(good.getUser())
				.postWriter(good.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		AlertEntity createdAlert = alertJpaRepository.save(alert);
		alertJpaRepository.delete(createdAlert);
		Optional<AlertEntity> selectAlert = alertJpaRepository.findById(createdAlert.getId());
		assertFalse(selectAlert.isPresent());
	}

	@Test(expected = CustomException.class)
	public void deleteGoodAlert() {
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
				.createDate(LocalDateTime.now())
				.build();
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
				.postInfo(postInfo)
				.registerUser(good.getUser())
				.postWriter(good.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		AlertEntity createdAlert = alertJpaRepository.save(alert);
		em.clear();
		alertJpaRepository.deleteAlert(SaveType.DIARY, diary.getId());

		alertJpaRepository.findById(createdAlert.getId())
				.orElseThrow(() -> new CustomException("400", "게시물 정보가 없습니다."));
	}
	
	@Test
	public void deleteDiaryAnswerAlert() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
//				.type("Diary")
//				.diaryAnswer(diaryAnswer)
				.postInfo(postInfo)
				.registerUser(diaryAnswer.getWriter())
				.postWriter(diaryAnswer.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		AlertEntity createdAlert = alertJpaRepository.save(alert);
		alertJpaRepository.delete(createdAlert);
		Optional<AlertEntity> selectAlert = alertJpaRepository.findById(createdAlert.getId());
		assertFalse(selectAlert.isPresent());
	}
	
	@Test
	public void deleteFreeBoardAnswerAlert() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
//				.type("FreeBoard")
//				.freeBoardAnswer(freeBoardAnswer)
				.postInfo(postInfo)
				.registerUser(freeBoardAnswer.getWriter())
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		AlertEntity createdAlert = alertJpaRepository.save(alert);
		alertJpaRepository.delete(createdAlert);
		Optional<AlertEntity> selectAlert = alertJpaRepository.findById(createdAlert.getId());
		assertFalse(selectAlert.isPresent());
	}
	
	@Test
	public void findDiaryAlert() {
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
				.createDate(LocalDateTime.now())
				.build();
		Good createdGood = goodRepository.save(good);
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.GOOD)
				.postId(createdGood.getId())
				.redirectId(createdGood.getDiary().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
//				.type("Diary")
//				.good(createdGood)
				.postInfo(postInfo)
				.registerUser(good.getUser())
				.postWriter(createdGood.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		AlertEntity createdAlert = alertJpaRepository.save(alert);
		AlertEntity selectAlert = alertJpaRepository.findByPostInfo(postInfo)
				.orElseThrow(() -> new CustomException("400", "알림이 존재하지 않습니다."));
		assertThat(createdAlert).isEqualTo(selectAlert);
	}
	
	@Test
	public void findDiaryAnswerAlert() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
//				.type("Diary")
//				.diaryAnswer(diaryAnswer)
				.postInfo(postInfo)
				.registerUser(diaryAnswer.getWriter())
				.postWriter(diaryAnswer.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		AlertEntity createdAlert = alertJpaRepository.save(alert);
		AlertEntity selectAlert = alertJpaRepository.findByPostInfo(postInfo)
				.orElseThrow(() -> new CustomException("400", "알림이 존재하지 않습니다."));
		assertThat(createdAlert).isEqualTo(selectAlert);
	}
	
	@Test
	public void findFreeBoardAnswerAlert() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
//				.type("FreeBoard")
//				.freeBoardAnswer(freeBoardAnswer)
				.postInfo(postInfo)
				.registerUser(freeBoardAnswer.getWriter())
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		AlertEntity createdAlert = alertJpaRepository.save(alert);
		AlertEntity selectAlert = alertJpaRepository.findByPostInfo(postInfo)
				.orElseThrow(() -> new CustomException("400", "알림이 존재하지 않습니다."));
		assertThat(createdAlert).isEqualTo(selectAlert);
	}
	
}
