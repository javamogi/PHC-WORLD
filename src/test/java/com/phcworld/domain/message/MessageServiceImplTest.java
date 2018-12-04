package com.phcworld.domain.message;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MessageServiceImplTest {

	@Autowired
	private MessageServiceImpl messageService;
	
	@Test
	@Transactional
	public void createMessage() {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		User receiveUser = new User("test4@test.test", "test4", "테스트4");
		user.setId(2L);
		Message message = messageService.createMessage(user, receiveUser, "test");
		Message actual = messageService.getOneMessage(message.getId());
		assertThat(actual, is(message));
	}
	
	@Test
	@Transactional
	public void confirmMessage() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		User receiveUser = new User("test4@test.test", "test4", "테스트4");
		receiveUser.setId(2L);
		Message message = messageService.createMessage(user, receiveUser, "test");
		Message actual = messageService.confirmMessage(message.getId(), receiveUser);
		assertThat(actual.getConfirm(), is("읽음"));
		assertThat(actual.getClassName(), is("read"));
	}
	
	@Test
	@Transactional
	public void findMessageAllByToUserAndConfirm() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		User receiveUser = new User("test4@test.test", "test4", "테스트4");
		receiveUser.setId(2L);
		Message message = messageService.createMessage(user, receiveUser, "test");
		Message message2 = messageService.createMessage(user, receiveUser, "test2");
		List<Message> messageList = messageService.findMessageAllByToUserAndConfirm(receiveUser, "읽지 않음");
		assertThat(messageList, hasItems(message, message2));		
	}
	
	@Test
	@Transactional
	public void findMessageByRequestPageToUserAndConfirm() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		User receiveUser = new User("test4@test.test", "test4", "테스트4");
		receiveUser.setId(2L);
		Message message = messageService.createMessage(user, receiveUser, "test");
		Message message2 = messageService.createMessage(user, receiveUser, "test2");
		List<Message> messageList = messageService.findMessageByRequestPageToUserAndConfirm(receiveUser, "읽지 않음");
		assertThat(messageList, hasItems(message, message2));		
	}
	
	@Test
	@Transactional
	public void findMessageByReceiveMessages() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		User receiveUser = new User("test4@test.test", "test4", "테스트4");
		receiveUser.setId(2L);
		Message message = messageService.createMessage(user, receiveUser, "test");
		Message message2 = messageService.createMessage(user, receiveUser, "test2");
		Page<Message> messagePage = messageService.findMessageByReceiveMessages(1, receiveUser);
		List<Message> messageList = messagePage.getContent();
		assertThat(messageList, hasItems(message, message2));	
	}
	
	@Test
	@Transactional
	public void findMessageBySendMessage() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		User receiveUser = new User("test4@test.test", "test4", "테스트4");
		receiveUser.setId(2L);
		Message message = messageService.createMessage(user, receiveUser, "test");
		Message message2 = messageService.createMessage(user, receiveUser, "test2");
		Page<Message> messagePage = messageService.findMessageBySendMessage(1, user);
		List<Message> messageList = messagePage.getContent();
		assertThat(messageList, hasItems(message, message2));	
	}

}
