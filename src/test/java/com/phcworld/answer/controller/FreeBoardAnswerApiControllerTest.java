package com.phcworld.answer.controller;

import com.phcworld.answer.controller.port.FreeBoardAnswerResponse;
import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.exception.model.EmptyContentsException;
import com.phcworld.exception.model.EmptyLoginUserException;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.mock.FakeHttpSession;
import com.phcworld.mock.TestContainer;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.utils.HttpSessionUtils;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class FreeBoardAnswerApiControllerTest {

    @Test
    public void 회원은_게시글의_답변을_등록할_수_있다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(() -> time)
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test2")
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build();
        testContainer.userRepository.save(user);
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .build();
        testContainer.freeBoardRepository.save(freeBoard);
        FreeBoardAnswerRequest requestDto = FreeBoardAnswerRequest.builder()
                .contents("답변 내용")
                .build();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));
        long freeBoardId = 1;

        // when
        ResponseEntity<FreeBoardAnswerResponse> result = testContainer
                .freeBoardAnswerApiController
                .create(freeBoardId, requestDto, fakeHttpSession);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContents()).isEqualTo("답변 내용");
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("test@test.test");
    }

    @Test(expected = EmptyContentsException.class)
    public void 답변의_내용이_빈_글자라면_예외를_던진다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(() -> time)
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test2")
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build();
        testContainer.userRepository.save(user);
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .build();
        testContainer.freeBoardRepository.save(freeBoard);
        FreeBoardAnswerRequest requestDto = FreeBoardAnswerRequest.builder()
                .contents("")
                .build();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));
        long freeBoardId = 1;

        // when
        // then
        testContainer
                .freeBoardAnswerApiController
                .create(freeBoardId, requestDto, fakeHttpSession);
    }

    @Test(expected = EmptyLoginUserException.class)
    public void 로그인_회원이_없다면_답변_등록_요청시_예외를_던진다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(() -> time)
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test2")
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build();
        testContainer.userRepository.save(user);
        FreeBoard freeBoard = FreeBoard.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .count(0)
                .writer(user)
                .createDate(time)
                .updateDate(time)
                .build();
        testContainer.freeBoardRepository.save(freeBoard);
        FreeBoardAnswerRequest requestDto = FreeBoardAnswerRequest.builder()
                .contents("답변 내용")
                .build();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        long freeBoardId = 1;

        // when
        // then
        testContainer
                .freeBoardAnswerApiController
                .create(freeBoardId, requestDto, fakeHttpSession);
    }

}