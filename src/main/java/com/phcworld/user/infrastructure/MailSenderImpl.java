package com.phcworld.user.infrastructure;

import com.phcworld.exception.model.EmailSendErrorException;
import com.phcworld.user.service.port.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    public final JavaMailSender emailSender;

    @Value("${domain.url}")
    private String domain;

    @Override
    public void sendEmail(String email, String certificationCode) {
        MimeMessage msg = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg);
        try {
            helper.setTo(email);
            helper.setSubject("PHC WORLD 이메일 인증");
            helper.setText(new StringBuilder()
                    .append("<h1>메일인증</h1>")
                    .append("<a href='")
                    .append(domain)
                    .append("/users/verify?email=")
                    .append(email)
                    .append("&authKey=")
                    .append(certificationCode)
                    .append("' target='_blenk'>이메일 인증 확인</a>")
                    .toString(), true);
        } catch (MessagingException e) {
            throw new EmailSendErrorException();
        }
        emailSender.send(msg);
    }
}
