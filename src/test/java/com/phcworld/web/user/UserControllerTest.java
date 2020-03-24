package com.phcworld.web.user;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.emailAuth.EmailAuth;
import com.phcworld.domain.message.Message;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.emailAuth.EmailAuthService;
import com.phcworld.service.message.MessageServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;
import com.phcworld.service.user.UserService;
import com.phcworld.utils.HttpSessionUtils;

import lombok.extern.slf4j.Slf4j;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(UserController.class)
@Slf4j
public class UserControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private TimelineServiceImpl timelineService;
	
	@MockBean
	private MessageServiceImpl messageService;
	
	@MockBean
	private AlertServiceImpl alertService;
	
	@MockBean
	private EmailAuthService emailService;
	
	@MockBean
	private HttpSessionUtils sessionUtil;

	@Test
	public void userServiceAutowired() throws Exception {
		assertNotNull(userService);
	}
	
	@Test
	public void successCreateUser() throws Exception {
		this.mvc.perform(post("/users")
				.param("email", "test@test.test")
				.param("password", "test")
				.param("name", "테스트"))
		.andDo(print())
		.andExpect(redirectedUrl("/users/loginForm"));
	}
	
	@Test
	public void createWhenIsNotEmailValid() throws Exception {
		this.mvc.perform(post("/users")
				.param("email", "test")
				.param("password", "test")
				.param("name", "테스트"))
		.andDo(print())
		.andExpect(view().name(containsString("/user/form")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "이메일 형식이 아닙니다."))
		.andExpect(model().size(2));
	}
	
	@Test
	public void createWhenIsNotPasswordValid() throws Exception {
		this.mvc.perform(post("/users")
				.param("email", "test@test.test")
				.param("password", "te")
				.param("name", "테스트"))
		.andDo(print())
		.andExpect(view().name(containsString("/user/form")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "4자 이상으로 해야합니다."))
		.andExpect(model().size(2));
	}
		
	@Test
	public void createWhenIsNotNameValid() throws Exception {
		this.mvc.perform(post("/users")
				.param("email", "test@test.test")
				.param("password", "test")
				.param("name", "테스"))
		.andDo(print())
		.andExpect(view().name(containsString("/user/form")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "영문 3자 이상 20자 이하 또는 한글 두자이상 6자 이하로 해야합니다."))
		.andExpect(model().size(2));
	}
		
	@Test
	public void createWhenIsOverlapEmail() throws Exception {
		User user = User.builder()
				.email("test3@test.test")
				.password("test3")
				.name("test3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		when(this.userService.findUserByEmail("test@test.test"))
		.thenReturn(user);
		this.mvc.perform(post("/users")
				.param("email", "test@test.test")
				.param("password", "test")
				.param("name", "테스트"))
		.andDo(print())
		.andExpect(view().name(containsString("/user/form")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "이미 등록된 이메일입니다."))
		.andExpect(model().size(2));
	}
	
	@Test
	public void successLoginForm() throws Exception {
		this.mvc.perform(get("/users/loginForm"))
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void successLogout() throws Exception {
		this.mvc.perform(get("/users/logout"))
		.andExpect(redirectedUrl("/users/loginForm"));
	}
	
	@Test
	public void successLoginUser() throws Exception {
		User user = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_ADMIN")
				.createDate(LocalDateTime.now())
				.build();
		when(this.userService.findUserByEmail("test@test.test"))
		.thenReturn(user);
		EmailAuth emailAuth = EmailAuth.builder()
				.email("test@test.test")
				.authKey("1234")
				.authenticate(true)
				.build();
		when(this.emailService.findByEmail("test@test.test"))
		.thenReturn(emailAuth);
		this.mvc.perform(post("/users/login")
				.param("email", "test@test.test")
				.param("password", "test"))
		.andDo(print())
		.andExpect(redirectedUrl("/dashboard"));
	}
	
	@Test
	public void loginWhenIsNotFoundEmailLoginUser() throws Exception {
		User user = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		when(this.userService.findUserByEmail("test@test.test"))
		.thenReturn(user);
		this.mvc.perform(post("/users/login")
				.param("email", "test2@test.test")
				.param("password", "test"))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "존재하지 않는 이메일입니다."))
		.andExpect(model().size(2));
	}
		
	@Test
	public void loginWhenIsNotMatchPasswordLoginUser() throws Exception {
		User user = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		when(this.userService.findUserByEmail("test@test.test"))
		.thenReturn(user);
		this.mvc.perform(post("/users/login")
				.param("email", "test@test.test")
				.param("password", "test1"))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "비밀번호가 틀립니다."))
		.andExpect(model().size(2));
	}
	
	@Test
	public void loginWhenIsNotEmailAuthLoginUser() throws Exception {
		User user = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		when(this.userService.findUserByEmail("test@test.test"))
		.thenReturn(user);
		EmailAuth emailAuth = EmailAuth.builder()
				.email("test@test.test")
				.authKey("1234")
				.authenticate(false)
				.build();
		when(this.emailService.findByEmail("test@test.test"))
		.thenReturn(emailAuth);
		this.mvc.perform(post("/users/login")
				.param("email", "test@test.test")
				.param("password", "test"))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "이메일 인증이 안됐습니다. 메일에서 인증하세요."))
		.andExpect(model().size(2));
	}
	
	@Test
	public void successUpdateForm() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		log.debug("session User : {}", mockSession.getAttribute(HttpSessionUtils.USER_SESSION_KEY));
		this.mvc.perform(get("/users/{id}/form", user.getId())
				.session(mockSession))
		.andExpect(view().name("/user/updateForm"))
		.andExpect(status().isOk())
		.andExpect(model().attribute("user", user))
		.andExpect(model().size(1));
	}
	
	@Test
	public void requestUpdateFormWhenIsEmptySession() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(get("/users/{id}/form", 1L)
				.session(mockSession))
		.andExpect(view().name("/user/login"))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "로그인이 필요합니다."))
		.andExpect(model().size(1));
	}
	
	@Test
	public void requestUpdateFormWhenIsNotMatchId() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);//session에 들어간 상태라면 login페이지의 session이 다 들어가야한다.
		this.mvc.perform(get("/users/{id}/form", 2L)
				.session(mockSession))
		.andExpect(view().name("/user/login"))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "본인의 정보만 수정 가능합니다."))
		.andExpect(model().size(1));
	}
	
	@Test
	public void successUpdateUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		log.debug("session User : {}", mockSession.getAttribute(HttpSessionUtils.USER_SESSION_KEY));
		this.mvc.perform(put("/users/{id}", 1L)
				.param("email", "test@test.test")
				.param("password", "test")
				.param("name", "테스트1")
				.session(mockSession))
		.andDo(print())
		.andExpect(redirectedUrl("/dashboard"));
	}
	
	@Test
	public void updateUserWhenIsUpdateUserEmptySession() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(put("/users/{id}", 1L)
				.session(mockSession))
		.andExpect(view().name("/user/login"))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "로그인이 필요합니다."))
		.andExpect(model().size(2));
	}
	
	@Test
	public void updateUserWhenIsUpdateUserNotMatchId() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		this.mvc.perform(put("/users/{id}", 2L)
				.session(mockSession))
		.andExpect(view().name("/user/login"))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "본인의 정보만 수정 가능합니다."))
		.andExpect(model().size(2));
	}
	
	@Test
	public void updateUserWhenIsNotPasswordValidUpdateUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		this.mvc.perform(put("/users/{id}", 1L)
				.param("email", "test@test.test")
				.param("password", "te")
				.param("name", "테스트")
				.session(mockSession))
		.andExpect(redirectedUrl(String.format("/users/%d/form", 1L)));
	}
	
	@Test
	public void updateUserWhenIsNotNameValidUpdateUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		this.mvc.perform(put("/users/{id}", 1L)
				.param("email", "test@test.test")
				.param("password", "test")
				.param("name", "테")
				.session(mockSession))
		.andExpect(redirectedUrl(String.format("/users/%d/form", 1L)));
	}
	
	@Test
	public void requestProfileWhenIsNotEqualLoginUserProfile() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User loginUser = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		loginUser.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, loginUser);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user = User.builder()
				.email("test2@test.test")
				.password("test2")
				.name("테스트2")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		when(this.userService.findUserById(2L))
		.thenReturn(user);
		List<Timeline> timelines = timelineService.findTimelineList(0, user);
		when(this.timelineService.findTimelineList(0, user))
		.thenReturn(timelines);
		this.mvc.perform(get("/users/{id}/profile", 2L)
				.param("id", "2L")
				.session(mockSession))
		.andExpect(view().name("/user/profile"))
		.andExpect(status().isOk())
		.andExpect(model().attribute("show more", false))
		.andExpect(model().attribute("user", user))
		.andExpect(model().attribute("timeline", timelines))
		.andExpect(model().size(3));
	}
	
	@Test
	public void requestProfileWhenIsEqualLoginUserNotMessageProfile() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User loginUser = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		loginUser.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, loginUser);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		when(this.userService.findUserById(1L))
		.thenReturn(loginUser);
		List<Timeline> timelines = timelineService.findTimelineList(0, loginUser);
		when(this.timelineService.findTimelineList(0, loginUser))
		.thenReturn(timelines);
		Page<Message> pageReceiveMessages = messageService.findMessageByReceiverMessages(1, loginUser);
		when(this.messageService.findMessageByReceiverMessages(1, loginUser))
		.thenReturn(pageReceiveMessages);
		Page<Message> pageSendMessages = messageService.findMessageByReceiverMessages(1, loginUser);
		when(this.messageService.findMessageByReceiverMessages(1, loginUser))
		.thenReturn(pageSendMessages);
		this.mvc.perform(get("/users/{id}/profile", 1L)
				.param("id", "1L")
				.session(mockSession))
		.andExpect(view().name("/user/profile"))
		.andExpect(status().isOk())
		.andExpect(model().attribute("show more", false))
		.andExpect(model().attribute("equalLoginUser", true))
		.andExpect(model().attribute("user", loginUser))
		.andExpect(model().attribute("timeline", timelines))
		.andExpect(model().size(4));
	}
	
	/* messageServiceTest 후에 받은메세지와 보낸메세지가 있을때 상황
	@Test
	public void whenEqualLoginUserProfile() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User loginUser = new User(1L, "test@test.test", "test", "테스트");
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, loginUser);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		given(this.userService.findUserById(1L))
		.willReturn(loginUser);
		Page<Timeline> timelines = timelineService.findPageTimelineByUser(loginUser);
		given(this.timelineService.findPageTimelineByUser(loginUser))
		.willReturn(timelines);
		Page<Message> pageReceiveMessages = messageService.findMessageByReceiveMessages(1, loginUser);
		given(this.messageService.findMessageByReceiveMessages(1, loginUser))
		.willReturn(pageReceiveMessages);
		Page<Message> pageSendMessages = messageService.findMessageByReceiveMessages(1, loginUser);
		given(this.messageService.findMessageByReceiveMessages(1, loginUser))
		.willReturn(pageSendMessages);
		this.mvc.perform(get("/users/{id}/profile", 1L)
				.param("id", "1L")
//				.param("sendPageNum", "1")
//				.param("receivePageNum", "1")
				.session(mockSession))
		.andExpect(view().name("/user/profile"))
		.andExpect(status().isOk())
		.andExpect(model().attribute("show more", false))
//		.andExpect(model().attribute("receivepreviousPage", null))
//		.andExpect(model().attribute("receivenextPage", null))
//		.andExpect(model().attribute("receivePageNations", 1))
//		.andExpect(model().attribute("sendpreviousPage", null))
//		.andExpect(model().attribute("sendnextPage", null))
//		.andExpect(model().attribute("sendPageNations", 1))
//		.andExpect(model().attribute("receiveMessages", pageReceiveMessages.getContent()))
//		.andExpect(model().attribute("sendMessages", pageSendMessages.getContent()))
		.andExpect(model().attribute("equalLoginUser", true))
		.andExpect(model().attribute("user", loginUser))
		.andExpect(model().attribute("timeline", timelines))
		.andExpect(model().size(4));
	}
	*/
}
