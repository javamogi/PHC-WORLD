package com.phcworld.answer.domain;

import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.UserStatus;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class FreeBoardAnswerTest {

    @Test
    public void FreeBoardAnswerRequest_로_답변을_만들_수_있다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        FreeBoardAnswerRequest request = FreeBoardAnswerRequest.builder()
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
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .writer(user)
                .count(0)
                .title("제목")
                .contents("내용")
                .createDate(now)
                .updateDate(now)
                .build();

        // when
        FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.from(freeBoard,
                user,
                request,
                new FakeLocalDateTimeHolder(now));

        // then
        assertThat(freeBoardAnswer.getId()).isNull();
        assertThat(freeBoardAnswer.getContents()).isEqualTo("내용");
        assertThat(freeBoardAnswer.getWriter()).isEqualTo(user);
        assertThat(freeBoardAnswer.getCreateDate()).isEqualTo(now);
        assertThat(freeBoardAnswer.getUpdateDate()).isEqualTo(now);
    }

}