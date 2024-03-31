package com.phcworld.user.controller.port;

import com.phcworld.user.domain.User;
import com.phcworld.user.domain.dto.UserRequest;

public interface UserService {
    User registerUser(UserRequest user);
    User verifyCertificationCode(String email, String authKey);
}
