package com.phcworld.user.service.port;

public interface MailSender {
    void sendEmail(String email, String certificationCode);
}
