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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.api.model.response.FreeBoardAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.user.User;
import com.phcworld.service.answer.FreeBoardAnswerServiceImpl;
import com.phcworld.service.board.FreeBoardServiceImpl;
import com.phcworld.utils.HttpSessionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(FreeBoardAnswerController.class)
public class FreeBoardAnswerControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private FreeBoardServiceImpl freeBoardService;
	
	@MockBean
	private FreeBoardAnswerServiceImpl freeBoardAnswerService;

	@Test
	public void createFreeBoardAnswer() throws Exception {
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
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
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
		List<FreeBoardAnswer> list = new ArrayList<FreeBoardAnswer>();
		list.add(freeBoardAnswer);
		freeBoard.setFreeBoardAnswers(list);
		
		FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = FreeBoardAnswerApiResponse.builder()
				.id(freeBoardAnswer.getId())
				.writer(freeBoardAnswer.getWriter())
				.contents(freeBoardAnswer.getContents())
				.freeBoardId(freeBoardAnswer.getFreeBoard().getId())
				.countOfAnswers(freeBoardAnswer.getFreeBoard().getCountOfAnswer())
				.updateDate(freeBoardAnswer.getFormattedUpdateDate())
				.build();
		
		when(this.freeBoardAnswerService.create(user, freeBoard.getId(), "test"))
		.thenReturn(freeBoardAnswerApiResponse);
		this.mvc.perform(post("/freeboards/{freeboardId}/answers", 1L)
				.param("contents", "test")
				.session(mockSession))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").value(freeBoardAnswerApiResponse.getId()))
		.andExpect(jsonPath("$.writer.id").value(freeBoardAnswerApiResponse.getWriter().getId()))
		.andExpect(jsonPath("$.contents").value(freeBoardAnswerApiResponse.getContents()))
		.andExpect(jsonPath("$.freeBoardId").value(freeBoardAnswerApiResponse.getFreeBoardId()))
		.andExpect(jsonPath("$.countOfAnswers").value(freeBoardAnswerApiResponse.getCountOfAnswers()))
		.andExpect(jsonPath("$.updateDate").value(freeBoardAnswerApiResponse.getUpdateDate()));
	}
	
	@Test
	public void createFailedFreeBoardAnswerWhenNotLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(post("/freeboards/{freeboardId}/answers", 1L)
				.param("contents", "test")
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("로그인을 해야합니다."));
	}
	
	@Test
	public void createFailedFreeBoardAnswerWhenEmptyContents() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(post("/freeboards/{freeboardId}/answers", 1L)
				.param("contents", "")
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("내용을 입력하세요."));
	}
	
	@Test
	public void deleteSuccessFreeBoardAnswer() throws Exception {
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
		SuccessResponse response = SuccessResponse.builder()
				.success(freeBoardAnswer.getFreeBoard().getCountOfAnswer())
				.build();
		
		when(this.freeBoardAnswerService.delete(freeBoardAnswer.getId(), user))
		.thenReturn(response);
		this.mvc.perform(delete("/freeboards/{freeboardId}/answers/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.success").value(""));
	}
	
	@Test
	public void deleteFailedFreeBoardAnswerWhenNotLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(delete("/freeboards/{freeboardId}/answers/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("로그인을 해야합니다."));
	}
	
	@Test
	public void deleteFailedFreeBoardAnswerWhenNotMatchUserUser() throws Exception {
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
				.writer(user2)
				.freeBoard(freeBoard)
				.contents("test")
				.build();
		
		
		when(this.freeBoardAnswerService.delete(freeBoardAnswer.getId(), user))
		.thenThrow(new MatchNotUserExceptioin("본인이 작성한 글만 삭제 가능합니다."));
		this.mvc.perform(delete("/freeboards/{freeboardId}/answers/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("본인이 작성한 글만 삭제 가능합니다."));
	}
	
	@Test
	public void readFreeBoardAnswer() throws Exception {
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
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
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
		List<FreeBoardAnswer> list = new ArrayList<FreeBoardAnswer>();
		list.add(freeBoardAnswer);
		freeBoard.setFreeBoardAnswers(list);
		
		FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = FreeBoardAnswerApiResponse.builder()
				.id(freeBoardAnswer.getId())
				.writer(freeBoardAnswer.getWriter())
				.contents(freeBoardAnswer.getContents())
				.freeBoardId(freeBoardAnswer.getFreeBoard().getId())
				.countOfAnswers(freeBoard.getCountOfAnswer())
				.updateDate(freeBoardAnswer.getFormattedUpdateDate())
				.build();
		
		when(this.freeBoardAnswerService.read(freeBoardAnswer.getId(), user))
		.thenReturn(freeBoardAnswerApiResponse);
		this.mvc.perform(get("/freeboards/{freeboardId}/answers/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(freeBoardAnswerApiResponse.getId()))
		.andExpect(jsonPath("$.writer.id").value(freeBoardAnswerApiResponse.getWriter().getId()))
		.andExpect(jsonPath("$.contents").value(freeBoardAnswerApiResponse.getContents()))
		.andExpect(jsonPath("$.freeBoardId").value(freeBoardAnswerApiResponse.getFreeBoardId()))
		.andExpect(jsonPath("$.countOfAnswers").value(freeBoardAnswerApiResponse.getCountOfAnswers()))
		.andExpect(jsonPath("$.updateDate").value(freeBoardAnswerApiResponse.getUpdateDate()));
	}
	
	@Test
	public void updateFailedFreeBoardAnswerWhenEmptyContents() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(patch("/freeboards/{freeboardId}/answers", 1L)
				.param("contents", "")
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("내용을 입력하세요."));
	}
	
	@Test
	public void updateFreeBoardAnswer() throws Exception {
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
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
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
		List<FreeBoardAnswer> list = new ArrayList<FreeBoardAnswer>();
		list.add(freeBoardAnswer);
		freeBoard.setFreeBoardAnswers(list);
		freeBoardAnswer.update("update");
		
		FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = FreeBoardAnswerApiResponse.builder()
				.id(freeBoardAnswer.getId())
				.writer(freeBoardAnswer.getWriter())
				.contents(freeBoardAnswer.getContents())
				.freeBoardId(freeBoardAnswer.getFreeBoard().getId())
				.countOfAnswers(freeBoard.getCountOfAnswer())
				.updateDate(freeBoardAnswer.getFormattedUpdateDate())
				.build();
		
		when(this.freeBoardAnswerService.update(freeBoardAnswer.getId(), freeBoardAnswer.getContents(), user))
		.thenReturn(freeBoardAnswerApiResponse);
		this.mvc.perform(patch("/freeboards/{freeboardId}/answers", 1L)
				.param("id", "1")
				.param("contents", "update")
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(freeBoardAnswerApiResponse.getId()))
		.andExpect(jsonPath("$.writer.id").value(freeBoardAnswerApiResponse.getWriter().getId()))
		.andExpect(jsonPath("$.contents").value(freeBoardAnswerApiResponse.getContents()))
		.andExpect(jsonPath("$.freeBoardId").value(freeBoardAnswerApiResponse.getFreeBoardId()))
		.andExpect(jsonPath("$.countOfAnswers").value(freeBoardAnswerApiResponse.getCountOfAnswers()))
		.andExpect(jsonPath("$.updateDate").value(freeBoardAnswerApiResponse.getUpdateDate()));
	}

}
