package com.phcworld.freeboard.service;

import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.mock.*;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.UserStatus;
import com.phcworld.user.service.CertificateService;
import com.phcworld.user.service.NewUserServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

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
    }

    @Test
    public void 등록된_게시글_전체_데이터를_가져올_수_있다(){
        // given
        // when
        List<FreeBoard> list = freeBoardService.findAllList();

        // then
        assertThat(list).hasSize(1);
    }
}