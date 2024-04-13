package com.phcworld.freeboard.service;

import com.phcworld.exception.model.FreeBoardNotFoundException;
import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.domain.dto.FreeBoardUpdateRequest;
import com.phcworld.mock.*;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.UserStatus;
import com.phcworld.user.infrastructure.UserEntity;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FreeBoardServiceTest {

    private NewFreeBoardServiceImpl freeBoardService;

    @Before
    public void init(){
        LocalDateTime localDateTime = LocalDateTime.of(2024, 4, 9, 12, 0);
        FakeLocalDateTimeHolder fakeLocalDateTimeHolder = new FakeLocalDateTimeHolder(localDateTime);
        FakeFreeBoardRepository freeBoardRepository = new FakeFreeBoardRepository();

        this.freeBoardService = NewFreeBoardServiceImpl.builder()
                .localDateTimeHolder(fakeLocalDateTimeHolder)
                .freeBoardRepository(freeBoardRepository)
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
        freeBoardRepository.save(FreeBoard.builder()
                        .id(1L)
                        .writer(user)
                        .count(0)
                        .title("제목")
                        .contents("내용")
                        .createDate(localDateTime)
                        .updateDate(localDateTime)
                        .build());
    }

    @Test
    public void FreeBoardRequest로_게시글을_등록_할_수_있다(){
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 4, 9, 12, 0);
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
                .createDate(localDateTime)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();

        // when
        FreeBoard freeBoard = freeBoardService.register(request, user);

        // then
        assertThat(freeBoard).isNotNull();
        assertThat(freeBoard.getId()).isNotNull();
        assertThat(freeBoard.getTitle()).isEqualTo("새로운 제목");
        assertThat(freeBoard.getContents()).isEqualTo("새로운 내용");
        assertThat(freeBoard.getWriter()).isEqualTo(user);
        assertThat(freeBoard.getCreateDate()).isEqualTo(localDateTime);
        assertThat(freeBoard.getUpdateDate()).isEqualTo(localDateTime);
        assertThat(freeBoard.getCount()).isEqualTo(0);
    }

    @Test
    public void 등록된_게시글_전체_데이터를_가져올_수_있다(){
        // given
        // when
        List<FreeBoard> list = freeBoardService.findAllList();

        // then
        assertThat(list).hasSize(1);
    }

    @Test
    public void 게시글을_조회하면_조회수가_1_오른_FreeBoard를_얻는다(){
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 4, 9, 12, 0);
        long id = 1;

        // when
        FreeBoard freeBoard = freeBoardService.addReadCount(id);

        // then
        assertThat(freeBoard).isNotNull();
        assertThat(freeBoard.getId()).isNotNull();
        assertThat(freeBoard.getTitle()).isEqualTo("제목");
        assertThat(freeBoard.getContents()).isEqualTo("내용");
        assertThat(freeBoard.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(freeBoard.getCreateDate()).isEqualTo(localDateTime);
        assertThat(freeBoard.getUpdateDate()).isEqualTo(localDateTime);
        assertThat(freeBoard.getCount()).isEqualTo(1);
    }

    @Test(expected = FreeBoardNotFoundException.class)
    public void 조회할_게시글이_없는_경우_예외를_던진다(){
        // given
        long id = 99;

        // when
        // then
        FreeBoard freeBoard = freeBoardService.addReadCount(id);
    }

    @Test
    public void 게시글_글쓴이와_로그인_회원이_같으면_조회할_수_있다(){
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 4, 9, 12, 0);
        long id = 1;
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

        // when
        FreeBoard freeBoard = freeBoardService.getFreeBoard(id, UserEntity.from(user));

        // then
        assertThat(freeBoard).isNotNull();
        assertThat(freeBoard.getId()).isNotNull();
        assertThat(freeBoard.getTitle()).isEqualTo("제목");
        assertThat(freeBoard.getContents()).isEqualTo("내용");
        assertThat(freeBoard.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(freeBoard.getCreateDate()).isEqualTo(localDateTime);
        assertThat(freeBoard.getUpdateDate()).isEqualTo(localDateTime);
        assertThat(freeBoard.getCount()).isEqualTo(0);
    }

    @Test(expected = FreeBoardNotFoundException.class)
    public void 게시글이_존재하지_않으면_예외를_던진다(){
        // given
        long id = 99;
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(LocalDateTime.now())
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();

        // when
        // then
        FreeBoard freeBoard = freeBoardService.getFreeBoard(id, UserEntity.from(user));
    }

    @Test(expected = NotMatchUserException.class)
    public void 게시글의_글쓴이와_로그인_회원이_다르면_예외를_던진다(){
        // given
        long id = 1;
        User user = User.builder()
                .id(2L)
                .email("test2@test.test")
                .password("test2")
                .name("테스트2")
                .profileImage("blank-profile-picture.png")
                .createDate(LocalDateTime.now())
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();

        // when
        // then
        FreeBoard freeBoard = freeBoardService.getFreeBoard(id, UserEntity.from(user));
    }

    @Test
    public void 게시글_글쓴이와_로그인_회원이_같으면_FreeBoardRequest로_게시글을_수정_할_수_있다(){
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 4, 9, 12, 0);
        FreeBoardUpdateRequest request = FreeBoardUpdateRequest.builder()
                .id(1L)
                .contents("새로운 내용")
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

        // when
        FreeBoard freeBoard = freeBoardService.update(request, UserEntity.from(user));

        // then
        assertThat(freeBoard).isNotNull();
        assertThat(freeBoard.getId()).isNotNull();
        assertThat(freeBoard.getTitle()).isEqualTo("제목");
        assertThat(freeBoard.getContents()).isEqualTo("새로운 내용");
        assertThat(freeBoard.getWriter()).isEqualTo(user);
        assertThat(freeBoard.getCreateDate()).isEqualTo(localDateTime);
        assertThat(freeBoard.getUpdateDate()).isEqualTo(localDateTime);
        assertThat(freeBoard.getCount()).isZero();
    }

    @Test(expected = NotMatchUserException.class)
    public void 게시글_글쓴이와_로그인_회원이_다르면_예외를_던진다(){
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 4, 9, 12, 0);
        FreeBoardUpdateRequest request = FreeBoardUpdateRequest.builder()
                .id(1L)
                .contents("새로운 내용")
                .build();
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

        // when
        // then
        FreeBoard freeBoard = freeBoardService.update(request, UserEntity.from(user));
    }

}