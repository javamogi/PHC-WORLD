package com.phcworld.user.infrastructure;

import com.phcworld.exception.model.EmailSendErrorException;
import com.phcworld.exception.model.InternalServerErrorException;
import com.phcworld.user.service.port.EmailAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailAuthServiceImpl implements EmailAuthService {

	private final JavaMailSender emailSender;
	
	private final EmailAuthRepository emailAuthRepository;

	@Override
	@Transactional
	public void sendEmail(String email) {
		String authKey = UUID.randomUUID().toString().replaceAll("-", "");
		EmailAuth emailAuth = EmailAuth.builder()
				.email(email)
				.authKey(authKey)
				.authenticate(false)
				.build();
		emailAuthRepository.save(emailAuth);
		
		MimeMessage msg = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg);
		try {
			helper.setTo(email);
			helper.setSubject("PHC WORLD 이메일 인증");
			helper.setText(new StringBuffer().append("<h1>메일인증</h1>").append("<a href='http://www.phcworld.com/email/emailConfirm?email=").append(email).append("&authKey=").append(authKey).append("' target='_blenk'>이메일 인증 확인</a>").toString(), true);
		} catch (MessagingException e) {
			throw new EmailSendErrorException();
		}
		emailSender.send(msg);
	}

	@Override
	public EmailAuth findByEmail(String email) {
		return emailAuthRepository.findByEmail(email);
	}
	
	
}
