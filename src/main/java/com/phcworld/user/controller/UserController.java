package com.phcworld.user.controller;

import com.phcworld.domain.alert.dto.AlertResponseDto;
import com.phcworld.user.controller.port.UserService;
import com.phcworld.user.infrastructure.EmailAuth;
import com.phcworld.domain.message.Message;
import com.phcworld.domain.message.MessageResponse;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.user.domain.dto.LoginRequestUser;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.security.utils.SecurityUtil;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.user.infrastructure.EmailAuthServiceImpl;
import com.phcworld.service.message.MessageServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;
import com.phcworld.user.service.UserServiceImpl;
import com.phcworld.utils.HttpSessionUtils;
import com.phcworld.utils.PageNationsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Controller
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

	private final UserServiceImpl userServiceImpl;
	
	private final TimelineServiceImpl timelineService;
	
	private final MessageServiceImpl messageService;
	
	private final AlertServiceImpl alertService;
	
	private final EmailAuthServiceImpl emailService;

	private final PasswordEncoder passwordEncoder;

	private final UserService userService;

	@PostMapping("")
	public String register(@Valid UserRequest requestUser, BindingResult bindingResult, Model model) {
		log.debug("User : {}", requestUser);
		if(bindingResult.hasErrors()) {
			log.debug("Binding Result has Error!");
			List<ObjectError> errors = bindingResult.getAllErrors();
			for (ObjectError error : errors) {
				log.debug("error : {},{}", error.getCode(), error.getDefaultMessage());
				model.addAttribute("errorMessage", error.getDefaultMessage());
			}
			return "user/form";
		}
		userService.registerUser(requestUser);

		return "redirect:/users/loginForm";
	}
	
	private boolean existUser(UserEntity emailUser) {
		return emailUser != null;
	}
	
	@GetMapping("/form")
	public String form(HttpSession session) {
		UserEntity loginUser = HttpSessionUtils.getUserFromSession(session);
		if(existUser(loginUser)) {
			return "redirect:/dashboard";
		}
		return "user/form";
	}
	
	@GetMapping("/loginForm")
	public String loginForm(HttpSession session) {
		UserEntity loginUser = HttpSessionUtils.getUserFromSession(session);
		if(existUser(loginUser)) {
			return "redirect:/dashboard";
		}
		return "user/login";
	}
	
	@PostMapping("/login")
	public String login(LoginRequestUser requestUser, Model model, HttpSession session) throws NoSuchAlgorithmException {
		UserEntity user = userServiceImpl.findUserByEmail(requestUser.getEmail());
		log.debug("input User : {}", user);
		if(!existUser(user)) {
			model.addAttribute("errorMessage", "존재하지 않는 이메일입니다.");
			return "user/login";
		}
//		if(!user.matchPassword(encodedPassword)) {
		if(!passwordEncoder.matches(requestUser.getPassword(), user.getPassword())) {
			model.addAttribute("errorMessage", "비밀번호가 틀립니다.");
			return "user/login";
		}
		
		EmailAuth emailAuth = emailService.findByEmail(user.getEmail());
		if(emailAuth != null) {
			if(!emailAuth.isAuthenticate()) {
				model.addAttribute("errorMessage", "이메일 인증이 안됐습니다. 메일에서 인증하세요.");
				return "user/login";
			}
		}
		
		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		
//		List<Message> allMessages = messageService.findMessageAllBySenderAndNotConfirmUseProfile(user, "읽지 않음");
		List<MessageResponse> allMessages = messageService.findMessageAllBySenderAndNotConfirmUseProfile(user, "읽지 않음");
		
		List<MessageResponse> messages = messageService.findMessageBySenderAndConfirmUseMenu(user, "읽지 않음");
//		List<Message> messages = messageService.findMessageBySenderAndConfirmUseMenu(user, "읽지 않음");
		String countOfMessages = Integer.toString(allMessages.size());
		if(allMessages.size() == 0) {
			countOfMessages = "";
		}
		session.setAttribute("messages", messages);
		session.setAttribute("countMessages", countOfMessages);
		
//		List<Alert> alerts = alertService.findListAlertByPostUser(user);
		List<AlertResponseDto> alerts = alertService.findByAlertListByPostUser(user);
		session.setAttribute("alerts", alerts);
		SecurityUtil.setSecurityContext(user);
		return "redirect:/dashboard";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
		return "redirect:/users/loginForm";
	}
	
	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			model.addAttribute("errorMessage", "로그인이 필요합니다.");
			return "user/login";
		}
		UserEntity loginUser = HttpSessionUtils.getUserFromSession(session);
		if(!loginUser.matchId(id)) {
			model.addAttribute("errorMessage", "본인의 정보만 수정 가능합니다.");
			return "user/login";
		}
		model.addAttribute("user", loginUser);
		return "user/updateForm";
	}
	
	@PutMapping("/{id}")
	public String update(@PathVariable Long id, @Valid UserEntity newUser, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			model.addAttribute("errorMessage", "로그인이 필요합니다.");
			return "user/login";
		}
		UserEntity loginUser = HttpSessionUtils.getUserFromSession(session);
		if(!loginUser.matchId(id)) {
			model.addAttribute("errorMessage", "본인의 정보만 수정 가능합니다.");
			return "user/login";
		}
		if(bindingResult.hasErrors()) {
			log.debug("Binding Result has Error!");
			List<ObjectError> errors = bindingResult.getAllErrors();
			for (ObjectError error : errors) {
				log.debug("error : {},{}", error.getCode(), error.getDefaultMessage());
				redirectAttributes.addFlashAttribute("errorMessage", error.getDefaultMessage());
			}
			return String.format("redirect:/users/%d/form", id);
		}
		userServiceImpl.updateUser(loginUser, newUser);
		return "redirect:/dashboard";
	}
	
	@GetMapping("/{id}/profile")
	public String profile(@PathVariable Long id, @RequestParam(defaultValue = "1") Integer sendPageNum, 
			@RequestParam(defaultValue = "1") Integer receivePageNum, HttpSession session, Model model) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			model.addAttribute("errorMessage", "로그인이 필요합니다.");
			return "user/login";
		}
		UserEntity loginUser = HttpSessionUtils.getUserFromSession(session);
		UserEntity user = userServiceImpl.findUserById(id);
		
		List<Timeline> timelines = timelineService.findTimelineList(0, user);
		boolean showMore = false;
		if(timelines.size() > 0) {
			showMore = true;
		}
		model.addAttribute("show more", showMore);

		if(loginUser.matchId(id)) {
			viewLoginUserMessage(sendPageNum, receivePageNum, model, user);
		}
		
		model.addAttribute("user", user);
		model.addAttribute("timeline", timelines);
		return "user/profile";
	}

	private void viewLoginUserMessage(Integer sendPageNum, Integer receivePageNum, Model model, UserEntity user) {
		Page<Message> pageReceiveMessages = messageService.findMessageByReceiverMessages(receivePageNum, user);
		if(pageReceiveMessages != null) {
			List<MessageResponse> receiveMessages = messageService.responseList(pageReceiveMessages);
			PageNationsUtil.viewPageNation("receive", receivePageNum, pageReceiveMessages.getTotalPages(), model);
			model.addAttribute("receiveMessages", receiveMessages);
		}

		Page<Message> pageSendMessages = messageService.findMessageBySenderMessage(sendPageNum, user);
		if(pageSendMessages != null) {
			List<MessageResponse> sendMessages = messageService.responseList(pageSendMessages);
			PageNationsUtil.viewPageNation("send", sendPageNum, pageSendMessages.getTotalPages(), model);
			model.addAttribute("sendMessages", sendMessages);
		}
		model.addAttribute("equalLoginUser", true);
	}

	
}
