package com.phcworld.user.controller;

import com.phcworld.user.controller.port.SessionUser;
import com.phcworld.user.controller.port.UserService;
import com.phcworld.domain.message.Message;
import com.phcworld.domain.message.MessageResponse;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.dto.LoginRequestUser;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.domain.dto.UserUpdateRequest;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.security.utils.SecurityUtil;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.user.service.CertificateService;
import com.phcworld.service.message.MessageServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;
import com.phcworld.user.service.UserServiceImpl;
import com.phcworld.utils.HttpSessionUtils;
import com.phcworld.utils.PageNationsUtil;
import lombok.Builder;
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
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@Builder
public class UserController {

	private final UserServiceImpl userServiceImpl;
	
	private final TimelineServiceImpl timelineService;
	
	private final MessageServiceImpl messageService;
	
	private final AlertServiceImpl alertService;
	
	private final CertificateService emailService;

	private final PasswordEncoder passwordEncoder;

	private final UserService userService;

	@PostMapping("")
	public String register(@Valid UserRequest requestUser) {
		userService.registerUser(requestUser);
		return "redirect:/users/loginForm";
	}

	@RequestMapping("/verify")
	public String verifyCertificationCode(@RequestParam String email, @RequestParam String authKey, Model model) {
		userService.verifyCertificationCode(email, authKey);
		model.addAttribute("authMessage", "인증되었습니다.");
		return "user/login";
	}
	
	@GetMapping("/form")
	public String form(HttpSession session) {
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
//		SessionUser loginUser = HttpSessionUtils.getUserFromSession(session);
		if(Objects.nonNull(loginUser)) {
			return "redirect:/dashboard";
		}
		return "user/form";
	}
	
	@GetMapping("/loginForm")
	public String loginForm(HttpSession session) {
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
//		SessionUser loginUser = HttpSessionUtils.getUserFromSession(session);
		if(Objects.nonNull(loginUser)) {
			return "redirect:/dashboard";
		}
		return "user/login";
	}
	
	@PostMapping("/login")
	public String login(LoginRequestUser requestUser, HttpSession session) {
		User user = userService.login(requestUser);
		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));
//		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, SessionUser.from(user));
		SecurityUtil.setSecurityContext(UserEntity.from(user));
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
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
//		SessionUser loginUser = HttpSessionUtils.getUserFromSession(session);
		if(!loginUser.matchId(id)) {
			model.addAttribute("errorMessage", "본인의 정보만 수정 가능합니다.");
			return "user/login";
		}
		model.addAttribute("user", loginUser);
		return "user/updateForm";
	}
	
	@PutMapping("")
	public String update(@Valid UserUpdateRequest request, Model model, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			model.addAttribute("errorMessage", "로그인이 필요합니다.");
			return "user/login";
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
//		SessionUser loginUser = HttpSessionUtils.getUserFromSession(session);
		if(!loginUser.matchId(request.getId())) {
			model.addAttribute("errorMessage", "본인의 정보만 수정 가능합니다.");
			return "user/login";
		}
		userService.update(request);
		return "redirect:/dashboard";
	}
	
	@GetMapping("/{id}/profile")
	public String profile(@PathVariable Long id, @RequestParam(defaultValue = "1") Integer sendPageNum, 
			@RequestParam(defaultValue = "1") Integer receivePageNum, HttpSession session, Model model) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			model.addAttribute("errorMessage", "로그인이 필요합니다.");
			return "user/login";
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
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
