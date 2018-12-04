package com.phcworld.domain.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.user.User;

@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	private MessageRepository messageRepository;

	@Override
	public Message createMessage(User loginUser, User receiveUser, String contents) {
		Message message = new Message(loginUser, receiveUser, contents.replace("\r\n", "<br>"));
		return messageRepository.save(message);
	}
	
	@Override
	public Message confirmMessage(Long id, User loginUser) {
		Message message = messageRepository.getOne(id);
		if(!loginUser.matchId(message.getToUser().getId())) {
			throw new MatchNotUserExceptioin("본인만 확인 가능합니다.");
		}
		message.setConfirm("읽음");
		message.setClassName("read");
		return messageRepository.save(message);
	}
	
	@Override
	public List<Message> findMessageAllByToUserAndConfirm(User loginUser, String confirm) {
		return messageRepository.findAllByToUserAndConfirm(loginUser, confirm);
	}
	
	@Override
	public List<Message> findMessageByRequestPageToUserAndConfirm(User loginUser, String confirm) {
		PageRequest pageRequest = PageRequest.of(0, 5, new Sort(Direction.DESC, "id"));
		Page<Message> pageMessage = messageRepository.findByToUserAndConfirm(loginUser, "읽지 않음", pageRequest);
		return pageMessage.getContent();
	}
	
	@Override
	public Page<Message> findMessageByReceiveMessages(Integer receivePageNum, User user) {
		PageRequest pageRequest = PageRequest.of(0, 10, new Sort(Direction.DESC, "id"));
		if(receivePageNum > 1) {
			pageRequest = PageRequest.of(receivePageNum - 1, 10, new Sort(Direction.DESC, "id"));
		}
		return messageRepository.findByToUser(user, pageRequest);
	}
	
	@Override
	public Page<Message> findMessageBySendMessage(Integer sendPageNum, User user) {
		PageRequest pageRequest = PageRequest.of(0, 10, new Sort(Direction.DESC, "id"));
		if(sendPageNum > 1) {
			pageRequest = PageRequest.of(sendPageNum - 1, 10, new Sort(Direction.DESC, "id"));
		}
		return messageRepository.findByFromUser(user, pageRequest);
	}

	public Message getOneMessage(Long id) {
		return messageRepository.getOne(id);
	}

}
