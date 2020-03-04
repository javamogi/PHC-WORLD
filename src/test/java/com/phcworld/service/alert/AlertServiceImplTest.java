package com.phcworld.service.alert;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.service.alert.AlertServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AlertServiceImplTest {
	
	@MockBean
	private AlertServiceImpl alertService;
	
	@Test
	public void createDiaryAnswerAlert() {
		User writer = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(writer)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.id(1L)
				.writer(writer)
				.diary(diary)
				.contents("diaryAnswer")
				.build();
		Alert alert = Alert.builder()
				.type("Diary")
				.diaryAnswer(diaryAnswer)
				.postWriter(diary.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		when(alertService.createAlert(diaryAnswer))
		.thenReturn(alert);
		Alert alertCreated = alertService.createAlert(diaryAnswer);
		assertThat(diaryAnswer, is(alertCreated.getDiaryAnswer()));
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
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.badge("")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(user)
				.freeBoard(freeBoard)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		Alert alert = Alert.builder()
				.type("FreeBoard")
				.freeBoardAnswer(freeBoardAnswer)
				.postWriter(freeBoard.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		when(alertService.createAlert(freeBoardAnswer))
		.thenReturn(alert);
		Alert alertCreated = alertService.createAlert(freeBoardAnswer);
		assertThat(freeBoardAnswer, is(alertCreated.getFreeBoardAnswer()));
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
				.id(1L)
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
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
		when(alertService.createAlert(good))
		.thenReturn(alert);
		Alert alertCreated = alertService.createAlert(good);
		assertThat(good, is(alertCreated.getGood()));
	}
	
	@Test
	public void getOneAlert() {
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
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.badge("")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(user)
				.freeBoard(freeBoard)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		Alert alert = Alert.builder()
				.id(1L)
				.type("FreeBoard")
				.freeBoardAnswer(freeBoardAnswer)
				.postWriter(freeBoard.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		when(alertService.getOneAlert(alert.getId()))
		.thenReturn(alert);
		Alert alertCreated = alertService.getOneAlert(alert.getId());
		assertThat(alert, is(alertCreated));
	}
	
	@Test
	public void findPageRequestAlertByPostUser() throws Exception {
		User writer = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(writer)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.id(1L)
				.writer(writer)
				.diary(diary)
				.contents("diaryAnswer")
				.build();
		Alert diaryAnswerAlert = Alert.builder()
				.type("Diary")
				.diaryAnswer(diaryAnswer)
				.postWriter(diary.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.title("title")
				.contents("content")
				.icon("")
				.writer(writer)
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		Alert freeBoardAnswerAlert = Alert.builder()
				.type("Free Board")
				.freeBoardAnswer(freeBoardAnswer)
				.postWriter(writer)
				.createDate(LocalDateTime.now())
				.build();
		List<Alert> list = new ArrayList<Alert>();
		list.add(diaryAnswerAlert);
		list.add(freeBoardAnswerAlert);
		when(alertService.findListAlertByPostUser(writer))
		.thenReturn(list);
		List<Alert> alertList = alertService.findListAlertByPostUser(writer);
		assertThat(alertList, hasItems(diaryAnswerAlert, freeBoardAnswerAlert));
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
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.id(1L)
				.writer(user)
				.diary(diary)
				.contents("diaryAnswer")
				.createDate(LocalDateTime.now())
				.build();
		alertService.deleteAlert(diaryAnswer);
		verify(alertService, times(1)).deleteAlert(diaryAnswer);
	}
	
	@Test
	public void deleteFreeBoardAnswer() {
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
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.badge("")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(user)
				.freeBoard(freeBoard)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		alertService.deleteAlert(freeBoardAnswer);
		verify(alertService, times(1)).deleteAlert(freeBoardAnswer);
	}

}
