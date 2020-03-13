package com.phcworld.service.message;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.message.Message;
import com.phcworld.domain.user.User;
import com.phcworld.repository.message.MessageRepository;

@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	private MessageRepository messageRepository;

	@Override
	public Message createMessage(User loginUser, User receiveUser, String contents) {
		Message message = Message.builder()
				.sender(loginUser)
				.receiver(receiveUser)
				.contents(contents.replace("\r\n", "<br>"))
				.confirm("읽지 않음")
				.className("important")
				.sendDate(LocalDateTime.now())
				.build();
		return messageRepository.save(message);
	}
	
	@Override
	public Message confirmMessage(Long id, User loginUser) {
		Message message = messageRepository.getOne(id);
		if(!loginUser.matchId(message.getReceiver().getId())) {
			throw new MatchNotUserExceptioin("본인만 확인 가능합니다.");
		}
		message.setConfirm("읽음");
		message.setClassName("read");
		return messageRepository.save(message);
	}
	
	@Override
	public List<Message> findMessageAllBySenderAndNotConfirmUseProfile(User loginUser, String confirm) {
		return messageRepository.findAllByReceiverAndConfirm(loginUser, confirm);
	}
	
	@Override
	public List<Message> findMessageBySenderAndConfirmUseMenu(User loginUser, String confirm) {
		PageRequest pageRequest = PageRequest.of(0, 5, new Sort(Direction.DESC, "id"));
		Page<Message> pageMessage = messageRepository.findByReceiverAndConfirm(loginUser, "읽지 않음", pageRequest);
		return pageMessage.getContent();
	}
	
	@Override
	public Page<Message> findMessageByReceiverMessages(Integer receivePageNum, User user) {
		PageRequest pageRequest = PageRequest.of(0, 10, new Sort(Direction.DESC, "id"));
		if(receivePageNum > 1) {
			pageRequest = PageRequest.of(receivePageNum - 1, 10, new Sort(Direction.DESC, "id"));
		}
		return messageRepository.findByReceiver(user, pageRequest);
	}
	
	@Override
	public Page<Message> findMessageBySenderMessage(Integer sendPageNum, User user) {
		PageRequest pageRequest = PageRequest.of(0, 10, new Sort(Direction.DESC, "id"));
		if(sendPageNum > 1) {
			pageRequest = PageRequest.of(sendPageNum - 1, 10, new Sort(Direction.DESC, "id"));
		}
		return messageRepository.findBySender(user, pageRequest);
	}

	public Message getOneMessage(Long id) {
		return messageRepository.getOne(id);
	}

}
