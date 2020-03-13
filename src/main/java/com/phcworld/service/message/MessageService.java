package com.phcworld.service.message;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phcworld.domain.message.Message;
import com.phcworld.domain.user.User;

public interface MessageService {
	Message createMessage(User loginUser, User receiveUser, String contents);
	
	Message confirmMessage(Long id, User loginUser);
	
	List<Message> findMessageAllBySenderAndNotConfirmUseProfile(User loginUser, String confirm);
	
	List<Message> findMessageBySenderAndConfirmUseMenu(User loginUser, String confirm);
	
	Page<Message> findMessageByReceiverMessages(Integer receivePageNum, User user);
	
	Page<Message> findMessageBySenderMessage(Integer sendPageNum, User user);
}
