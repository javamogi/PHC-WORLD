package com.phcworld.web.answer;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import com.phcworld.domain.api.model.response.DiaryAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.exception.MatchNotUserExceptioin;
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
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.id(1L)
				.writer(user)
				.diary(diary)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		List<DiaryAnswer> list = new ArrayList<DiaryAnswer>();
		list.add(diaryAnswer);
		diary.setDiaryAnswers(list);
		
		DiaryAnswerApiResponse diaryAnswerApiResponse = DiaryAnswerApiResponse.builder()
				.id(diaryAnswer.getId())
				.writer(diaryAnswer.getWriter())
				.contents(diaryAnswer.getContents())
				.diaryId(diaryAnswer.getDiary().getId())
				.countOfAnswers(diaryAnswer.getDiary().getCountOfAnswer())
				.createDate(diaryAnswer.getFormattedCreateDate())
				.build();
		
		when(this.diaryAnswerService.createDiaryAnswer(user, diary.getId(), "test"))
		.thenReturn(diaryAnswerApiResponse);
		this.mvc.perform(post("/diary/{diaryId}/answer", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.param("contents", "test")
				.session(mockSession))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").value(diaryAnswerApiResponse.getId()))
		.andExpect(jsonPath("$.writer.id").value(diaryAnswerApiResponse.getWriter().getId()))
		.andExpect(jsonPath("$.contents").value(diaryAnswerApiResponse.getContents()))
		.andExpect(jsonPath("$.diaryId").value(diaryAnswerApiResponse.getDiaryId()))
		.andExpect(jsonPath("$.countOfAnswers").value(diaryAnswerApiResponse.getCountOfAnswers()))
		.andExpect(jsonPath("$.createDate").value(diaryAnswerApiResponse.getCreateDate()));
	}
	
	@Test
	public void createEmptyLoginUser() throws Exception {
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
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.id(1L)
				.writer(user)
				.diary(diary)
				.contents("test")
				.build();
		
		SuccessResponse response = SuccessResponse.builder()
				.success(diaryAnswer.getDiary().getCountOfAnswer())
				.build();
		
		
		when(this.diaryAnswerService.deleteDiaryAnswer(diaryAnswer.getId(), user, diary.getId()))
		.thenReturn(response);
		this.mvc.perform(delete("/diary/{diaryId}/answer/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.success").value(""));
	}
	
	@Test
	public void deleteEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(delete("/diary/{diaryId}/answer/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("로그인을 해야합니다."));
	}
	
	@Test
	public void deleteWhenNotMatchWriter() throws Exception {
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
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.id(1L)
				.writer(user2)
				.diary(diary)
				.contents("test")
				.build();
		
		when(this.diaryAnswerService.deleteDiaryAnswer(diaryAnswer.getId(), user, diary.getId()))
		.thenThrow(new MatchNotUserExceptioin("본인이 작성한 글만 삭제 가능합니다."));
		this.mvc.perform(delete("/diary/{diaryId}/answer/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("본인이 작성한 글만 삭제 가능합니다."));
	}
	
	@Test
	public void readDiaryAnswer() throws Exception {
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
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.id(1L)
				.writer(user)
				.diary(diary)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		List<DiaryAnswer> list = new ArrayList<DiaryAnswer>();
		list.add(diaryAnswer);
		diary.setDiaryAnswers(list);
		
		DiaryAnswerApiResponse diaryAnswerApiResponse = DiaryAnswerApiResponse.builder()
				.id(diaryAnswer.getId())
				.writer(diaryAnswer.getWriter())
				.contents(diaryAnswer.getContents())
				.diaryId(diaryAnswer.getDiary().getId())
				.countOfAnswers(diaryAnswer.getDiary().getCountOfAnswer())
				.createDate(diaryAnswer.getFormattedCreateDate())
				.build();
		
		when(this.diaryAnswerService.readDiaryAnswer(diaryAnswer.getId(), user))
		.thenReturn(diaryAnswerApiResponse);
		
		this.mvc.perform(get("/diary/{diaryId}/answer/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(diaryAnswerApiResponse.getId()))
		.andExpect(jsonPath("$.writer.id").value(diaryAnswerApiResponse.getWriter().getId()))
		.andExpect(jsonPath("$.contents").value(diaryAnswerApiResponse.getContents()))
		.andExpect(jsonPath("$.diaryId").value(diaryAnswerApiResponse.getDiaryId()))
		.andExpect(jsonPath("$.countOfAnswers").value(diaryAnswerApiResponse.getCountOfAnswers()))
		.andExpect(jsonPath("$.createDate").value(diaryAnswerApiResponse.getCreateDate()));
	}
	
	@Test
	public void updateDiaryAnswer() throws Exception {
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
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.id(1L)
				.writer(user)
				.diary(diary)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		List<DiaryAnswer> list = new ArrayList<DiaryAnswer>();
		list.add(diaryAnswer);
		diary.setDiaryAnswers(list);
		diaryAnswer.update("update");
		
		DiaryAnswerApiResponse diaryAnswerApiResponse = DiaryAnswerApiResponse.builder()
				.id(diaryAnswer.getId())
				.writer(diaryAnswer.getWriter())
				.contents(diaryAnswer.getContents())
				.diaryId(diaryAnswer.getDiary().getId())
				.countOfAnswers(diaryAnswer.getDiary().getCountOfAnswer())
				.createDate(diaryAnswer.getFormattedCreateDate())
				.build();
		
		when(this.diaryAnswerService.updateDiaryAnswer(diaryAnswer.getId(), diaryAnswer.getContents(), user))
		.thenReturn(diaryAnswerApiResponse);
		this.mvc.perform(patch("/diary/{diaryId}/answer", 1L)
				.param("id", "1")
				.param("contents", "update")
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(diaryAnswerApiResponse.getId()))
		.andExpect(jsonPath("$.writer.id").value(diaryAnswerApiResponse.getWriter().getId()))
		.andExpect(jsonPath("$.contents").value(diaryAnswerApiResponse.getContents()))
		.andExpect(jsonPath("$.diaryId").value(diaryAnswerApiResponse.getDiaryId()))
		.andExpect(jsonPath("$.countOfAnswers").value(diaryAnswerApiResponse.getCountOfAnswers()))
		.andExpect(jsonPath("$.createDate").value(diaryAnswerApiResponse.getCreateDate()));
	}
	
}
