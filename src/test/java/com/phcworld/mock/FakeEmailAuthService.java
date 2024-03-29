package com.phcworld.mock;

import com.phcworld.user.infrastructure.EmailAuth;
import com.phcworld.user.service.port.EmailAuthService;

public class FakeEmailAuthService implements EmailAuthService {

    private String email;

    @Override
    public void sendEmail(String email) {
        this.email = email;
    }

    @Override
    public EmailAuth findByEmail(String email) {
        return null;
    }
}
