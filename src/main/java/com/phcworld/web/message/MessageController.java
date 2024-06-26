package com.phcworld.web.message;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.phcworld.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phcworld.domain.exception.LoginNotUserException;
import com.phcworld.domain.exception.MatchNotUserException;
import com.phcworld.domain.exception.UserNotFoundException;
import com.phcworld.domain.message.MessageRequest;
import com.phcworld.domain.message.MessageResponse;
import com.phcworld.service.message.MessageServiceImpl;
import com.phcworld.user.service.UserServiceImpl;
import com.phcworld.utils.HttpSessionUtils;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
	
	private static final Logger log = LoggerFactory.getLogger(MessageController.class);
	
	private final UserServiceImpl userService;

	private final MessageServiceImpl messageService;
	
	@PostMapping("")
	public MessageResponse sendMessage(MessageRequest request, HttpSession session) {
		log.debug("toUser : {}", request.getToUserEmail()); 
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		UserEntity receiveUser = userService.findUserByEmail(request.getToUserEmail());
		if(receiveUser == null) {
			throw new UserNotFoundException("보낼 유저 정보가 없습니다.");
		}
		if(loginUser.matchId(receiveUser.getId())) {
			throw new MatchNotUserException("자신에게는 메세지를 보낼 수 없습니다.");
		}
		return messageService.createMessage(loginUser, receiveUser, request.getContents());
	}
	
	@GetMapping("/{id}")
	public String confirmMessage(@PathVariable Long id, Model model, HttpSession session) 
			throws LoginNotUserException, MatchNotUserException {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		
		MessageResponse message = messageService.confirmMessage(id, loginUser);
		
		List<MessageResponse> allMessages = messageService.findMessageAllBySenderAndNotConfirmUseProfile(loginUser, "읽지 않음");
		String countOfMessages = Integer.toString(allMessages.size());
		if(allMessages.size() == 0) {
			countOfMessages = "";
		}
		session.removeAttribute("countMessages");
		session.setAttribute("countMessages", countOfMessages);
		return "{\"className\":\"" + message.getConfirm() +"\", \"countOfMessage\" : \"" + countOfMessages +"\"}";
	}
	
	@PostMapping("/info/{id}")
	public String confirmAndGetReceiverInfo(@PathVariable Long id, Model model, HttpSession session) 
			throws LoginNotUserException, MatchNotUserException {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		
		MessageResponse message = messageService.confirmMessage(id, loginUser);

		List<MessageResponse> allMessages = messageService.findMessageAllBySenderAndNotConfirmUseProfile(loginUser, "읽지 않음");
		String countOfMessages = Integer.toString(allMessages.size());
		if(allMessages.size() == 0) {
			countOfMessages = "";
		}
		session.removeAttribute("countMessages");
		session.setAttribute("countMessages", countOfMessages);
		return "{\"fromUser\" : \""+ message.getSender().getEmail() +"\", \"className\":\"" + message.getConfirm() +"\", \"countOfMessage\" : \"" + countOfMessages +"\"}";
	}
}
