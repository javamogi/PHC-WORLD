package com.phcworld.web.board;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.board.FreeBoardRequest;
import com.phcworld.domain.board.FreeBoardResponse;
import com.phcworld.domain.user.User;
import com.phcworld.service.board.FreeBoardServiceImpl;
import com.phcworld.utils.HttpSessionUtils;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(FreeBoardController.class)
public class FreeBoardControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private FreeBoardServiceImpl freeBoardService;
	
	@Test
	public void getAllList() throws Exception{
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
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.createDate("방금전")
				.build();
		FreeBoardResponse response2 = FreeBoardResponse.builder()
				.id(2L)
				.writer(user)
				.title("title2")
				.contents("content2")
				.icon("")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.createDate("방금전")
				.build();
		List<FreeBoardResponse> list = new ArrayList<FreeBoardResponse>();
		list.add(response);
		list.add(response2);
		
		when(this.freeBoardService.findFreeBoardAllListAndSetNewBadge())
		.thenReturn(list);
		this.mvc.perform(get("/freeboards"))
		.andDo(print())
		.andExpect(view().name(containsString("/board/freeboard/freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeboards", list))
		.andExpect(model().size(1));
	}
	
	@Test
	public void matchNotLoginUserCreateForm() throws Exception {
		this.mvc.perform(get("/freeboards/form"))
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void successCreateForm() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		this.mvc.perform(get("/freeboards/form")
				.session(mockSession))
		.andExpect(view().name(containsString("/board/freeboard/freeboard_form")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void create() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		
		FreeBoardRequest request = FreeBoardRequest.builder()
				.title("test")
				.contents("test")
				.icon("")
				.build();
		
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.createDate("방금전")
				.build();
		
		when(this.freeBoardService.createFreeBoard(user, request))
		.thenReturn(response);
		
		this.mvc.perform(post("/freeboards")
				.param("title", "test")
				.param("contents", "test")
				.param("icon", "")
				.session(mockSession))
		.andExpect(redirectedUrl("/freeboards/"+response.getId()));
	}
	
	@Test
	public void createEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(post("/freeboards")
				.param("title", "test")
				.param("contents", "test")
				.param("icon", "")
				.session(mockSession))
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void readWhenWriterEqualLoginUser() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.addFreeBoardCount(1L))
		.thenReturn(response);
		this.mvc.perform(get("/freeboards/{id}", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/freeboard/detail_freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeBoard", response))
		.andExpect(model().attribute("user", true))
		.andExpect(model().attribute("matchUser", true))
		.andExpect(model().attribute("matchAuthority", false))
		.andExpect(model().size(4));
	}
	
	@Test
	public void readWhenNotMatchLoginUserAndWriter() throws Exception {
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
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.addFreeBoardCount(1L))
		.thenReturn(response);
		this.mvc.perform(get("/freeboards/{id}", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/freeboard/detail_freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeBoard", response))
		.andExpect(model().attribute("user", false))
		.andExpect(model().attribute("matchUser", false))
		.andExpect(model().attribute("matchAuthority", false))
		.andExpect(model().size(4));
	}
	
	@Test
	public void readWhenWriterNotEqualLoginUser() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user2)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.addFreeBoardCount(1L))
		.thenReturn(response);
		this.mvc.perform(get("/freeboards/{id}", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/freeboard/detail_freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeBoard", response))
		.andExpect(model().attribute("user", true))
		.andExpect(model().attribute("matchUser", false))
		.andExpect(model().attribute("matchAuthority", false))
		.andExpect(model().size(4));
	}
	
	@Test
	public void readWhenHasAdminAuthority() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_ADMIN")
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user2)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.addFreeBoardCount(1L))
		.thenReturn(response);
		this.mvc.perform(get("/freeboards/{id}", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/freeboard/detail_freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeBoard", response))
		.andExpect(model().attribute("user", true))
		.andExpect(model().attribute("matchUser", false))
		.andExpect(model().attribute("matchAuthority", true))
		.andExpect(model().size(4));
	}
	
	@Test
	public void successUpdateForm() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.getOneFreeBoard(1L))
		.thenReturn(response);
		this.mvc.perform(get("/freeboards/{id}/form", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/freeboard/freeboard_updateForm")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeBoard", response))
		.andExpect(model().size(1));
	}
	
	@Test
	public void updateWhenRequestEmptyLoginUser() throws Exception {
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
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.getOneFreeBoard(1L))
		.thenReturn(response);
		this.mvc.perform(get("/freeboards/{id}/form", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void updateWhenMatchNotLoginUserAndWriter() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user2)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.getOneFreeBoard(1L))
		.thenReturn(response);
		this.mvc.perform(get("/freeboards/{id}/form", 1L)
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		FreeBoard board = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(board.getId())
				.writer(board.getWriter())
				.title(board.getTitle())
				.contents(board.getContents())
				.icon(board.getIcon())
				.badge(board.getBadge())
				.count(board.getCount())
				.countOfAnswer(board.getCountOfAnswer())
				.build();
		FreeBoardRequest request = FreeBoardRequest.builder()
				.id(1L)
				.contents("new contents")
				.icon("")
				.build();
		FreeBoardResponse response2 = FreeBoardResponse.builder()
				.id(1L)
				.writer(board.getWriter())
				.title(board.getTitle())
				.contents(board.getContents())
				.icon(board.getIcon())
				.badge(board.getBadge())
				.count(board.getCount())
				.countOfAnswer(board.getCountOfAnswer())
				.build();
		board.update(request);
		when(this.freeBoardService.getOneFreeBoard(1L))
		.thenReturn(response);
		when(this.freeBoardService.updateFreeBoard(request))
		.thenReturn(response2);
		this.mvc.perform(patch("/freeboards")
				.param("id", "1")
				.session(mockSession))
		.andExpect(redirectedUrl("/freeboards/" + 1L));
	}
	
	@Test
	public void updateWhenEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(patch("/freeboards")
				.param("id", "1")
				.param("contents", "updateTest")
				.param("icon", "test.jpg")
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void updateWhenNotMatchUserAndWriter() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user2)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.getOneFreeBoard(1L))
		.thenReturn(response);
		this.mvc.perform(patch("/freeboards")
				.param("id", "1")
				.param("contents", "updateTest")
				.param("icon", "test.jpg")
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "본인의 작성한 글만 수정 가능합니다."))
		.andExpect(model().size(2));
	}
	
	@Test
	public void successDelete() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.getOneFreeBoard(1L))
		.thenReturn(response);
		this.mvc.perform(delete("/freeboards/{id}", 1L)
				.session(mockSession))
		.andExpect(redirectedUrl("/freeboards"));
	}
	
	@Test
	public void deleteWhenEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(delete("/freeboards/{id}", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void deleteWhenNotMatchAuthority() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user2)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.getOneFreeBoard(1L))
		.thenReturn(response);
		this.mvc.perform(delete("/freeboards/{id}", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "삭제 권한이 없습니다."))
		.andExpect(model().size(1));
	}

}
