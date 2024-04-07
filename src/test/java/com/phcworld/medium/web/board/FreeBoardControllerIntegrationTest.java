package com.phcworld.medium.web.board;

import com.phcworld.domain.board.dto.FreeBoardResponse;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.service.board.FreeBoardService;
import com.phcworld.utils.HttpSessionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FreeBoardControllerIntegrationTest {
	
	@Autowired
	private MockMvc mvc;

	@SpyBean
	private FreeBoardService freeBoardService;

	@Test
	@WithMockUser(username = "test@test.test", authorities = "ROLE_ADMIN")
	public void 자유게시판_목록_불러오기() throws Exception{
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(1L)
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);

		List<FreeBoardResponse> list = freeBoardService.findFreeBoardAllListAndSetNewBadge();

		this.mvc.perform(get("/freeboards")
						.with(csrf()))
		.andDo(print())
		.andExpect(view().name(containsString("board/freeboard/freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("freeboards", list));
	}
	
	@Test
	public void 자유게시판_등록_페이지_로그인_유저_없음() throws Exception {
		this.mvc.perform(get("/freeboards/form"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/users/loginForm"));
	}
	
	@Test
	@WithMockUser(username = "test@test.test", authorities = "ROLE_ADMIN")
	public void 자유게시판_등록_페이지() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_ADMIN)
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
	@WithMockUser(username = "test@test.test", authorities = "ROLE_ADMIN")
	public void 자유게시판_게시글_등록() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_ADMIN)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		
		this.mvc.perform(post("/freeboards")
						.with(csrf())
				.param("title", "test")
				.param("contents", "test")
				.param("icon", "")
				.session(mockSession))
		.andExpect(status().is3xxRedirection());
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
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/users/loginForm"));
	}
	
	@Test
	@WithMockUser(username = "test@test.test", authorities = "ROLE_ADMIN")
	public void 게시글_하나_요청_로그인_회원_게시글_글쓴이_같음() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_ADMIN)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);

		this.mvc.perform(get("/freeboards/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("board/freeboard/detail_freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("user", true))
		.andExpect(model().attribute("matchUser", true))
		.andExpect(model().attribute("matchAuthority", false))
		.andExpect(model().size(4));
	}

	@Test
	@WithMockUser(username = "test2@test.test", authorities = "ROLE_USER")
	public void 게시글_하나_요청_로그인_회원_게시글_글쓴이_다름() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(2L)
				.email("test2@test.test")
				.password("test2")
				.name("테스트2")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		this.mvc.perform(get("/freeboards/{id}", 1L)
						.with(csrf())
						.session(mockSession))
				.andDo(print())
				.andExpect(view().name(containsString("board/freeboard/detail_freeboard")))
				.andExpect(status().isOk())
				.andExpect(model().attribute("user", true))
				.andExpect(model().attribute("matchUser", false))
				.andExpect(model().attribute("matchAuthority", false))
				.andExpect(model().size(4));
	}
	
	@Test
	@WithMockUser(username = "test@test.test", authorities = "ROLE_ADMIN")
	public void 게시글_하나_요청_관리자_권한() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_ADMIN)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		this.mvc.perform(get("/freeboards/{id}", 4L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("board/freeboard/detail_freeboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("user", true))
		.andExpect(model().attribute("matchUser", false))
		.andExpect(model().attribute("matchAuthority", true))
		.andExpect(model().size(4));
	}
	
	@Test
	@WithMockUser(username = "test@test.test", authorities = "ROLE_ADMIN")
	public void 자유게시판_수정_페이지_요청_성공() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_ADMIN)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		this.mvc.perform(get("/freeboards/{id}/form", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("board/freeboard/freeboard_updateForm")))
		.andExpect(status().isOk())
		.andExpect(model().size(1));
	}
	
	@Test
	public void 자유게시판_수정_페이지_요청_로그인_회원_없음() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(get("/freeboards/{id}/form", 1L)
						.with(csrf())
				.session(mockSession))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/users/loginForm"));
	}
	
	@Test
	@WithMockUser(username = "test2@test.test", authorities = "ROLE_USER")
	public void 자유게시판_수정_페이지_요청_글쓴이_로그인_회원_다름() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(2L)
				.email("test2@test.test")
				.password("test2")
				.name("테스트2")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
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
	@WithMockUser(username = "test@test.test", authorities = "ROLE_ADMIN")
	public void 자유게시판_수정_성공() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_ADMIN)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		this.mvc.perform(patch("/freeboards")
						.with(csrf())
				.param("id", "1")
				.param("contents", "test")
				.session(mockSession))
		.andExpect(redirectedUrl("/freeboards/" + 1L));
	}
	
	@Test
	public void 자유게시판_수정_요청_로그인_회원_없음() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(patch("/freeboards")
						.with(csrf())
				.param("id", "1")
				.param("contents", "updateTest")
				.param("icon", "test.jpg")
				.session(mockSession))
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrlPattern("**/users/loginForm"));
	}
	
	@Test
	@WithMockUser(username = "test2@test.test", authorities = "ROLE_USER")
	public void 자유게시판_수정_요청_글쓴이_로그인_회원_다름() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(2L)
				.email("test2@test.test")
				.password("test2")
				.name("테스트2")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
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
	@WithMockUser(username = "test@test.test", authorities = "ROLE_ADMIN")
	public void 삭제_성공() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(1L)
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		this.mvc.perform(delete("/freeboards/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/freeboards"));
	}

	@Test
	@WithMockUser(username = "test2@test.test", authorities = "ROLE_USER")
	public void 삭제_실패_권한_없음() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(2L)
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		this.mvc.perform(delete("/freeboards/{id}", 1L)
						.with(csrf())
						.session(mockSession))
				.andDo(print())
				.andExpect(view().name(containsString("user/login")))
				.andExpect(status().isOk())
				.andExpect(model().attribute("errorMessage", "삭제 권한이 없습니다."));
	}

	@Test
	@WithMockUser(username = "test2@test.test", authorities = "ROLE_USER")
	public void 삭제_실패_글쓴이_다름() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(2L)
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		this.mvc.perform(delete("/freeboards/{id}", 3L)
						.with(csrf())
						.session(mockSession))
				.andDo(print())
				.andExpect(view().name(containsString("user/login")))
				.andExpect(status().isOk())
				.andExpect(model().attribute("errorMessage", "삭제 권한이 없습니다."));
	}

	@Test
	public void 삭제_요청_로그인_회원_없음() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(delete("/freeboards/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrlPattern("**/users/loginForm"));
	}
	
}
