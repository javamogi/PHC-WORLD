package com.phcworld.medium.web.board;

import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.controller.port.FreeBoardResponse;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.freeboard.service.FreeBoardServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;
import com.phcworld.utils.HttpSessionUtils;
import com.phcworld.freeboard.controller.FreeBoardController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(FreeBoardController.class)
public class FreeBoardControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private FreeBoardServiceImpl freeBoardService;

	@MockBean
	private TimelineServiceImpl timelineService;

	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void getAllList() throws Exception{
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
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
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
		this.mvc.perform(get("/freeboards")
						.with(csrf()))
		.andDo(print())
		.andExpect(view().name(containsString("board/freeboard/freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeboards", list))
		.andExpect(model().size(1));
	}
	
	@Test
	public void matchNotLoginUserCreateForm() throws Exception {
		this.mvc.perform(get("/freeboards/form"))
				.andExpect(status().isUnauthorized());
//		.andExpect(view().name(containsString("/user/login")))
//		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void successCreateForm() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		this.mvc.perform(get("/freeboards/form")
						.with(csrf())
				.session(mockSession))
		.andExpect(view().name(containsString("board/freeboard/freeboard_form")))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void create() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		
		FreeBoardRequest request = FreeBoardRequest.builder()
				.title("test")
				.contents("test")
				.build();
		
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.createDate("방금전")
				.build();
		
		when(this.freeBoardService.createFreeBoard(user, request))
		.thenReturn(response);

		this.mvc.perform(post("/freeboards")
						.with(csrf())
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
						.with(csrf())
				.param("title", "test")
				.param("contents", "test")
				.param("icon", "")
				.session(mockSession))
				.andExpect(status().isUnauthorized());
//		.andExpect(view().name(containsString("/user/login")))
//		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void readWhenWriterEqualLoginUser() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.addFreeBoardCount(1L))
		.thenReturn(response);
		this.mvc.perform(get("/freeboards/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("board/freeboard/detail_freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeBoard", response))
		.andExpect(model().attribute("user", true))
		.andExpect(model().attribute("matchUser", true))
		.andExpect(model().attribute("matchAuthority", false))
		.andExpect(model().size(4));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void readWhenNotMatchLoginUserAndWriter() throws Exception {
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
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.addFreeBoardCount(1L))
		.thenReturn(response);
		this.mvc.perform(get("/freeboards/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("board/freeboard/detail_freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeBoard", response))
		.andExpect(model().attribute("user", false))
		.andExpect(model().attribute("matchUser", false))
		.andExpect(model().attribute("matchAuthority", false))
		.andExpect(model().size(4));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void readWhenWriterNotEqualLoginUser() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		UserEntity user2 = UserEntity.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user2)
				.title("title")
				.contents("content")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.addFreeBoardCount(1L))
		.thenReturn(response);
		this.mvc.perform(get("/freeboards/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("board/freeboard/detail_freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeBoard", response))
		.andExpect(model().attribute("user", true))
		.andExpect(model().attribute("matchUser", false))
		.andExpect(model().attribute("matchAuthority", false))
		.andExpect(model().size(4));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void readWhenHasAdminAuthority() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_ADMIN)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		UserEntity user2 = UserEntity.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user2)
				.title("title")
				.contents("content")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.addFreeBoardCount(1L))
		.thenReturn(response);
		this.mvc.perform(get("/freeboards/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("board/freeboard/detail_freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeBoard", response))
		.andExpect(model().attribute("user", true))
		.andExpect(model().attribute("matchUser", false))
		.andExpect(model().attribute("matchAuthority", true))
		.andExpect(model().size(4));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void successUpdateForm() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.getOneFreeBoard(1L))
		.thenReturn(response);
		this.mvc.perform(get("/freeboards/{id}/form", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("board/freeboard/freeboard_updateForm")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeBoard", response))
		.andExpect(model().size(1));
	}
	
	@Test
	public void updateWhenRequestEmptyLoginUser() throws Exception {
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
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.getOneFreeBoard(1L))
		.thenReturn(response);
		this.mvc.perform(get("/freeboards/{id}/form", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
				.andExpect(status().isUnauthorized());
//		.andExpect(view().name(containsString("/user/login")))
//		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void updateWhenMatchNotLoginUserAndWriter() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		UserEntity user2 = UserEntity.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user2)
				.title("title")
				.contents("content")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.getOneFreeBoard(1L))
		.thenReturn(response);
		this.mvc.perform(get("/freeboards/{id}/form", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "본인의 작성한 글만 수정 가능합니다."))
		.andExpect(model().size(1));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void successUpdateFreeBoard() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		FreeBoardEntity board = FreeBoardEntity.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.count(0)
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(board.getId())
				.writer(board.getWriter())
				.title(board.getTitle())
				.contents(board.getContents())
				.count(board.getCount())
				.countOfAnswer(board.getCountOfAnswer())
				.build();
		FreeBoardRequest request = FreeBoardRequest.builder()
				.id(1L)
				.contents("new contents")
				.build();
		FreeBoardResponse response2 = FreeBoardResponse.builder()
				.id(1L)
				.writer(board.getWriter())
				.title(board.getTitle())
				.contents(board.getContents())
				.count(board.getCount())
				.countOfAnswer(board.getCountOfAnswer())
				.build();
		board.update(request);
		when(this.freeBoardService.getOneFreeBoard(1L))
		.thenReturn(response);
		when(this.freeBoardService.updateFreeBoard(request))
		.thenReturn(response2);
		this.mvc.perform(patch("/freeboards")
						.with(csrf())
				.param("id", "1")
				.session(mockSession))
		.andExpect(redirectedUrl("/freeboards/" + 1L));
	}
	
	@Test
	public void updateWhenEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(patch("/freeboards")
						.with(csrf())
				.param("id", "1")
				.param("contents", "updateTest")
				.param("icon", "test.jpg")
				.session(mockSession))
		.andDo(print())
				.andExpect(status().isUnauthorized());
//		.andExpect(view().name(containsString("/user/login")))
//		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void updateWhenNotMatchUserAndWriter() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		UserEntity user2 = UserEntity.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user2)
				.title("title")
				.contents("content")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.getOneFreeBoard(1L))
		.thenReturn(response);
		this.mvc.perform(patch("/freeboards")
						.with(csrf())
				.param("id", "1")
				.param("contents", "updateTest")
				.param("icon", "test.jpg")
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "본인의 작성한 글만 수정 가능합니다."))
		.andExpect(model().size(2));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void successDelete() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.getOneFreeBoard(1L))
		.thenReturn(response);
		this.mvc.perform(delete("/freeboards/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andExpect(redirectedUrl("/freeboards"));
	}

	@Test
	public void deleteWhenEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(delete("/freeboards/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
				.andExpect(status().isUnauthorized());
//		.andExpect(view().name(containsString("/user/login")))
//		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void deleteWhenNotMatchAuthority() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		UserEntity user2 = UserEntity.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user2)
				.title("title")
				.contents("content")
				.badge("new")
				.count(0)
				.countOfAnswer("")
				.build();
		when(this.freeBoardService.getOneFreeBoard(1L))
		.thenReturn(response);
		this.mvc.perform(delete("/freeboards/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "삭제 권한이 없습니다."))
		.andExpect(model().size(1));
	}

}
