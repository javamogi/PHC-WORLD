package com.phcworld.mock;

import com.phcworld.user.service.port.MailSender;

public class FakeMailSender implements MailSender {

    public String email;
    public String certificationCode;

    @Override
    public void sendEmail(String email, String certificationCode) {
        this.email = email;
        this.certificationCode = certificationCode;
    }

}
