package com.phcworld.answer.controller;

import com.phcworld.answer.controller.port.FreeBoardAnswerResponse;
import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.answer.domain.dto.FreeBoardAnswerUpdateRequest;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.exception.model.AnswerNotFoundException;
import com.phcworld.exception.model.EmptyContentsException;
import com.phcworld.exception.model.EmptyLoginUserException;
import com.phcworld.exception.model.NotMatchUserException;
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

    @Test
    public void 회원은_답변_한건을_가져올_수_있다(){
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
        testContainer.freeBoardAnswerRepository.save(FreeBoardAnswer.builder()
                        .id(1L)
                        .freeBoard(freeBoard)
                        .writer(user)
                        .contents("답변내용")
                        .createDate(time)
                        .updateDate(time)
                .build());
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));
        long answerId = 1;

        // when
        ResponseEntity<FreeBoardAnswerResponse> result = testContainer
                .freeBoardAnswerApiController
                .read(answerId, fakeHttpSession);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContents()).isEqualTo("답변내용");
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(result.getBody().getUpdateDate()).isEqualTo("03.13");
    }

    @Test(expected = NotMatchUserException.class)
    public void 답변의_글쓴이와_로그인회원이_다르면_예외를_던진다(){
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
        testContainer.freeBoardAnswerRepository.save(FreeBoardAnswer.builder()
                .id(1L)
                .freeBoard(freeBoard)
                .writer(user)
                .contents("답변내용")
                .createDate(time)
                .updateDate(time)
                .build());
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        User userB = User.builder()
                .id(2L)
                .email("test2@test.test")
                .name("테스트2")
                .password("test2")
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .build();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(userB));
        long answerId = 1;

        // when

        // then
        ResponseEntity<FreeBoardAnswerResponse> result = testContainer
                .freeBoardAnswerApiController
                .read(answerId, fakeHttpSession);
    }

    @Test(expected = AnswerNotFoundException.class)
    public void 존재하지_않는_답변을_요청하면_예외를_던진다(){
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
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));
        long answerId = 99;

        // when

        // then
        ResponseEntity<FreeBoardAnswerResponse> result = testContainer
                .freeBoardAnswerApiController
                .read(answerId, fakeHttpSession);
    }

    @Test
    public void 답변의_글쓴이와_로그인_회원이_같으면_게시글을_수정할_수_있다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(LocalDateTime::now)
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
        testContainer.freeBoardAnswerRepository.save(FreeBoardAnswer.builder()
                .id(1L)
                .freeBoard(freeBoard)
                .writer(user)
                .contents("답변내용")
                .createDate(time)
                .updateDate(time)
                .build());
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));
        FreeBoardAnswerUpdateRequest request = FreeBoardAnswerUpdateRequest.builder()
                .id(1L)
                .contents("답변 수정")
                .build();

        // when
        ResponseEntity<FreeBoardAnswerResponse> result = testContainer
                .freeBoardAnswerApiController
                .update(request, fakeHttpSession);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContents()).isEqualTo("답변 수정");
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(result.getBody().getUpdateDate()).isEqualTo("방금전");
    }

    @Test(expected = EmptyContentsException.class)
    public void 답변을_수정할_내용이_비어있으면_예외를_던진다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        FreeBoardAnswerUpdateRequest request = FreeBoardAnswerUpdateRequest.builder()
                .id(1L)
                .contents("")
                .build();

        // when
        // then
        testContainer
                .freeBoardAnswerApiController
                .update(request, fakeHttpSession);
    }

    @Test(expected = EmptyLoginUserException.class)
    public void 답변_수정시_로그인회원이_없다면_예외를_던진다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        FreeBoardAnswerUpdateRequest request = FreeBoardAnswerUpdateRequest.builder()
                .id(1L)
                .contents("수정 내용")
                .build();

        // when
        // then
        testContainer
                .freeBoardAnswerApiController
                .update(request, fakeHttpSession);
    }

    @Test
    public void 답변의_글쓴이와_로그인_회원이_같으면_게시글을_삭제할_수_있다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 13, 11, 11, 11, 111111);
        TestContainer testContainer = TestContainer.builder()
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
        testContainer.freeBoardAnswerRepository.save(FreeBoardAnswer.builder()
                .id(1L)
                .freeBoard(freeBoard)
                .writer(user)
                .contents("답변내용")
                .createDate(time)
                .updateDate(time)
                .build());
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));
        long id = 1;

        // when
        ResponseEntity<SuccessResponse> result = testContainer
                .freeBoardAnswerApiController
                .delete(id, fakeHttpSession);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getSuccess()).isEqualTo("삭제 성공");
    }

    @Test(expected = EmptyLoginUserException.class)
    public void 답변_삭제시_로그인_회원이_없으면_예외를_던진다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        long id = 1;

        // when
        // then
        testContainer
                .freeBoardAnswerApiController
                .delete(id, fakeHttpSession);
    }

    @Test(expected = AnswerNotFoundException.class)
    public void 답변_삭제시_답변이_존재하지_않으면_예외를_던진다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .password("test2")
                .authority(Authority.ROLE_USER)
                .profileImage("blank-profile-picture.png")
                .createDate(LocalDateTime.now())
                .build();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));
        long id = 1;

        // when
        // then
        testContainer
                .freeBoardAnswerApiController
                .delete(id, fakeHttpSession);
    }
}