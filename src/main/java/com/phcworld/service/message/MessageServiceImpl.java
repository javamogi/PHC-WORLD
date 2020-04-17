package com.phcworld.service.message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.message.Message;
import com.phcworld.domain.message.MessageResponse;
import com.phcworld.domain.user.User;
import com.phcworld.repository.message.MessageRepository;

@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	private MessageRepository messageRepository;

	@Override
	public MessageResponse createMessage(User loginUser, User receiveUser, String contents) {
		Message message = Message.builder()
				.sender(loginUser)
				.receiver(receiveUser)
				.contents(contents.replace("\r\n", "<br>"))
				.confirm("읽지 않음")
				.className("important")
				.sendDate(LocalDateTime.now())
				.build();
		Message createdMessage = messageRepository.save(message);
		return response(createdMessage);
	}
	
	@Override
	public MessageResponse confirmMessage(Long id, User loginUser) {
		Message message = messageRepository.getOne(id);
		if(!loginUser.matchId(message.getReceiver().getId())) {
			throw new MatchNotUserExceptioin("본인만 확인 가능합니다.");
		}
		message.setConfirm("읽음");
		message.setClassName("read");
		
		Message createdMessage = messageRepository.save(message);
		return response(createdMessage);
	}

	@Override
	public List<MessageResponse> findMessageAllBySenderAndNotConfirmUseProfile(User loginUser, String confirm) {
		List<Message> messageList = messageRepository.findAllByReceiverAndConfirm(loginUser, confirm);
		if(messageList != null) {
			List<MessageResponse> messageResponseList = messageList.stream()
					.map(message -> {
						return response(message);
					})
					.collect(Collectors.toList());
			return messageResponseList;
		}
		return null;
	}
	
	@Override
	public List<MessageResponse> findMessageBySenderAndConfirmUseMenu(User loginUser, String confirm) {
		PageRequest pageRequest = PageRequest.of(0, 5, new Sort(Direction.DESC, "id"));
		Page<Message> pageMessage = messageRepository.findByReceiverAndConfirm(loginUser, "읽지 않음", pageRequest);
		List<Message> messageList = pageMessage.getContent();
		if(messageList != null) {
			List<MessageResponse> messageResponseList = messageList.stream()
					.map(message -> {
						return response(message);
					})
					.collect(Collectors.toList());
			return messageResponseList;
		}
		return null;
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

	public MessageResponse getOneMessage(Long id) {
		Message message = messageRepository.getOne(id);
		return response(message);
	}
	
	private MessageResponse response(Message message) {
		MessageResponse messageResponse = MessageResponse.builder()
				.id(message.getId())
				.sender(message.getSender())
				.receiver(message.getReceiver())
				.contents(message.getContents())
				.className(message.getClassName())
				.confirm(message.getConfirm())
				.sendDate(message.getFormattedCreateDate())
				.build();
		return messageResponse;
	}
	
	public List<MessageResponse> responseList(Page<Message> pageMessage){
		List<Message> messageList = pageMessage.getContent();
		List<MessageResponse> messageResponseList = messageList.stream()
				.map(message -> {
					MessageResponse response = response(message);
					return response;
				})
				.collect(Collectors.toList());
		return messageResponseList;
	}

}
