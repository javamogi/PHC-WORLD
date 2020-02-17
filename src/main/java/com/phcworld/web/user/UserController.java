package com.phcworld.web.user;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.alert.AlertServiceImpl;
import com.phcworld.domain.email.EmailAuth;
import com.phcworld.domain.email.EmailService;
import com.phcworld.domain.message.Message;
import com.phcworld.domain.message.MessageServiceImpl;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.timeline.TimelineServiceImpl;
import com.phcworld.domain.user.User;
import com.phcworld.service.user.UserService;
import com.phcworld.web.HttpSessionUtils;
import com.phcworld.web.PageNationsUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/users")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TimelineServiceImpl timelineService;
	
	@Autowired
	private MessageServiceImpl messageService;
	
	@Autowired
	private AlertServiceImpl alertService;
	
	@Autowired
	private EmailService emailService;
	
	@PostMapping("")
	public String create(@Valid User user, BindingResult bindingResult, Model model) {
		log.debug("User : {}", user);
		if(bindingResult.hasErrors()) {
			log.debug("Binding Result has Error!");
			List<ObjectError> errors = bindingResult.getAllErrors();
			for (ObjectError error : errors) {
				log.debug("error : {},{}", error.getCode(), error.getDefaultMessage());
				model.addAttribute("errorMessage", error.getDefaultMessage());
			}
			return "/user/form";
		}
		User emailUser = userService.findUserByEmail(user.getEmail());
		if(existUser(emailUser)) {
			model.addAttribute("errorMessage", "이미 등록된 이메일입니다.");
			return "/user/form";
		}
		userService.createUser(user);
		
		try {
			emailService.sendEmail(user.getEmail());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/users/loginForm";
	}

	private boolean existUser(User emailUser) {
		return emailUser != null;
	}
	
	@GetMapping("/loginForm")
	public String loginForm(HttpSession session) {
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if(existUser(loginUser)) {
			return "redirect:/dashboard";
		}
		return "/user/login";
	}
	
	@PostMapping("/login")
	public String login(String email, String password, Model model, HttpSession session) {
		User user = userService.findUserByEmail(email);
		log.debug("input User : {}", user);
		if(user == null) {
			model.addAttribute("errorMessage", "존재하지 않는 이메일입니다.");
			return "/user/login";
		}
		if(!user.matchPassword(password)) {
			model.addAttribute("errorMessage", "비밀번호가 틀립니다.");
			return "/user/login";
		}
		
		EmailAuth emailAuth = emailService.findByEmail(email);
		if(!emailAuth.auth()) {
			model.addAttribute("errorMessage", "이메일 인증이 안됐습니다. 메일에서 인증하세요.");
			return "/user/login";
		}
		
		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		
		List<Message> allMessages = messageService.findMessageAllByToUserAndConfirm(user, "읽지 않음");
		
		List<Message> messages = messageService.findMessageByRequestPageToUserAndConfirm(user, "읽지 않음");
		String countOfMessages = Integer.toString(allMessages.size());
		if(allMessages.size() == 0) {
			countOfMessages = "";
		}
		session.setAttribute("messages", messages);
		session.setAttribute("countMessages", countOfMessages);
		
		List<Alert> alerts = alertService.findPageRequestAlertByUser(user);
		session.setAttribute("alerts", alerts);
		
		return "redirect:/dashboard";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
		return "redirect:/users/loginForm";
	}
	
	@GetMapping("/form")
	public String form(HttpSession session) {
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if(existUser(loginUser)) {
			return "redirect:/dashboard";
		}
		return "/user/form";
	}
	
	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			model.addAttribute("errorMessage", "로그인이 필요합니다.");
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if(!loginUser.matchId(id)) {
			model.addAttribute("errorMessage", "본인의 정보만 수정 가능합니다.");
			return "/user/login";
		}
		model.addAttribute("user", loginUser);
		return "/user/updateForm";
	}
	
	@PutMapping("/{id}")
	public String update(@PathVariable Long id, @Valid User newUser, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			model.addAttribute("errorMessage", "로그인이 필요합니다.");
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if(!loginUser.matchId(id)) {
			model.addAttribute("errorMessage", "본인의 정보만 수정 가능합니다.");
			return "/user/login";
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
		userService.updateUser(loginUser, newUser);
		return "redirect:/dashboard";
	}
	
	@GetMapping("/{id}/profile")
	public String profile(@PathVariable Long id, @RequestParam(defaultValue = "1") Integer sendPageNum, @RequestParam(defaultValue = "1") Integer receivePageNum, HttpSession session, Model model) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			model.addAttribute("errorMessage", "로그인이 필요합니다.");
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		User user = userService.findUserById(id);
		
		Page<Timeline> timelines = timelineService.findPageTimelineByUser(user);
		boolean temp = false;
		if(timelines != null) {
			temp = timelines.hasNext();
		}
		model.addAttribute("show more", temp);

		if(loginUser.matchId(id)) {
			PageNationsUtil pageNation = new PageNationsUtil();
			Page<Message> pageReceiveMessages = messageService.findMessageByReceiveMessages(receivePageNum, user);
			if(pageReceiveMessages != null) {
				List<Message> receiveMessages = pageReceiveMessages.getContent();
				pageNation.viewPageNation("receive", receivePageNum, pageReceiveMessages.getTotalPages(), model);
				model.addAttribute("receiveMessages", receiveMessages);
			}

			Page<Message> pageSendMessages = messageService.findMessageBySendMessage(sendPageNum, user);
			if(pageSendMessages != null) {
				List<Message> sendMessages = pageSendMessages.getContent();
				pageNation.viewPageNation("send", sendPageNum, pageSendMessages.getTotalPages(), model);
				model.addAttribute("sendMessages", sendMessages);
			}
			model.addAttribute("equalLoginUser", true);
		}
		
		model.addAttribute("user", user);
		model.addAttribute("timeline", timelines);
		return "/user/profile";
	}
	
}
