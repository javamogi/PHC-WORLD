package com.phcworld.web.answer;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.user.User;
import com.phcworld.service.answer.DiaryAnswerServiceImpl;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.web.HttpSessionUtils;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(DiaryAnswerController.class)
public class DiaryAnswerControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private DiaryServiceImpl diaryService;
	
	@MockBean
	private DiaryAnswerServiceImpl diaryAnswerService;
	
	@Test
	public void createDiaryAnswer() throws Exception {
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
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.countOfGood(0)
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		given(this.diaryService.addDiaryAnswer(diary.getId()))
		.willReturn(diary);
		DiaryAnswer diaryAnswer = new DiaryAnswer(user, diary, "test");
		diaryAnswer.setId(1L);
		given(this.diaryAnswerService.createDiaryAnswer(user, diary, "test"))
		.willReturn(diaryAnswer);
		this.mvc.perform(post("/diary/{diaryId}/answer", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.param("contents", "test")
				.session(mockSession))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").value(diaryAnswer.getId()))
		.andExpect(jsonPath("$.writer.id").value(diaryAnswer.getWriter().getId()))
		.andExpect(jsonPath("$.contents").value(diaryAnswer.getContents()))
		.andExpect(jsonPath("$.diary.id").value(diaryAnswer.getDiary().getId()))
		.andExpect(jsonPath("$.formattedCreateDate").value(diaryAnswer.getFormattedCreateDate()));
	}
	
	@Test
	public void createFailedDiaryAnswerWhenNotLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(post("/diary/{diaryId}/answer", 1L)
				.param("contents", "test")
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("로그인을 해야합니다."));
	}
	
	@Test
	public void deleteSuccessDiaryAnswer() throws Exception {
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
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.countOfGood(0)
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer diaryAnswer = new DiaryAnswer(user, diary, "test");
		diaryAnswer.setId(1L);
		given(this.diaryAnswerService.deleteDiaryAnswer(diaryAnswer.getId(), user, diary.getId()))
		.willReturn("{\"success\":\"" + "[]" +"\"}");
		this.mvc.perform(delete("/diary/{diaryId}/answer/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.success").value("[]"));
	}
	
	@Test
	public void deleteFailedDiaryAnswerWhenNotLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(delete("/diary/{diaryId}/answer/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("로그인을 해야합니다."));
	}
	
	@Test
	public void deleteFailedDiaryAnswerWhenNotMatchUserUser() throws Exception {
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
		User user2 = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.countOfGood(0)
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer diaryAnswer = new DiaryAnswer(user2, diary, "test");
		diaryAnswer.setId(1L);
		given(this.diaryAnswerService.deleteDiaryAnswer(diaryAnswer.getId(), user, diary.getId()))
		.willReturn("{\"error\":\"" + "본인이 작성한 글만 삭제 가능합니다." +"\"}");
		this.mvc.perform(delete("/diary/{diaryId}/answer/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("본인이 작성한 글만 삭제 가능합니다."));
	}
	
}
