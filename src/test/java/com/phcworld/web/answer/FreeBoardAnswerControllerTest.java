package com.phcworld.web.answer;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.answer.FreeBoardAnswerServiceImpl;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.board.FreeBoardServiceImpl;
import com.phcworld.domain.user.User;
import com.phcworld.web.HttpSessionUtils;

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
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		FreeBoard freeBoard = new FreeBoard(user, "test", "", "test"); 
		freeBoard.setId(1L);
		given(this.freeBoardService.addFreeBoardAnswer(freeBoard.getId()))
		.willReturn(freeBoard);
		FreeBoardAnswer freeBoardAnswer = new FreeBoardAnswer(user, freeBoard, "test"); 
		freeBoardAnswer.setId(1L);
		given(this.freeBoardAnswerService.createFreeBoardAnswer(user, freeBoard, "test"))
		.willReturn(freeBoardAnswer);
		this.mvc.perform(post("/freeboard/{freeboardId}/answer", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.param("contents", "test")
				.session(mockSession))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id").value(freeBoardAnswer.getId()))
		.andExpect(jsonPath("$.writer.id").value(freeBoardAnswer.getWriter().getId()))
		.andExpect(jsonPath("$.contents").value(freeBoardAnswer.getContents()))
		.andExpect(jsonPath("$.freeBoard.id").value(freeBoardAnswer.getFreeBoard().getId()))
		.andExpect(jsonPath("$.formattedCreateDate").value(freeBoardAnswer.getFormattedCreateDate()));
	}
	
	@Test
	public void createFailedFreeBoardAnswerWhenNotLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(post("/freeboard/{freeboardId}/answer", 1L)
				.param("contents", "test")
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("로그인을 해야합니다."));
	}
	
	@Test
	public void deleteSuccessFreeBoardAnswer() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		FreeBoard freeBoard = new FreeBoard(user, "test", "", "test"); 
		freeBoard.setId(1L);
		FreeBoardAnswer freeBoardAnswer = new FreeBoardAnswer(user, freeBoard, "test"); 
		freeBoardAnswer.setId(1L);
		given(this.freeBoardAnswerService.deleteFreeBoardAnswer(freeBoardAnswer.getId(), user, freeBoard.getId()))
		.willReturn("{\"success\":\"" + "[]" +"\"}");
		this.mvc.perform(delete("/freeboard/{freeboardId}/answer/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.success").value("[]"));
	}
	
	@Test
	public void deleteFailedFreeBoardAnswerWhenNotLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(delete("/freeboard/{freeboardId}/answer/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("로그인을 해야합니다."));
	}
	
	@Test
	public void deleteFailedFreeBoardAnswerWhenNotMatchUserUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		User user2 = new User("test4@test.test", "test4", "테스트4");
		user.setId(2L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		FreeBoard freeBoard = new FreeBoard(user, "test", "", "test"); 
		freeBoard.setId(1L);
		FreeBoardAnswer freeBoardAnswer = new FreeBoardAnswer(user2, freeBoard, "test"); 
		freeBoardAnswer.setId(1L);
		given(this.freeBoardAnswerService.deleteFreeBoardAnswer(freeBoardAnswer.getId(), user, freeBoard.getId()))
		.willReturn("{\"error\":\"" + "본인이 작성한 글만 삭제 가능합니다." +"\"}");
		this.mvc.perform(delete("/freeboard/{freeboardId}/answer/{id}", 1L, 1L)
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("본인이 작성한 글만 삭제 가능합니다."));
	}

}
