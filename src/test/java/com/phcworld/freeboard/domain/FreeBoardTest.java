package com.phcworld.freeboard.domain;

import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.domain.dto.FreeBoardUpdateRequest;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardSelectDto;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.UserStatus;
import com.phcworld.user.infrastructure.UserEntity;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

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
        FreeBoard addedCountFreeBoard = freeBoard.addCount(null);

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

    @Test
    public void FreeBoardUpdate_로_게시글을_수정할_수_있다(){
        // given
        LocalDateTime now = LocalDateTime.now();
        FreeBoardUpdateRequest request = FreeBoardUpdateRequest.builder()
                .id(1L)
                .contents("내용 수정")
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
        freeBoard = freeBoard.update(request, new FakeLocalDateTimeHolder(now));

        // then
        assertThat(freeBoard.getId()).isEqualTo(1);
        assertThat(freeBoard.getTitle()).isEqualTo("제목");
        assertThat(freeBoard.getContents()).isEqualTo("내용 수정");
        assertThat(freeBoard.getWriter()).isEqualTo(user);
        assertThat(freeBoard.getCreateDate()).isEqualTo(now);
        assertThat(freeBoard.getUpdateDate()).isEqualTo(now);
    }

    @Test
    public void 삭제시_delete_값이_true를_반환한다(){
        // given
        LocalDateTime now = LocalDateTime.now();
        FakeLocalDateTimeHolder localDateTimeHolder = new FakeLocalDateTimeHolder(now);
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
        freeBoard = freeBoard.delete(localDateTimeHolder);

        // then
        assertThat(freeBoard.isDeleted()).isTrue();
    }

    @Test
    public void FreeBoardSelectDto로_게시글을_만들_수_있다(){
        // given
        LocalDateTime now = LocalDateTime.now();
        FakeLocalDateTimeHolder localDateTimeHolder = new FakeLocalDateTimeHolder(now);
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
        FreeBoardSelectDto dto = FreeBoardSelectDto.builder()
                .id(1L)
                .title("제목")
                .contents("내용")
                .writer(UserEntity.from(user))
                .count(0)
                .createDate(localDateTimeHolder.now())
                .updateDate(localDateTimeHolder.now())
                .isDeleted(false)
                .countOfAnswer(0L)
                .build();

        // when
        FreeBoard freeBoard = FreeBoard.from(dto);

        // then
        assertThat(freeBoard.getId()).isEqualTo(1);
        assertThat(freeBoard.getTitle()).isEqualTo("제목");
        assertThat(freeBoard.getContents()).isEqualTo("내용");
        assertThat(freeBoard.getCount()).isZero();
        assertThat(freeBoard.getWriter()).isEqualTo(user);
        assertThat(freeBoard.getCreateDate()).isEqualTo(now);
        assertThat(freeBoard.getUpdateDate()).isEqualTo(now);
        assertThat(freeBoard.isDeleted()).isFalse();
        assertThat(freeBoard.getCountOfAnswer()).isEmpty();
    }

}