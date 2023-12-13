package com.phcworld.api.user.web;

import com.phcworld.api.dashboard.dto.UserResponseDto;
import com.phcworld.api.user.dto.UserRequestDto;
import com.phcworld.domain.user.User;
import com.phcworld.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    private final UserService userService;

    @PostMapping("")
    public UserResponseDto create(@ModelAttribute @Valid UserRequestDto user, BindingResult bindingResult) throws NoSuchAlgorithmException {
        if(bindingResult.hasErrors()) {
            log.debug("Binding Result has Error!");
            List<ObjectError> errors = bindingResult.getAllErrors();
            for (ObjectError error : errors) {
                log.debug("error : {},{}", error.getCode(), error.getDefaultMessage());
//                model.addAttribute("errorMessage", error.getDefaultMessage());
            }
//            return "user/form";
        }

        return UserResponseDto.of(userService.registerUser(user));
    }
}
