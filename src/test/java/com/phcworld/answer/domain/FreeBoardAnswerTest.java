package com.phcworld.answer.domain;

import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.answer.domain.dto.FreeBoardAnswerUpdateRequest;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardUpdateRequest;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.UserStatus;
import com.phcworld.user.infrastructure.UserEntity;
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

    @Test
    public void 수정_날짜형식은_문자열로_받는다(){
        // given
        LocalDateTime now = LocalDateTime.now();
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
        FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
                .id(1L)
                .writer(user)
                .freeBoard(freeBoard)
                .contents("답변 내용")
                .createDate(now)
                .updateDate(now)
                .build();

        // when
        String formattedDate = freeBoardAnswer.getFormattedUpdateDate();

        // then
        assertThat(formattedDate).isEqualTo("방금전");
    }

    @Test
    public void 글쓴이가_다르면_false를_반환한다(){
        // given
        LocalDateTime now = LocalDateTime.now();
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

        FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
                .id(1L)
                .writer(user)
                .freeBoard(freeBoard)
                .contents("답변 내용")
                .createDate(now)
                .updateDate(now)
                .build();

        User userB = User.builder()
                .id(2L)
                .email("test2@test.test")
                .password("test2")
                .name("테스트2")
                .profileImage("blank-profile-picture.png")
                .createDate(now)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();

        // when
        boolean matchedWriter = freeBoardAnswer.matchWriter(UserEntity.from(userB));

        // then
        assertThat(matchedWriter).isFalse();
    }

    @Test
    public void FreeBoardAnswerUpdateRequest_로_게시글을_수정할_수_있다(){
        // given
        LocalDateTime now = LocalDateTime.now();
        FreeBoardAnswerUpdateRequest request = FreeBoardAnswerUpdateRequest.builder()
                .id(1L)
                .contents("답변 수정")
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

        FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
                .id(1L)
                .writer(user)
                .freeBoard(freeBoard)
                .contents("답변 내용")
                .createDate(now)
                .updateDate(now)
                .build();

        // when
        freeBoardAnswer = freeBoardAnswer.update(request, new FakeLocalDateTimeHolder(now));

        // then
        assertThat(freeBoardAnswer.getId()).isEqualTo(1);
        assertThat(freeBoardAnswer.getContents()).isEqualTo("답변 수정");
        assertThat(freeBoardAnswer.getWriter()).isEqualTo(user);
        assertThat(freeBoardAnswer.getCreateDate()).isEqualTo(now);
        assertThat(freeBoardAnswer.getUpdateDate()).isEqualTo(now);
    }

}