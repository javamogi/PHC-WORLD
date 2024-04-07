package com.phcworld.user.controller.port;

import com.phcworld.user.domain.User;
import com.phcworld.user.domain.dto.LoginRequestUser;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.domain.dto.UserUpdateRequest;

public interface UserService {
    User registerUser(UserRequest user);
    User verifyCertificationCode(String email, String authKey);
    User login(LoginRequestUser requestUser);
    User update(UserUpdateRequest request);
}
