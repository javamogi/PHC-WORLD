package com.phcworld.medium.web.answer;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.api.model.request.DiaryAnswerRequest;
import com.phcworld.domain.api.model.response.DiaryAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.Diary;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.service.answer.DiaryAnswerServiceImpl;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.utils.HttpSessionUtils;
import com.phcworld.web.answer.DiaryAnswerController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(DiaryAnswerController.class)
@AutoConfigureRestDocs
public class DiaryAnswerControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private DiaryServiceImpl diaryService;

	@MockBean
	private DiaryAnswerServiceImpl diaryAnswerService;

	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void createDiaryAnswer() throws Exception {
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
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswerRequest request = DiaryAnswerRequest.builder()
				.contents("test")
				.build();
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.id(1L)
				.writer(user)
				.diary(diary)
				.contents(request.getContents())
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
				.updateDate(diaryAnswer.getFormattedUpdateDate())
				.build();

		when(this.diaryAnswerService.create(user, diary.getId(), request))
		.thenReturn(diaryAnswerApiResponse);
		this.mvc.perform(RestDocumentationRequestBuilders.post("/diaries/{diaryId}/answer", 1L)
						.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.param("contents", "test")
				.session(mockSession))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").value(diaryAnswerApiResponse.getId()))
		.andExpect(jsonPath("$.writer.id").value(diaryAnswerApiResponse.getWriter().getId()))
		.andExpect(jsonPath("$.contents").value(diaryAnswerApiResponse.getContents()))
		.andExpect(jsonPath("$.diaryId").value(diaryAnswerApiResponse.getDiaryId()))
		.andExpect(jsonPath("$.countOfAnswers").value(diaryAnswerApiResponse.getCountOfAnswers()))
		.andExpect(jsonPath("$.updateDate").value(diaryAnswerApiResponse.getUpdateDate()))
				.andDo(document("diary-answer-save",
						RequestDocumentation.pathParameters(
								RequestDocumentation.parameterWithName("diaryId").description("게시글 ID")
						)
//						,
//						PayloadDocumentation.responseFields(
//								PayloadDocumentation.fieldWithPath("diaryId").description("게시글 ID"),
//								PayloadDocumentation.fieldWithPath("writer").description("작성자 정보"),
//								PayloadDocumentation.fieldWithPath("contents").description("게시글 내용")
//						)
				));
	}
	
	@Test
	public void createEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(post("/diaries/{diaryId}/answer", 1L)
						.with(csrf())
				.param("contents", "test")
				.session(mockSession))
				.andExpect(status().isUnauthorized());
//		.andExpect(jsonPath("$.error").value("권한이 없습니다."));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void createEmptyContents() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(post("/diaries/{diaryId}/answer", 1L)
						.with(csrf())
				.param("contents", "")
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("잘못된 요청입니다. 내용을 입력하세요."));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void deleteSuccessDiaryAnswer() throws Exception {
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
				.success("삭제성공")
				.build();


		when(this.diaryAnswerService.delete(diaryAnswer.getId(), user))
		.thenReturn(response);
		this.mvc.perform(delete("/diaries/{diaryId}/answer/{id}", 1L, 1L)
						.with(csrf())
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.success").value("삭제성공"));
	}
	
	@Test
	public void deleteEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(delete("/diaries/{diaryId}/answer/{id}", 1L, 1L)
						.with(csrf())
				.session(mockSession))
				.andExpect(status().isUnauthorized());
//		.andExpect(jsonPath("$.error").value("권한이 없습니다."));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void deleteWhenNotMatchWriter() throws Exception {
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
		UserEntity user2 = UserEntity.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
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

		when(this.diaryAnswerService.delete(diaryAnswer.getId(), user))
		.thenThrow(new NotMatchUserException());
		this.mvc.perform(delete("/diaries/{diaryId}/answer/{id}", 1L, 1L)
						.with(csrf())
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("권한이 없습니다."));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void readDiaryAnswer() throws Exception {
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
				.updateDate(diaryAnswer.getFormattedUpdateDate())
				.build();

		when(this.diaryAnswerService.read(diaryAnswer.getId(), user))
		.thenReturn(diaryAnswerApiResponse);

		this.mvc.perform(get("/diaries/{diaryId}/answer/{id}", 1L, 1L)
						.with(csrf())
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(diaryAnswerApiResponse.getId()))
		.andExpect(jsonPath("$.writer.id").value(diaryAnswerApiResponse.getWriter().getId()))
		.andExpect(jsonPath("$.contents").value(diaryAnswerApiResponse.getContents()))
		.andExpect(jsonPath("$.diaryId").value(diaryAnswerApiResponse.getDiaryId()))
		.andExpect(jsonPath("$.countOfAnswers").value(diaryAnswerApiResponse.getCountOfAnswers()))
		.andExpect(jsonPath("$.updateDate").value(diaryAnswerApiResponse.getUpdateDate()));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void updateEmptyContents() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(patch("/diaries/{diaryId}/answer", 1L)
						.with(csrf())
				.param("contents", "")
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("잘못된 요청입니다. 내용을 입력하세요."));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void updateDiaryAnswer() throws Exception {
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
				.build();
		List<DiaryAnswer> list = new ArrayList<DiaryAnswer>();
		list.add(diaryAnswer);
		diary.setDiaryAnswers(list);
		DiaryAnswerRequest request = DiaryAnswerRequest.builder()
				.id(diaryAnswer.getId())
				.contents("update")
				.build();
		diaryAnswer.update(request.getContents());

		DiaryAnswerApiResponse diaryAnswerApiResponse = DiaryAnswerApiResponse.builder()
				.id(diaryAnswer.getId())
				.writer(diaryAnswer.getWriter())
				.contents(diaryAnswer.getContents())
				.diaryId(diaryAnswer.getDiary().getId())
				.countOfAnswers(diaryAnswer.getDiary().getCountOfAnswer())
				.updateDate(diaryAnswer.getFormattedUpdateDate())
				.build();

		when(this.diaryAnswerService.update(request, user))
		.thenReturn(diaryAnswerApiResponse);
		this.mvc.perform(patch("/diaries/{diaryId}/answer", 1L)
						.with(csrf())
				.param("id", "1")
				.param("contents", "update")
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(diaryAnswerApiResponse.getId()))
		.andExpect(jsonPath("$.writer.id").value(diaryAnswerApiResponse.getWriter().getId()))
		.andExpect(jsonPath("$.contents").value(diaryAnswerApiResponse.getContents()))
		.andExpect(jsonPath("$.diaryId").value(diaryAnswerApiResponse.getDiaryId()))
		.andExpect(jsonPath("$.countOfAnswers").value(diaryAnswerApiResponse.getCountOfAnswers()))
		.andExpect(jsonPath("$.updateDate").value(diaryAnswerApiResponse.getUpdateDate()));
	}

}
