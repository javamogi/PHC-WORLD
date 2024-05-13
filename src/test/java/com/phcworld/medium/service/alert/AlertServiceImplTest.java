package com.phcworld.medium.service.alert;

import com.phcworld.alert.infrasturcture.AlertEntity;
import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.alert.domain.dto.AlertResponseDto;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.embedded.PostInfo;
import com.phcworld.domain.good.Good;
import com.phcworld.alert.service.AlertServiceImpl;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class AlertServiceImplTest {
	
	@Mock
	private AlertServiceImpl alertService;

	private UserEntity user;
	private Diary diary;

	private DiaryAnswer diaryAnswer;

	private FreeBoardEntity freeBoard;
	private FreeBoardAnswerEntity freeBoardAnswer;

	@Before
	public void setup(){
		user = UserEntity.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		diaryAnswer = DiaryAnswer.builder()
				.id(1L)
				.writer(user)
				.diary(diary)
				.contents("diaryAnswer")
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
				.build();
		when(alertService.createAlert(diaryAnswer))
		.thenReturn(alert);
		AlertEntity alertCreated = alertService.createAlert(diaryAnswer);
		assertThat(diaryAnswer.getId()).isEqualTo(alertCreated.getPostInfo().getPostId());
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
				.build();
		when(alertService.createAlert(freeBoardAnswer))
		.thenReturn(alert);
		AlertEntity alertCreated = alertService.createAlert(freeBoardAnswer);
		assertThat(freeBoardAnswer.getId()).isEqualTo(alertCreated.getPostInfo().getPostId());
	}
	
	@Test
	public void createGoodAlert() {
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
		when(alertService.createAlert(good))
		.thenReturn(alert);
		AlertEntity alertCreated = alertService.createAlert(good);
		assertThat(good.getId()).isEqualTo(alertCreated.getPostInfo().getPostId());
	}
	
	@Test
	public void getOneAlert() {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		AlertEntity alert = AlertEntity.builder()
				.id(1L)
//				.type("FreeBoard")
//				.freeBoardAnswer(freeBoardAnswer)
				.postInfo(postInfo)
				.registerUser(freeBoardAnswer.getWriter())
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		when(alertService.getOneAlert(alert.getId()))
		.thenReturn(alert);
		AlertEntity alertCreated = alertService.getOneAlert(alert.getId());
		assertThat(alert).isEqualTo(alertCreated);
	}
	
	@Test
	public void findPageRequestAlertByPostUser() throws Exception {
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.build();
		AlertEntity diaryAnswerAlert = AlertEntity.builder()
//				.type("Diary")
//				.diaryAnswer(diaryAnswer)
				.postInfo(postInfo)
				.registerUser(diaryAnswer.getWriter())
				.postWriter(diaryAnswer.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		PostInfo postInfo2 = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		AlertEntity freeBoardAnswerAlert = AlertEntity.builder()
//				.type("Free Board")
//				.freeBoardAnswer(freeBoardAnswer)
				.postInfo(postInfo2)
				.registerUser(freeBoardAnswer.getWriter())
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		List<AlertEntity> list = new ArrayList<AlertEntity>();
		list.add(diaryAnswerAlert);
		list.add(freeBoardAnswerAlert);
		when(alertService.findListAlertByPostUser(user))
		.thenReturn(list);
		List<AlertEntity> alertList = alertService.findListAlertByPostUser(user);
		assertThat(alertList)
				.contains(diaryAnswerAlert)
				.contains(freeBoardAnswerAlert);
	}

	@Test
	public void findAlertListByPostUser() throws Exception {

		AlertResponseDto diaryAnswerAlert = AlertResponseDto.builder()
				.userName(diaryAnswer.getWriter().getName())
				.isGood(false)
				.postTitle(diaryAnswer.getDiary().getTitle())
				.formattedCreateDate(diaryAnswer.getFormattedCreateDate())
				.build();

		AlertResponseDto freeBoardAnswerAlert = AlertResponseDto.builder()
				.userName(freeBoardAnswer.getWriter().getName())
				.isGood(false)
				.postTitle(freeBoardAnswer.getFreeBoard().getTitle())
//				.formattedCreateDate(freeBoardAnswer.getFormattedCreateDate())
				.build();

		List<AlertResponseDto> list = new ArrayList<>();
		list.add(diaryAnswerAlert);
		list.add(freeBoardAnswerAlert);

		when(alertService.findByAlertListByPostUser(user))
				.thenReturn(list);
		List<AlertResponseDto> alertList = alertService.findByAlertListByPostUser(user);
		assertThat(alertList)
				.contains(diaryAnswerAlert)
				.contains(freeBoardAnswerAlert);
	}
	
	@Test
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
		alertService.deleteAlert(good);
		verify(alertService, times(1)).deleteAlert(good);
	}
	
	@Test
	public void deleteDiaryAnswerAlert() {
		alertService.deleteAlert(diaryAnswer);
		verify(alertService, times(1)).deleteAlert(diaryAnswer);
	}
	
	@Test
	public void deleteFreeBoardAnswer() {
		alertService.deleteAlert(freeBoardAnswer);
		verify(alertService, times(1)).deleteAlert(freeBoardAnswer);
	}

}
