package com.phcworld.answer.service;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.exception.model.AnswerNotFoundException;
import com.phcworld.exception.model.FreeBoardNotFoundException;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.mock.FakeFreeBoardAnswerRepository;
import com.phcworld.mock.FakeFreeBoardRepository;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.UserStatus;
import com.phcworld.user.infrastructure.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class FreeBoardAnswerServiceTest {

    private FreeBoardAnswerServiceImpl freeBoardAnswerService;

    @Before
    public void init(){
        LocalDateTime localDateTime = LocalDateTime.of(2024, 4, 9, 12, 0);
        FakeLocalDateTimeHolder fakeLocalDateTimeHolder = new FakeLocalDateTimeHolder(localDateTime);
        FakeFreeBoardRepository freeBoardRepository = new FakeFreeBoardRepository();
        FakeFreeBoardAnswerRepository fakeFreeBoardAnswerRepository = new FakeFreeBoardAnswerRepository();

        this.freeBoardAnswerService = FreeBoardAnswerServiceImpl.builder()
                .localDateTimeHolder(fakeLocalDateTimeHolder)
                .freeBoardRepository(freeBoardRepository)
                .freeBoardAnswerRepository(fakeFreeBoardAnswerRepository)
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(localDateTime)
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
                .createDate(localDateTime)
                .updateDate(localDateTime)
                .build();
        freeBoardRepository.save(freeBoard);
        FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
                .id(1L)
                .freeBoard(freeBoard)
                .writer(user)
                .contents("답변 내용")
                .createDate(localDateTime)
                .updateDate(localDateTime)
                .build();
        fakeFreeBoardAnswerRepository.save(freeBoardAnswer);
    }

    @Test
    public void FreeBoardAnswerRequest로_답변을_등록_할_수_있다(){
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 4, 9, 12, 0);
        FreeBoardAnswerRequest request = FreeBoardAnswerRequest.builder()
                .contents("답변 등록")
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(localDateTime)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        long freeBoardId = 1;

        // when
        FreeBoardAnswer answer = freeBoardAnswerService.register(freeBoardId, user, request);

        // then
        assertThat(answer).isNotNull();
        assertThat(answer.getId()).isNotNull();
        assertThat(answer.getContents()).isEqualTo("답변 등록");
        assertThat(answer.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(answer.getCreateDate()).isEqualTo(localDateTime);
        assertThat(answer.getUpdateDate()).isEqualTo(localDateTime);
    }

    @Test(expected = FreeBoardNotFoundException.class)
    public void 존재하지_않는_게시글에_답변을_등록하려면_예외를_던진다(){
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 4, 9, 12, 0);
        FreeBoardAnswerRequest request = FreeBoardAnswerRequest.builder()
                .contents("답변 등록")
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(localDateTime)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        long freeBoardId = 99;

        // when
        // then
        FreeBoardAnswer answer = freeBoardAnswerService.register(freeBoardId, user, request);
    }

    @Test
    public void 답변의_id로_답변_하나를_조회할_수_있다(){
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 4, 9, 12, 0);
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(localDateTime)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        long answerId = 1;

        // when
        FreeBoardAnswer answer = freeBoardAnswerService.getById(answerId, UserEntity.from(user));

        // then
        assertThat(answer).isNotNull();
        assertThat(answer.getId()).isNotNull();
        assertThat(answer.getContents()).isEqualTo("답변 내용");
        assertThat(answer.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(answer.getCreateDate()).isEqualTo(localDateTime);
        assertThat(answer.getUpdateDate()).isEqualTo(localDateTime);
    }

    @Test(expected = NotMatchUserException.class)
    public void 답변_조회시_글쓴이와_다른_회원은_예외를_던진다(){
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 4, 9, 12, 0);
        User user = User.builder()
                .id(2L)
                .email("test2@test.test")
                .password("test2")
                .name("테스트2")
                .profileImage("blank-profile-picture.png")
                .createDate(localDateTime)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        long answerId = 1;

        // when

        // then
        FreeBoardAnswer answer = freeBoardAnswerService.getById(answerId, UserEntity.from(user));
    }

    @Test(expected = AnswerNotFoundException.class)
    public void 존재하지_않는_답변_요청시_예외를_던진다(){
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 4, 9, 12, 0);
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(localDateTime)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        long answerId = 99;

        // when

        // then
        FreeBoardAnswer answer = freeBoardAnswerService.getById(answerId, UserEntity.from(user));
    }

}