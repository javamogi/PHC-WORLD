package com.phcworld.web.login;

import com.phcworld.user.domain.dto.LoginRequestUser;
import com.phcworld.jwt.dto.TokenDto;
import com.phcworld.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginApiController {

    private final UserServiceImpl userService;

    @PostMapping("/login")
    public TokenDto loginByToken(@RequestBody LoginRequestUser requestUser) {
        return userService.tokenLogin(requestUser);
    }
}
