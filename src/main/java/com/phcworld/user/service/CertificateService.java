package com.phcworld.user.service;

import com.phcworld.user.domain.User;
import com.phcworld.user.service.port.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificateService {

	private final MailSender mailSender;
	
	public void sendEmail(User user) {
		mailSender.sendEmail(user.getEmail(), user.getCertificationCode());
	}

}
