package com.phcworld.user.service.port;

import com.phcworld.user.infrastructure.EmailAuth;

public interface EmailAuthService {
    void sendEmail(String email);
    EmailAuth findByEmail(String email);
}
