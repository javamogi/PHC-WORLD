package com.phcworld.service.message;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.message.Message;
import com.phcworld.domain.message.MessageRequest;
import com.phcworld.domain.message.MessageResponse;
import com.phcworld.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MessageServiceImplTest {

	@Mock
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
		MessageRequest request = MessageRequest.builder()
				.toUserEmail("test4@test.test")
				.contents("test")
				.build();
		User receiveUser = User.builder()
				.id(2L)
				.email(request.getToUserEmail())
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		
		MessageResponse messageResponse = MessageResponse.builder()
				.id(1L)
				.sender(user)
				.receiver(receiveUser)
				.contents("test")
				.confirm("읽지 않음")
				.className("important")
				.build();
		when(messageService.createMessage(user, receiveUser, "test"))
		.thenReturn(messageResponse);
		MessageResponse message = messageService.createMessage(user, receiveUser, request.getContents());
		assertThat(message, is(messageResponse));
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
		
		MessageResponse messageResponse = MessageResponse.builder()
				.id(1L)
				.sender(user)
				.receiver(receiveUser)
				.contents("test")
				.confirm("읽지 않음")
				.className("important")
				.build();
		
		when(messageService.createMessage(user, receiveUser, "test"))
		.thenReturn(messageResponse);
		
		MessageResponse message = messageService.createMessage(user, receiveUser, "test");
		
		MessageResponse messageConfirm = MessageResponse.builder()
				.id(1L)
				.sender(user)
				.receiver(receiveUser)
				.contents("test")
				.confirm("읽음")
				.className("read")
				.build();
		when(messageService.confirmMessage(1L, receiveUser))
		.thenReturn(messageConfirm);
		
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
		
		MessageResponse messageResponse = MessageResponse.builder()
				.id(1L)
				.sender(user)
				.receiver(receiveUser)
				.contents("test")
				.confirm("읽지 않음")
				.className("important")
				.build();
		
		when(messageService.createMessage(user, receiveUser, "test"))
		.thenReturn(messageResponse);
		
		MessageResponse message = messageService.createMessage(user, receiveUser, "test");
		
		MessageResponse messageResponse2 = MessageResponse.builder()
				.id(2L)
				.sender(user)
				.receiver(receiveUser)
				.contents("test2")
				.confirm("읽지 않음")
				.className("important")
				.build();
		when(messageService.createMessage(user, receiveUser, "test2"))
		.thenReturn(messageResponse2);
		
		MessageResponse message2 = messageService.createMessage(user, receiveUser, "test2");
		
		List<MessageResponse> messageList = new ArrayList<MessageResponse>();
		messageList.add(messageResponse);
		messageList.add(messageResponse2);
		
		when(messageService.findMessageAllBySenderAndNotConfirmUseProfile(receiveUser, "읽지 않음"))
		.thenReturn(messageList);
		
		List<MessageResponse> messageGetList = messageService.findMessageAllBySenderAndNotConfirmUseProfile(receiveUser, "읽지 않음");
		assertThat(messageGetList, hasItems(message, message2));		
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
		
		MessageResponse messageResponse = MessageResponse.builder()
				.id(1L)
				.sender(user)
				.receiver(receiveUser)
				.contents("test")
				.confirm("읽지 않음")
				.className("important")
				.build();
		
		when(messageService.createMessage(user, receiveUser, "test"))
		.thenReturn(messageResponse);
		
		MessageResponse message = messageService.createMessage(user, receiveUser, "test");
		
		MessageResponse messageResponse2 = MessageResponse.builder()
				.id(2L)
				.sender(user)
				.receiver(receiveUser)
				.contents("test2")
				.confirm("읽지 않음")
				.className("important")
				.build();
		when(messageService.createMessage(user, receiveUser, "test2"))
		.thenReturn(messageResponse2);
		
		MessageResponse message2 = messageService.createMessage(user, receiveUser, "test2");
		
		List<MessageResponse> messageList = new ArrayList<MessageResponse>();
		messageList.add(messageResponse);
		messageList.add(messageResponse2);
		
		when(messageService.findMessageBySenderAndConfirmUseMenu(receiveUser, "읽지 않음"))
		.thenReturn(messageList);
		
		List<MessageResponse> messageGetList = messageService.findMessageBySenderAndConfirmUseMenu(receiveUser, "읽지 않음");
		assertThat(messageGetList, hasItems(message, message2));		
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
		
		MessageResponse messageResponse = MessageResponse.builder()
				.id(1L)
				.sender(user)
				.receiver(receiveUser)
				.contents("test")
				.confirm("읽지 않음")
				.className("important")
				.build();
		
		when(messageService.createMessage(user, receiveUser, "test"))
		.thenReturn(messageResponse);
		
		MessageResponse message = messageService.createMessage(user, receiveUser, "test");
		
		MessageResponse messageResponse2 = MessageResponse.builder()
				.id(2L)
				.sender(user)
				.receiver(receiveUser)
				.contents("test2")
				.confirm("읽지 않음")
				.className("important")
				.build();
		when(messageService.createMessage(user, receiveUser, "test2"))
		.thenReturn(messageResponse2);
		
		MessageResponse message2 = messageService.createMessage(user, receiveUser, "test2");
		
		Message mes = Message.builder()
				.sender(user)
				.receiver(receiveUser)
				.contents("contents")
				.confirm("읽지 않음")
				.className("important")
				.sendDate(LocalDateTime.now())
				.build();
		Message mes2 = Message.builder()
				.sender(user)
				.receiver(receiveUser)
				.contents("contents")
				.confirm("읽지 않음")
				.className("important")
				.sendDate(LocalDateTime.now())
				.build();
		
		List<Message> mesList = new ArrayList<Message>();
		mesList.add(mes);
		mesList.add(mes2);
		
		List<MessageResponse> messageList = new ArrayList<MessageResponse>();
		messageList.add(messageResponse);
		messageList.add(messageResponse2);
		
		Page<Message> messagePage = new PageImpl<Message>(mesList);
		when(messageService.findMessageByReceiverMessages(1, receiveUser))
		.thenReturn(messagePage);
		
		Page<Message> messagePageResult = messageService.findMessageByReceiverMessages(1, receiveUser);
		
		
		when(messageService.responseList(messagePageResult))
		.thenReturn(messageList);
		
		List<MessageResponse> messageLists = messageService.responseList(messagePage);
		assertThat(messageLists, hasItems(message, message2));	
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
		
		MessageResponse messageResponse = MessageResponse.builder()
				.id(1L)
				.sender(user)
				.receiver(receiveUser)
				.contents("test")
				.confirm("읽지 않음")
				.className("important")
				.build();
		
		when(messageService.createMessage(user, receiveUser, "test"))
		.thenReturn(messageResponse);
		
		MessageResponse message = messageService.createMessage(user, receiveUser, "test");
		
		MessageResponse messageResponse2 = MessageResponse.builder()
				.id(2L)
				.sender(user)
				.receiver(receiveUser)
				.contents("test2")
				.confirm("읽지 않음")
				.className("important")
				.build();
		when(messageService.createMessage(user, receiveUser, "test2"))
		.thenReturn(messageResponse2);
		
		MessageResponse message2 = messageService.createMessage(user, receiveUser, "test2");
		
		Message mes = Message.builder()
				.sender(user)
				.receiver(receiveUser)
				.contents("contents")
				.confirm("읽지 않음")
				.className("important")
				.sendDate(LocalDateTime.now())
				.build();
		Message mes2 = Message.builder()
				.sender(user)
				.receiver(receiveUser)
				.contents("contents")
				.confirm("읽지 않음")
				.className("important")
				.sendDate(LocalDateTime.now())
				.build();
		
		List<Message> mesList = new ArrayList<Message>();
		mesList.add(mes);
		mesList.add(mes2);
		
		List<MessageResponse> messageList = new ArrayList<MessageResponse>();
		messageList.add(messageResponse);
		messageList.add(messageResponse2);
		
		Page<Message> messagePage = new PageImpl<Message>(mesList);
		when(messageService.findMessageBySenderMessage(1, user))
		.thenReturn(messagePage);
		
		Page<Message> messagePageResult = messageService.findMessageBySenderMessage(1, user);
		
		
		when(messageService.responseList(messagePageResult))
		.thenReturn(messageList);
		
		List<MessageResponse> messageLists = messageService.responseList(messagePage);
		assertThat(messageLists, hasItems(message, message2));	
		
	}

}
