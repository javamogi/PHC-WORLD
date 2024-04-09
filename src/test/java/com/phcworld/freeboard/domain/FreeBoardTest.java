package com.phcworld.freeboard.domain;

import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.mock.FakePasswordEncode;
import com.phcworld.mock.FakeUuidHolder;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.UserStatus;
import com.phcworld.user.domain.dto.UserRequest;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class FreeBoardTest {

    @Test
    public void FreeBoardRequest_로_게시글을_만들_수_있다(){
        // given
        LocalDateTime now = LocalDateTime.now();
        FreeBoardRequest request = FreeBoardRequest.builder()
                .title("제목")
                .contents("내용")
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(now)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();

        // when
        FreeBoard freeBoard = FreeBoard.from(request, user, new FakeLocalDateTimeHolder(now));

        // then
        assertThat(freeBoard.getId()).isNull();
        assertThat(freeBoard.getTitle()).isEqualTo("제목");
        assertThat(freeBoard.getContents()).isEqualTo("내용");
        assertThat(freeBoard.getWriter()).isEqualTo(user);
        assertThat(freeBoard.getCreateDate()).isEqualTo(now);
        assertThat(freeBoard.getUpdateDate()).isEqualTo(now);
    }

}