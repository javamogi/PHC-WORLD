package com.phcworld.web;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.service.alert.AlertServiceImpl;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private AlertServiceImpl alertService;
	
	@Test
	public void alertServiceNotNull() {
		assertNotNull(alertService);
	}
	
	@Test
	public void index() throws Exception {
		this.mvc.perform(get(""))
		.andExpect(view().name(containsString("index")))
		.andExpect(status().isOk());
	}

	@Test
	public void redirectDiaryAnswerAlert() throws Exception {
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
				.contents("test")
				.build();
		Alert diaryAnswerAlert = Alert.builder()
				.type("Diary")
				.diaryAnswer(diaryAnswer)
				.postWriter(diary.getWriter())
				.createDate(LocalDateTime.now())
				.build();
				
		when(this.alertService.getOneAlert(1L))
		.thenReturn(diaryAnswerAlert);
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(get("/alert/{id}", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(redirectedUrl("/diary/" + diaryAnswerAlert.getDiaryAnswer().getDiary().getId() + "/detail"));
	}
	
	@Test
	public void redirectFreeBoardAnswer() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
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
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(user)
				.freeBoard(freeBoard)
				.contents("test")
				.build();
		Alert freeBoardAnswerAlert = Alert.builder()
				.type("FreeBoard")
				.freeBoardAnswer(freeBoardAnswer)
				.postWriter(freeBoard.getWriter())
				.createDate(LocalDateTime.now())
				.build();
		when(this.alertService.getOneAlert(2L))
		.thenReturn(freeBoardAnswerAlert);
		this.mvc.perform(get("/alert/{id}", 2L)
				.session(mockSession))
		.andDo(print())
		.andExpect(redirectedUrl("/freeboards/" 
				+ freeBoardAnswerAlert.getFreeBoardAnswer().getFreeBoard().getId()));
	}
	
	@Test
	public void redirectGood() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
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
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.build();
		Alert goodAlert = Alert.builder()
				.type("good")
				.good(good)
				.postWriter(good.getUser())
				.createDate(LocalDateTime.now())
				.build();
		when(this.alertService.getOneAlert(3L))
		.thenReturn(goodAlert);
		this.mvc.perform(get("/alert/{id}", 3L)
				.session(mockSession))
		.andDo(print())
		.andExpect(redirectedUrl("/diary/" + goodAlert.getGood().getDiary().getId() + "/detail"));
	}
}
