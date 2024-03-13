package com.phcworld.api.user.web;

import com.phcworld.api.dashboard.dto.UserResponseDto;
import com.phcworld.api.user.dto.UserRequestDto;
import com.phcworld.domain.user.LoginRequestUser;
import com.phcworld.jwt.dto.TokenDto;
import com.phcworld.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    private final UserService userService;

    @PostMapping("")
    public UserResponseDto create(@Valid @ModelAttribute UserRequestDto user) {
        return UserResponseDto.of(userService.registerUser(user));
    }

    @PostMapping("/login")
    public TokenDto login(@Valid @ModelAttribute LoginRequestUser user) {
        return userService.tokenLogin(user);
    }
}
