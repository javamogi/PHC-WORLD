package com.phcworld.service.message;

import java.util.List;

import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.domain.Page;

import com.phcworld.domain.message.Message;
import com.phcworld.domain.message.MessageResponse;

public interface MessageService {
	MessageResponse createMessage(UserEntity loginUser, UserEntity receiveUser, String contents);
	
	MessageResponse confirmMessage(Long id, UserEntity loginUser);
	
	List<MessageResponse> findMessageAllBySenderAndNotConfirmUseProfile(UserEntity loginUser, String confirm);
	
	List<MessageResponse> findMessageBySenderAndConfirmUseMenu(UserEntity loginUser, String confirm);
	
	Page<Message> findMessageByReceiverMessages(Integer receivePageNum, UserEntity user);
	
	Page<Message> findMessageBySenderMessage(Integer sendPageNum, UserEntity user);
}
