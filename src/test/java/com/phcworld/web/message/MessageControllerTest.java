package com.phcworld.web.message;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.message.Message;
import com.phcworld.domain.user.User;
import com.phcworld.service.message.MessageServiceImpl;
import com.phcworld.service.user.UserService;
import com.phcworld.web.HttpSessionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(MessageController.class)
public class MessageControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;

	@MockBean
	private MessageServiceImpl messageService;
	
	@Test
	public void whenEmptyLoginUser() throws Exception {
		this.mvc.perform(post("/message")
				.param("toUserEmail", "test2@test.test")
				.param("contents", "test"))
		.andExpect(jsonPath("$.error").value("로그인을 해야합니다."));
	}
	
	@Test
	public void whenEmptyReceiveUser() throws Exception {
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
		when(this.userService.findUserByEmail("test2@test.test"))
		.thenReturn(null);
		this.mvc.perform(post("/message")
				.param("toUserEmail", "test2@test.test")
				.param("contents", "test")
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("보낼 유저 정보가 없습니다."));
	}
	
	@Test
	public void whenEqualReceiveUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		when(this.userService.findUserByEmail("test@test.test"))
		.thenReturn(user);
		this.mvc.perform(post("/message")
				.param("toUserEmail", "test@test.test")
				.param("contents", "test")
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("자신에게는 메세지를 보낼 수 없습니다."));
	}

	@Test
	public void sendMessage() throws Exception {
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
		User receiveUser = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		when(this.userService.findUserByEmail("test4@test.test"))
		.thenReturn(receiveUser);
		Message message = Message.builder()
				.id(1L)
				.sender(user)
				.receiver(receiveUser)
				.contents("test")
				.confirm("읽지 않음")
				.className("important")
				.sendDate(LocalDateTime.now())
				.build();
		when(this.messageService.createMessage(user, receiveUser, "test"))
		.thenReturn(message);
		this.mvc.perform(post("/message")
				.param("toUserEmail", "test4@test.test")
				.param("contents", "test")
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(message.getId()))
		.andExpect(jsonPath("$.sender.id").value(message.getSender().getId()))
		.andExpect(jsonPath("$.receiver.id").value(message.getReceiver().getId()))
		.andExpect(jsonPath("$.contents").value(message.getContents()));
	}
	
	@Test
	public void whenEmptyLoginUserConfirmMessage() throws Exception {
		this.mvc.perform(get("/message/{id}", 1L))
		.andExpect(jsonPath("$.error").value("로그인을 해야합니다."));
	}
	
	@Test
	public void confirmMessage() throws Exception {
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
		User receiveUser = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		Message message = Message.builder()
				.id(1L)
				.sender(user)
				.receiver(receiveUser)
				.contents("test")
				.confirm("읽지 않음")
				.className("important")
				.sendDate(LocalDateTime.now())
				.build();
		message.setConfirm("읽음");
		message.setClassName("read");
		when(this.messageService.confirmMessage(message.getId(), user))
		.thenReturn(message);
		Message message2 = Message.builder()
				.id(2L)
				.sender(user)
				.receiver(receiveUser)
				.contents("test2")
				.confirm("읽지 않음")
				.className("important")
				.sendDate(LocalDateTime.now())
				.build();
		List<Message> notReadMessageList = new ArrayList<>();
		notReadMessageList.add(message2);
		when(this.messageService.findMessageAllBySenderAndNotConfirmUseProfile(user, "읽지 않음"))
		.thenReturn(notReadMessageList);
		this.mvc.perform(get("/message/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.session(mockSession))
		.andExpect(jsonPath("$.className").value(message.getConfirm()))
		.andExpect(jsonPath("$.countOfMessage").value(notReadMessageList.size()));
	}
	
	@Test
	public void whenEmptyLoginUserGetToUserInfo() throws Exception {
		this.mvc.perform(post("/message/info/{id}", 1L))
		.andExpect(jsonPath("$.error").value("로그인을 해야합니다."));
	}
	
	@Test
	public void getToUserInfo() throws Exception {
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
		User receiveUser = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		Message message = Message.builder()
				.id(1L)
				.sender(user)
				.receiver(receiveUser)
				.contents("test")
				.confirm("읽지 않음")
				.className("important")
				.sendDate(LocalDateTime.now())
				.build();
		message.setConfirm("읽음");
		message.setClassName("read");
		when(this.messageService.confirmMessage(message.getId(), user))
		.thenReturn(message);
		Message message2 = Message.builder()
				.id(2L)
				.sender(user)
				.receiver(receiveUser)
				.contents("test2")
				.confirm("읽지 않음")
				.className("important")
				.sendDate(LocalDateTime.now())
				.build();
		List<Message> notReadMessageList = new ArrayList<>();
		notReadMessageList.add(message2);
		when(this.messageService.findMessageAllBySenderAndNotConfirmUseProfile(user, "읽지 않음"))
		.thenReturn(notReadMessageList);
		this.mvc.perform(post("/message/info/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.session(mockSession))
		.andExpect(jsonPath("$.fromUser").value(message.getSender().getEmail()))
		.andExpect(jsonPath("$.className").value(message.getConfirm()))
		.andExpect(jsonPath("$.countOfMessage").value(notReadMessageList.size()));
	}

}
