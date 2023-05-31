package com.phcworld.web.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.phcworld.domain.emailAuth.EmailAuth;
import com.phcworld.repository.emailAuth.EmailAuthRepository;

@Controller
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
	
	private EmailAuthRepository emailAuthRepository;
	
	@RequestMapping("/emailConfirm")
	public String authKeyConfirm(@RequestParam String email, @RequestParam String authKey, Model model) {
		EmailAuth emailAuth = emailAuthRepository.findByEmail(email);
		if(emailAuth == null) {
			model.addAttribute("errorMessage", "없는 이메일 주소입니다. 가입 먼저하세요!");
			return "/user/form";
		}
		if(!emailAuth.matchAuthKey(authKey)) {
			model.addAttribute("errorMessage", "인증키가 맞지 않습니다.");
			return "/user/form";
		}
		
		emailAuth.setAuthenticate(true);
		emailAuthRepository.save(emailAuth);
		model.addAttribute("authMessage", "인증되었습니다. 로그인하세요!");
		return "/user/login";
	}
}
