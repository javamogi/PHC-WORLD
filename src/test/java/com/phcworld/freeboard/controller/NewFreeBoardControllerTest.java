package com.phcworld.freeboard.controller;

import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.mock.FakeHttpSession;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.mock.TestContainer;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.UserStatus;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.utils.HttpSessionUtils;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class NewFreeBoardControllerTest {

    @Test
    public void 사용자가_게시글을_등록하면_리다이렉트_주소를_받는다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 4, 9, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        FreeBoardRequest request = FreeBoardRequest.builder()
                .title("새로운 제목")
                .contents("새로운 내용")
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));

        // when
        String result = testContainer.freeBoardController.register(request, fakeHttpSession);

        // then
        assertThat(result).isEqualTo("redirect:/freeboards/1");
    }

}