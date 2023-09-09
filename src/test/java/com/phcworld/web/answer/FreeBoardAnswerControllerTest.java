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

import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.util.FreeBoardFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.api.model.request.FreeBoardAnswerRequest;
import com.phcworld.domain.api.model.response.FreeBoardAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.exception.MatchNotUserException;
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

	private User user;
	private FreeBoard freeBoard;

	@Before
	public void setup(){
		user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		freeBoard = FreeBoard.builder()
				.id(1L)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
	}

	@Test
	public void createFreeBoardAnswer() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		FreeBoardAnswerRequest request = FreeBoardAnswerRequest.builder()
				.contents("test")
				.build();
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(user)
				.freeBoard(freeBoard)
				.contents(request.getContents())
				.build();
		List<FreeBoardAnswer> list = new ArrayList<FreeBoardAnswer>();
		list.add(freeBoardAnswer);
		freeBoard.setFreeBoardAnswers(list);
		
		FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = FreeBoardAnswerApiResponse.of(freeBoardAnswer);
		
		when(this.freeBoardAnswerService.create(user, freeBoard.getId(), request))
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
		.andExpect(jsonPath("$.error").value("권한이 없습니다."));
	}
	
	@Test
	public void createFailedFreeBoardAnswerWhenEmptyContents() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(post("/freeboards/{freeboardId}/answers", 1L)
				.param("contents", "")
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("잘못된 요청입니다. 내용을 입력하세요."));
	}
	
	@Test
	public void deleteSuccessFreeBoardAnswer() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(user)
				.freeBoard(freeBoard)
				.contents("test")
				.build();
		SuccessResponse response = SuccessResponse.builder()
				.success("삭제성공")
				.build();
		
		when(this.freeBoardAnswerService.delete(freeBoardAnswer.getId(), user))
		.thenReturn(response);
		this.mvc.perform(delete("/freeboards/{freeboardId}/answers/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.success").value("삭제성공"));
	}
	
	@Test
	public void deleteFailedFreeBoardAnswerWhenNotLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(delete("/freeboards/{freeboardId}/answers/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("권한이 없습니다."));
	}
	
	@Test
	public void deleteFailedFreeBoardAnswerWhenNotMatchUserUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
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
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(user2)
				.freeBoard(freeBoard)
				.contents("test")
				.build();
		
		
		when(this.freeBoardAnswerService.delete(freeBoardAnswer.getId(), user))
		.thenThrow(new NotMatchUserException());
		this.mvc.perform(delete("/freeboards/{freeboardId}/answers/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("권한이 없습니다."));
	}
	
	@Test
	public void readFreeBoardAnswer() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(user)
				.freeBoard(freeBoard)
				.contents("test")
				.build();
		List<FreeBoardAnswer> list = new ArrayList<FreeBoardAnswer>();
		list.add(freeBoardAnswer);
		freeBoard.setFreeBoardAnswers(list);
		
		FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = FreeBoardAnswerApiResponse.of(freeBoardAnswer);
		
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
		.andExpect(jsonPath("$.error").value("잘못된 요청입니다. 내용을 입력하세요."));
	}
	
	@Test
	public void updateFreeBoardAnswer() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(user)
				.freeBoard(freeBoard)
				.contents("test")
				.build();
		List<FreeBoardAnswer> list = new ArrayList<FreeBoardAnswer>();
		list.add(freeBoardAnswer);
		freeBoard.setFreeBoardAnswers(list);
		FreeBoardAnswerRequest request = FreeBoardAnswerRequest.builder()
				.id(freeBoardAnswer.getId())
				.contents("update")
				.build();
		freeBoardAnswer.update(request.getContents());
		
		FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = FreeBoardAnswerApiResponse.of(freeBoardAnswer);
		
		when(this.freeBoardAnswerService.update(request, user))
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
