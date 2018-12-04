package com.phcworld.web.user;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.alert.AlertServiceImpl;
import com.phcworld.domain.email.EmailAuth;
import com.phcworld.domain.email.EmailService;
import com.phcworld.domain.message.Message;
import com.phcworld.domain.message.MessageServiceImpl;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.timeline.TimelineServiceImpl;
import com.phcworld.domain.user.User;
import com.phcworld.domain.user.UserService;
import com.phcworld.web.HttpSessionUtils;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
	
	private static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);
	
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
	private EmailService emailService;
	
	@MockBean
	private HttpSessionUtils sessionUtil;

	@Test
	public void userServiceAutowired() throws Exception {
		assertNotNull(userService);
	}
	
	@Test
	public void whenSuccessLoginForm() throws Exception {
		this.mvc.perform(get("/users/loginForm"))
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void whenSuccessLogout() throws Exception {
		this.mvc.perform(get("/users/logout"))
		.andExpect(redirectedUrl("/users/loginForm"));
	}
	
	@Test
	public void whenSuccessLoginUser() throws Exception {
		User user = new User("test@test.test", "test", "테스트");
		user.setAuthority("ROLE_ADMIN");
		given(this.userService.findUserByEmail("test@test.test"))
		.willReturn(user);
		EmailAuth emailAuth = new EmailAuth("test@test.test", "1234");
		emailAuth.setConfirm("Y");
		given(this.emailService.findByEmail("test@test.test"))
		.willReturn(emailAuth);
		this.mvc.perform(post("/users/login")
				.param("email", "test@test.test")
				.param("password", "test"))
		.andDo(print())
		.andExpect(redirectedUrl("/dashboard"));
	}
	
	@Test
	public void whenIsNotFoundEmailLoginUser() throws Exception {
		given(this.userService.findUserByEmail("test@test.test"))
		.willReturn(new User("test@test.test", "test", "테스트"));//하나의 유저 데이터 입력
		this.mvc.perform(post("/users/login")
				.param("email", "test2@test.test") //없는 데이터 
				.param("password", "test"))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "존재하지 않는 이메일입니다."))
		.andExpect(model().size(1));
	}
		
	@Test
	public void whenIsNotMatchPasswordLoginUser() throws Exception {
		given(this.userService.findUserByEmail("test@test.test"))
		.willReturn(new User("test@test.test", "test", "테스트"));
		this.mvc.perform(post("/users/login")
				.param("email", "test@test.test")
				.param("password", "test1"))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "비밀번호가 틀립니다."))
		.andExpect(model().size(1));
	}
	
	@Test
	public void whenIsNotEmailAuthLoginUser() throws Exception {
		given(this.userService.findUserByEmail("test@test.test"))
		.willReturn(new User("test@test.test", "test", "테스트"));
		given(this.emailService.findByEmail("test@test.test"))
		.willReturn(new EmailAuth("test@test.test", "1234"));
		this.mvc.perform(post("/users/login")
				.param("email", "test@test.test")
				.param("password", "test"))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "이메일 인증이 안됐습니다. 메일에서 인증하세요."))
		.andExpect(model().size(1));
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
	public void whenIsNotEmailFailedCreateUser() throws Exception {
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
	public void whenIsNotPasswordFailedCreateUser() throws Exception {
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
	public void whenIsNotNameFailedCreateUser() throws Exception {
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
	public void whenIsOverlapEmailFailedCreateUser() throws Exception {
		given(this.userService.findUserByEmail("test@test.test"))
		.willReturn(new User("test@test.test", "test", "테스트"));
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
	public void whenSuccessUpdateForm() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
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
	public void whenUpdateFormEmptySession() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(get("/users/{id}/form", 1L)
				.session(mockSession))
		.andExpect(view().name("/user/login"))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "로그인이 필요합니다."))
		.andExpect(model().size(1));
	}
	
	@Test
	public void whenUpdateFormNotMatchId() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test@test.test", "test", "테스트");
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
	
	@Test//session 체크
	public void whenSuccessUpdateUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test@test.test", "test", "테스트");
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
	public void whenUpdateUserEmptySession() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(put("/users/{id}", 1L)
				.session(mockSession))
		.andExpect(view().name("/user/login"))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "로그인이 필요합니다."))
		.andExpect(model().size(2));
	}
	
	@Test
	public void whenUpdateUserNotMatchId() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test@test.test", "test", "테스트");
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
	public void whenIsNotPasswordUpdateUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test@test.test", "test", "테스트");
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
	public void whenIsNotNameUpdateUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test@test.test", "test", "테스트");
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
	public void whenNotEqualLoginUserProfile() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User loginUser = new User("test@test.test", "test", "테스트");
		loginUser.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, loginUser);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user = new User("test2@test.test", "test2", "테스트2");
		given(this.userService.findUserById(2L))
		.willReturn(user);
		Page<Timeline> timelines = timelineService.findPageTimelineByUser(user);
		given(this.timelineService.findPageTimelineByUser(user))
		.willReturn(timelines);
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
	public void whenEqualLoginUserNotMessageProfile() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User loginUser = new User("test@test.test", "test", "테스트");
		loginUser.setId(1L);
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
