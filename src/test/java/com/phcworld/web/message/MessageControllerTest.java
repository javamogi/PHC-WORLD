package com.phcworld.web.message;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.phcworld.domain.message.MessageServiceImpl;
import com.phcworld.domain.user.User;
import com.phcworld.domain.user.UserService;
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
		User user = new User("test@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		given(this.userService.findUserByEmail("test2@test.test"))
		.willReturn(null);
		this.mvc.perform(post("/message")
				.param("toUserEmail", "test2@test.test")
				.param("contents", "test")
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("보낼 유저 정보가 없습니다."));
	}
	
	@Test
	public void whenEqualReceiveUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		given(this.userService.findUserByEmail("test@test.test"))
		.willReturn(user);
		this.mvc.perform(post("/message")
				.param("toUserEmail", "test@test.test")
				.param("contents", "test")
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("자신에게는 메세지를 보낼 수 없습니다."));
	}

	@Test
	public void sendMessage() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		User receiveUser = new User("test4@test.test", "test4", "테스트4");
		user.setId(2L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		given(this.userService.findUserByEmail("test4@test.test"))
		.willReturn(receiveUser);
		Message message = new Message(user, receiveUser, "test"); 
		message.setId(1L);
		given(this.messageService.createMessage(user, receiveUser, "test"))
		.willReturn(message);
		this.mvc.perform(post("/message")
				.param("toUserEmail", "test4@test.test")
				.param("contents", "test")
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(message.getId()))
		.andExpect(jsonPath("$.fromUser.id").value(message.getFromUser().getId()))
		.andExpect(jsonPath("$.toUser.id").value(message.getToUser().getId()))
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
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		User receiveUser = new User("test4@test.test", "test4", "테스트4");
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		Message message = new Message(user, receiveUser, "test"); 
		message.setId(1L);
		message.setConfirm("읽음");
		message.setClassName("read");
		given(this.messageService.confirmMessage(message.getId(), user))
		.willReturn(message);
		Message message2 = new Message(user, receiveUser, "test2");
		message.setId(2L);
		List<Message> notReadMessageList = new ArrayList<>();
		notReadMessageList.add(message2);
		given(this.messageService.findMessageAllByToUserAndConfirm(user, "읽지 않음"))
		.willReturn(notReadMessageList);
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
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		User receiveUser = new User("test4@test.test", "test4", "테스트4");
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		Message message = new Message(user, receiveUser, "test"); 
		message.setId(1L);
		message.setConfirm("읽음");
		message.setClassName("read");
		given(this.messageService.confirmMessage(message.getId(), user))
		.willReturn(message);
		Message message2 = new Message(user, receiveUser, "test2");
		message.setId(2L);
		List<Message> notReadMessageList = new ArrayList<>();
		notReadMessageList.add(message2);
		given(this.messageService.findMessageAllByToUserAndConfirm(user, "읽지 않음"))
		.willReturn(notReadMessageList);
		this.mvc.perform(post("/message/info/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.session(mockSession))
		.andExpect(jsonPath("$.fromUser").value(message.getFromUser().getEmail()))
		.andExpect(jsonPath("$.className").value(message.getConfirm()))
		.andExpect(jsonPath("$.countOfMessage").value(notReadMessageList.size()));
	}

}
