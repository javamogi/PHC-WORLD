package com.phcworld.web.login;

import com.phcworld.domain.user.LoginRequestUser;
import com.phcworld.jwt.dto.TokenDto;
import com.phcworld.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginApiController {

    private final UserService userService;

    @PostMapping("/login")
    public TokenDto loginByToken(@RequestBody LoginRequestUser requestUser) {
        return userService.tokenLogin(requestUser);
    }
}
