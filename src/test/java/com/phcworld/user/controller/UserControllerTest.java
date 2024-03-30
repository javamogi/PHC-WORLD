package com.phcworld.user.controller;

import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.mock.TestContainer;
import com.phcworld.user.domain.dto.UserRequest;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class UserControllerTest {

    @Test
    public void 회원가입에_성공하면_리다이렉트_주소를_받는다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 29, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        UserRequest requestDto = UserRequest.builder()
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .build();

        // when
        String result = testContainer.userController.register(requestDto);

        // then
        assertThat(result).isEqualTo("redirect:/users/loginForm");
    }

}