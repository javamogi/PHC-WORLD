package com.phcworld.domain.message;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phcworld.domain.user.User;

public interface MessageService {
	Message createMessage(User loginUser, User receiveUser, String contents);
	
	Message confirmMessage(Long id, User loginUser);
	
	List<Message> findMessageAllByToUserAndConfirm(User loginUser, String confirm);
	
	List<Message> findMessageByRequestPageToUserAndConfirm(User loginUser, String confirm);
	
	Page<Message> findMessageByReceiveMessages(Integer receivePageNum, User user);
	
	Page<Message> findMessageBySendMessage(Integer sendPageNum, User user);
}
