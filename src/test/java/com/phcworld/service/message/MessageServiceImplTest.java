package com.phcworld.service.message;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.message.Message;
import com.phcworld.domain.message.MessageResponse;
import com.phcworld.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MessageServiceImplTest {

	@Autowired
	private MessageServiceImpl messageService;
	
	@Test
	@Transactional
	public void createMessage() {
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
		MessageResponse message = messageService.createMessage(user, receiveUser, "test");
		MessageResponse actual = messageService.getOneMessage(1L);
		assertThat(actual, is(message));
	}
	
	@Test
	@Transactional
	public void confirmMessage() throws Exception {
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
		MessageResponse message = messageService.createMessage(user, receiveUser, "test");
		MessageResponse actual = messageService.confirmMessage(message.getId(), receiveUser);
		assertThat(actual.getConfirm(), is("읽음"));
		assertThat(actual.getClassName(), is("read"));
	}
	
	@Test
	@Transactional
	public void findMessageAllByToUserAndConfirm() throws Exception {
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
		MessageResponse message = messageService.createMessage(user, receiveUser, "test");
		MessageResponse message2 = messageService.createMessage(user, receiveUser, "test2");
		List<MessageResponse> messageList = messageService.findMessageAllBySenderAndNotConfirmUseProfile(receiveUser, "읽지 않음");
		assertThat(messageList, hasItems(message, message2));		
	}
	
	@Test
	@Transactional
	public void findMessageByRequestPageToUserAndConfirm() throws Exception {
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
		MessageResponse message = messageService.createMessage(user, receiveUser, "test");
		MessageResponse message2 = messageService.createMessage(user, receiveUser, "test2");
		List<MessageResponse> messageList = messageService.findMessageBySenderAndConfirmUseMenu(receiveUser, "읽지 않음");
		assertThat(messageList, hasItems(message, message2));		
	}
	
	@Test
	@Transactional
	public void findMessageByReceiveMessages() throws Exception {
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
		MessageResponse message = messageService.createMessage(user, receiveUser, "test");
		MessageResponse message2 = messageService.createMessage(user, receiveUser, "test2");
		Page<Message> messagePage = messageService.findMessageByReceiverMessages(1, receiveUser);
		List<MessageResponse> messageList = messageService.responseList(messagePage);
		assertThat(messageList, hasItems(message, message2));	
	}
	
	@Test
	@Transactional
	public void findMessageBySendMessage() throws Exception {
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
		MessageResponse message = messageService.createMessage(user, receiveUser, "test");
		MessageResponse message2 = messageService.createMessage(user, receiveUser, "test2");
		Page<Message> messagePage = messageService.findMessageBySenderMessage(1, user);
		List<MessageResponse> messageList = messageService.responseList(messagePage);
		assertThat(messageList, hasItems(message, message2));	
	}

}
