package com.phcworld.service.emailAuth;

import java.util.UUID;

import javax.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.phcworld.domain.emailAuth.EmailAuth;
import com.phcworld.repository.emailAuth.EmailAuthRepository;

@Component
@RequiredArgsConstructor
public class EmailAuthService {

	private final JavaMailSender emailSender;
	
	private final EmailAuthRepository emailAuthRepository;
	
	public void sendEmail(String email) throws Exception {
		String authKey = UUID.randomUUID().toString().replaceAll("-", "");
		EmailAuth emailAuth = EmailAuth.builder()
				.email(email)
				.authKey(authKey)
				.authenticate(false)
				.build();
		emailAuthRepository.save(emailAuth);
		
		MimeMessage msg = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg);
		helper.setTo(email);
		helper.setSubject("PHC WORLD 이메일 인증");
		helper.setText(new StringBuffer().append("<h1>메일인증</h1>").append("<a href='http://www.phcworld.com/email/emailConfirm?email=").append(email).append("&authKey=").append(authKey).append("' target='_blenk'>이메일 인증 확인</a>").toString(), true);
		emailSender.send(msg);
	}

	public EmailAuth findByEmail(String email) {
		return emailAuthRepository.findByEmail(email);
	}
	
	
}
