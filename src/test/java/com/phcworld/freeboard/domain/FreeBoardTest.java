package com.phcworld.freeboard.domain;

import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.domain.dto.FreeBoardUpdateRequest;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.mock.FakePasswordEncode;
import com.phcworld.mock.FakeUuidHolder;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.UserStatus;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.infrastructure.UserEntity;
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

    @Test
    public void 등록_날짜형식은_문자열로_받는다(){
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

        // when
        String formattedDate = freeBoard.getFormattedCreateDate();

        // then
        assertThat(formattedDate).isEqualTo("방금전");
    }

    @Test
    public void 답변이_null이거나_비어있으면_빈_문자열을_얻는다(){
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

        // when
        String countOfAnswer = freeBoard.getCountOfAnswer();

        // then
        assertThat(countOfAnswer).isEmpty();
    }

    @Test
    public void 조회수_count_1_오른다(){
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

        // when
        FreeBoard addedCountFreeBoard = freeBoard.addCount();

        // then
        assertThat(addedCountFreeBoard.getId()).isEqualTo(1);
        assertThat(addedCountFreeBoard.getContents()).isEqualTo("내용");
        assertThat(addedCountFreeBoard.getCount()).isEqualTo(1);
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
        boolean matchedWriter = freeBoard.matchWriter(UserEntity.from(userB));

        // then
        assertThat(matchedWriter).isFalse();
    }


}