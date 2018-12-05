package com.phcworld.web.board;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.board.FreeBoardServiceImpl;
import com.phcworld.domain.user.User;
import com.phcworld.web.HttpSessionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(FreeBoardController.class)
public class FreeBoardControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private FreeBoardServiceImpl freeBoardService;

	@Test
	public void freeBoardList() throws Exception{
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		FreeBoard freeBoard = new FreeBoard(user, "test", "", "test");
		freeBoard.setId(1L);
		FreeBoard freeBoard2 = new FreeBoard(user, "test2", "", "test2");
		freeBoard2.setId(2L);
		List<FreeBoard> list = new ArrayList<FreeBoard>();
		list.add(freeBoard);
		list.add(freeBoard2);
		given(this.freeBoardService.findFreeBoardAllList())
		.willReturn(list);
		this.mvc.perform(get("/freeboard/list"))
		.andDo(print())
		.andExpect(view().name(containsString("/board/freeboard/freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeboards", list))
		.andExpect(model().size(1));
	}
	
	@Test
	public void whenNotLoginUserFreeBoardForm() throws Exception {
		this.mvc.perform(get("/freeboard/form"))
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void freeBoardForm() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		this.mvc.perform(get("/freeboard/form")
				.session(mockSession))
		.andExpect(view().name(containsString("/board/freeboard/freeboard_form")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void createFreeBoard() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		this.mvc.perform(post("/freeboard")
				.param("title", "test")
				.param("contents", "test")
				.param("icon", "")
				.session(mockSession))
		.andExpect(redirectedUrl("/freeboard/list"));
	}
	
	@Test
	public void createFailedNotLoginUserFreeBoard() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(post("/freeboard")
				.param("title", "test")
				.param("contents", "test")
				.param("icon", "")
				.session(mockSession))
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void detailFreeBoardWhenFreeBoardWriterEqualLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		FreeBoard freeBoard = new FreeBoard(user, "test", "", "test");
		freeBoard.setId(1L);
		given(this.freeBoardService.addFreeBoardCount(1L))
		.willReturn(freeBoard);
		this.mvc.perform(get("/freeboard/{id}/detail", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/freeboard/detail_freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeBoard", freeBoard))
		.andExpect(model().attribute("user", true))
		.andExpect(model().attribute("matchUser", true))
		.andExpect(model().attribute("matchAuthority", false))
		.andExpect(model().size(4));
	}
	
	@Test
	public void whenNotLoginUserDetailFreeBoard() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		FreeBoard freeBoard = new FreeBoard(user, "test", "", "test");
		freeBoard.setId(1L);
		given(this.freeBoardService.addFreeBoardCount(1L))
		.willReturn(freeBoard);
		this.mvc.perform(get("/freeboard/{id}/detail", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/freeboard/detail_freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeBoard", freeBoard))
		.andExpect(model().attribute("user", false))
		.andExpect(model().attribute("matchUser", false))
		.andExpect(model().attribute("matchAuthority", false))
		.andExpect(model().size(4));
	}
	
	@Test
	public void detailFreeBoardWhenFreeBoardWriterNotEqualLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = new User("test4@test.test", "test4", "테스트4");
		user.setId(2L);
		FreeBoard freeBoard = new FreeBoard(user2, "test", "", "test");
		freeBoard.setId(1L);
		given(this.freeBoardService.addFreeBoardCount(1L))
		.willReturn(freeBoard);
		this.mvc.perform(get("/freeboard/{id}/detail", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/freeboard/detail_freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeBoard", freeBoard))
		.andExpect(model().attribute("user", true))
		.andExpect(model().attribute("matchUser", false))
		.andExpect(model().attribute("matchAuthority", false))
		.andExpect(model().size(4));
	}
	
	@Test
	public void detailFreBoardWhenHasAuthority() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		user.setAuthority("ROLE_ADMIN");
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = new User("test4@test.test", "test4", "테스트4");
		user.setId(2L);
		FreeBoard freeBoard = new FreeBoard(user2, "test", "", "test");
		freeBoard.setId(1L);
		given(this.freeBoardService.addFreeBoardCount(1L))
		.willReturn(freeBoard);
		this.mvc.perform(get("/freeboard/{id}/detail", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/freeboard/detail_freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeBoard", freeBoard))
		.andExpect(model().attribute("user", true))
		.andExpect(model().attribute("matchUser", false))
		.andExpect(model().attribute("matchAuthority", true))
		.andExpect(model().size(4));
	}
	
	@Test
	public void updateFreeBoardForm() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		FreeBoard freeBoard = new FreeBoard(user, "test", "", "test");
		freeBoard.setId(1L);
		given(this.freeBoardService.getOneFreeBoard(1L))
		.willReturn(freeBoard);
		this.mvc.perform(get("/freeboard/{id}/form", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/freeboard/freeboard_updateForm")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeBoard", freeBoard))
		.andExpect(model().size(1));
	}
	
	@Test
	public void updateNotLoginUserFreeBoardForm() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		FreeBoard freeBoard = new FreeBoard(user, "test", "", "test");
		freeBoard.setId(1L);
		given(this.freeBoardService.getOneFreeBoard(1L))
		.willReturn(freeBoard);
		this.mvc.perform(get("/freeboard/{id}/form", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void updateMatchNotUserFreeBoardForm() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = new User("test4@test.test", "test4", "테스트4");
		user.setId(2L);
		FreeBoard freeBoard = new FreeBoard(user2, "test", "", "test");
		freeBoard.setId(1L);
		given(this.freeBoardService.getOneFreeBoard(1L))
		.willReturn(freeBoard);
		this.mvc.perform(get("/freeboard/{id}/form", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "본인의 작성한 글만 수정 가능합니다."))
		.andExpect(model().size(1));
	}
	
	@Test
	public void successUpdateFreeBoard() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		FreeBoard freeBoard = new FreeBoard(user, "test", "", "test");
		freeBoard.setId(1L);
		given(this.freeBoardService.getOneFreeBoard(1L))
		.willReturn(freeBoard);
		FreeBoard updatedFreeBoard = new FreeBoard(user, "test", "", "updateTest");
		freeBoard.setId(1L);
		given(this.freeBoardService.updateFreeBoard(updatedFreeBoard, "updateTest", "test.jpg"))
		.willReturn(updatedFreeBoard);
		this.mvc.perform(put("/freeboard/{id}", 1L)
				.param("contents", "updateTest")
				.param("icon", "test.jpg")
				.session(mockSession))
		.andExpect(redirectedUrl("/freeboard/" + 1L + "/detail"));
	}
	
	@Test
	public void updateFailedNotLoginUserFreeBoard() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		this.mvc.perform(put("/freeboard/{id}", 1L)
				.param("contents", "updateTest")
				.param("icon", "test.jpg")
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void updateFailedNotMatchUserFreeBoard() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = new User("test4@test.test", "test4", "테스트4");
		user.setId(2L);
		FreeBoard freeBoard = new FreeBoard(user2, "test", "", "test");
		freeBoard.setId(1L);
		given(this.freeBoardService.getOneFreeBoard(1L))
		.willReturn(freeBoard);
		this.mvc.perform(put("/freeboard/{id}", 1L)
				.param("contents", "updateTest")
				.param("icon", "test.jpg")
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "본인의 작성한 글만 수정 가능합니다."))
		.andExpect(model().size(1));
	}
	
	@Test
	public void successDeleteFreeBoard() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		FreeBoard freeBoard = new FreeBoard(user, "test", "", "test");
		freeBoard.setId(1L);
		given(this.freeBoardService.getOneFreeBoard(1L))
		.willReturn(freeBoard);
		this.mvc.perform(delete("/freeboard/{id}/delete", 1L)
				.session(mockSession))
		.andExpect(redirectedUrl("/freeboard/list"));
	}
	
	@Test
	public void deleteFailedNotLoginUserFreeBoard() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(delete("/freeboard/{id}/delete", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void deleteFailedNotMatchAuthorityFreeBoard() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = new User("test4@test.test", "test4", "테스트4");
		user.setId(2L);
		FreeBoard freeBoard = new FreeBoard(user2, "test", "", "test");
		freeBoard.setId(1L);
		given(this.freeBoardService.getOneFreeBoard(1L))
		.willReturn(freeBoard);
		this.mvc.perform(delete("/freeboard/{id}/delete", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "삭제 권한이 없습니다."))
		.andExpect(model().size(1));
	}

}
