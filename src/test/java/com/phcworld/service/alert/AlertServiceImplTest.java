package com.phcworld.service.alert;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.alert.dto.AlertResponseDto;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
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

	private User user;
	private Diary diary;

	private DiaryAnswer diaryAnswer;

	private FreeBoard freeBoard;
	private FreeBoardAnswer freeBoardAnswer;

	@Before
	public void setup(){
		user = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
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
		freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.badge("")
				.createDate(LocalDateTime.now())
				.build();
		freeBoardAnswer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(user)
				.freeBoard(freeBoard)
				.contents("test")
				.build();
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
				.build();
		when(alertService.createAlert(diaryAnswer))
		.thenReturn(alert);
		Alert alertCreated = alertService.createAlert(diaryAnswer);
		assertThat(diaryAnswer.getId()).isEqualTo(alertCreated.getPostId());
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
				.build();
		when(alertService.createAlert(freeBoardAnswer))
		.thenReturn(alert);
		Alert alertCreated = alertService.createAlert(freeBoardAnswer);
		assertThat(freeBoardAnswer.getId()).isEqualTo(alertCreated.getPostId());
	}
	
	@Test
	public void createGoodAlert() {
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
		when(alertService.createAlert(good))
		.thenReturn(alert);
		Alert alertCreated = alertService.createAlert(good);
		assertThat(good.getId()).isEqualTo(alertCreated.getPostId());
	}
	
	@Test
	public void getOneAlert() {
		Alert alert = Alert.builder()
				.id(1L)
//				.type("FreeBoard")
//				.freeBoardAnswer(freeBoardAnswer)
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.registerUser(freeBoardAnswer.getWriter())
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		when(alertService.getOneAlert(alert.getId()))
		.thenReturn(alert);
		Alert alertCreated = alertService.getOneAlert(alert.getId());
		assertThat(alert).isEqualTo(alertCreated);
	}
	
	@Test
	public void findPageRequestAlertByPostUser() throws Exception {
		Alert diaryAnswerAlert = Alert.builder()
//				.type("Diary")
//				.diaryAnswer(diaryAnswer)
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.registerUser(diaryAnswer.getWriter())
				.postWriter(diaryAnswer.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		Alert freeBoardAnswerAlert = Alert.builder()
//				.type("Free Board")
//				.freeBoardAnswer(freeBoardAnswer)
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.registerUser(freeBoardAnswer.getWriter())
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		List<Alert> list = new ArrayList<Alert>();
		list.add(diaryAnswerAlert);
		list.add(freeBoardAnswerAlert);
		when(alertService.findListAlertByPostUser(user))
		.thenReturn(list);
		List<Alert> alertList = alertService.findListAlertByPostUser(user);
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
				.formattedCreateDate(freeBoardAnswer.getFormattedCreateDate())
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
