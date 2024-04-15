package com.phcworld.medium.web;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;

import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.embedded.PostInfo;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.web.HomeController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.domain.good.Good;
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
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void index() throws Exception {
		this.mvc.perform(get("/"))
		.andExpect(view().name(containsString("index")))
		.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void redirectDiaryAnswerAlert() throws Exception {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
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
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.build();
		Alert diaryAnswerAlert = Alert.builder()
//				.type("Diary")
//				.diaryAnswer(diaryAnswer)
				.postInfo(postInfo)
				.registerUser(diaryAnswer.getWriter())
				.postWriter(diaryAnswer.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
				
		when(this.alertService.getOneAlert(1L))
		.thenReturn(diaryAnswerAlert);
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(get("/alert/{id}", 1L)
				.session(mockSession))
		.andDo(print())
				.andExpect(status().is3xxRedirection());
//		.andExpect(redirectedUrl("/diaries/" + diaryAnswerAlert.getPostInfo().getRedirectId()));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void redirectFreeBoardAnswer() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardEntity freeBoard = FreeBoardEntity.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		FreeBoardAnswerEntity freeBoardAnswer = FreeBoardAnswerEntity.builder()
				.id(1L)
				.writer(user)
				.freeBoard(freeBoard)
				.contents("test")
				.build();
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		Alert freeBoardAnswerAlert = Alert.builder()
//				.type("FreeBoard")
//				.freeBoardAnswer(freeBoardAnswer)
				.postInfo(postInfo)
				.registerUser(freeBoardAnswer.getWriter())
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		when(this.alertService.getOneAlert(2L))
		.thenReturn(freeBoardAnswerAlert);
		this.mvc.perform(get("/alert/{id}", 2L)
				.session(mockSession))
		.andDo(print())
				.andExpect(status().is3xxRedirection());
//		.andExpect(redirectedUrl("/freeboards/"
//				+ freeBoardAnswerAlert.getPostInfo().getRedirectId()));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void redirectGood() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
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
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.build();
		Alert goodAlert = Alert.builder()
//				.type("good")
//				.good(good)
				.postInfo(postInfo)
				.registerUser(good.getUser())
				.postWriter(good.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		when(this.alertService.getOneAlert(3L))
		.thenReturn(goodAlert);
		this.mvc.perform(get("/alert/{id}", 3L)
				.session(mockSession))
		.andDo(print())
				.andExpect(status().is3xxRedirection());
//		.andExpect(redirectedUrl("/diaries/" + goodAlert.getPostInfo().getRedirectId()));
	}
}
