package com.phcworld.freeboard.controller;

import com.phcworld.exception.model.FreeBoardNotFoundException;
import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.freeboard.controller.port.FreeBoardResponse;
import com.phcworld.freeboard.controller.port.FreeBoardResponseWithAuthority;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.domain.dto.FreeBoardUpdateRequest;
import com.phcworld.mock.FakeHttpSession;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.mock.FakeModel;
import com.phcworld.mock.TestContainer;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.UserStatus;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.utils.HttpSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class FreeBoardControllerTest {

    @Test
    public void 사용자가_게시글을_등록하면_리다이렉트_주소를_받는다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 4, 9, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
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
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));

        // when
        String result = testContainer.freeBoardController.register(request, fakeHttpSession);

        // then
        assertThat(result).isEqualTo("redirect:/freeboards/1");
    }

    @Test
    public void 로그인한_회원이_게시글_등록_페이지를_요청하면_등록_view_page를_받는다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
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
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));

        // when
        String result = testContainer.freeBoardController.form(fakeHttpSession);

        // then
        assertThat(result).isEqualTo("board/freeboard/freeboard_form");
    }

    @Test
    public void 로그인하지_않았으면_로그인_view_page를_받는다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();

        // when
        String result = testContainer.freeBoardController.form(fakeHttpSession);

        // then
        assertThat(result).isEqualTo("user/login");
    }

    @Test
    public void 게시글_목록_요청시_게시글_목록과_view_page를_받는다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 4, 9, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                        .id(1L)
                        .writer(user)
                        .count(0)
                        .title("제목")
                        .contents("내용")
                        .createDate(time)
                        .updateDate(time)
                        .build());
        FakeModel fakeModel = new FakeModel();

        // when
        String result = testContainer.freeBoardController.getAllList(fakeModel);

        // then
        List<FreeBoardResponse> freeboards = (List<FreeBoardResponse>) fakeModel.getAttribute("freeboards");
        assertThat(freeboards).hasSize(1);
        assertThat(freeboards.get(0).getTitle()).isEqualTo("제목");
        assertThat(freeboards.get(0).getContents()).isEqualTo("내용");
        assertThat(result).isEqualTo("board/freeboard/freeboard");
    }

    @Test
    public void 게시글_하나를_조회하면_view_page와_수정_삭제_권한이_포함된_게시글_정보를_model_에_담는다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 4, 9, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .writer(user)
                .count(0)
                .title("제목")
                .contents("내용")
                .createDate(time)
                .updateDate(time)
                .build());
        FakeModel fakeModel = new FakeModel();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();

        // when
        String result = testContainer.freeBoardController.read(1L, fakeHttpSession, fakeModel);
        FreeBoardResponseWithAuthority freeboards =
                (FreeBoardResponseWithAuthority) fakeModel.getAttribute("freeBoard");

        // then
        assertThat(freeboards).isNotNull();
        assertThat(freeboards.getId()).isEqualTo(1);
        assertThat(freeboards.getTitle()).isEqualTo("제목");
        assertThat(freeboards.getContents()).isEqualTo("내용");
        assertThat(freeboards.getCount()).isEqualTo(1);
        assertThat(freeboards.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(freeboards.getExistLoginUser()).isFalse();
        assertThat(freeboards.getIsModifyAuthority()).isFalse();
        assertThat(freeboards.getIsDeleteAuthority()).isFalse();
        assertThat(result).isEqualTo("board/freeboard/detail_freeboard");
    }

    @Test
    public void 게시글_하나를_조회할때_글쓴이와_로그인_회원이_같으면_수정_삭제_권한은_true를_얻는다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 4, 9, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .writer(user)
                .count(0)
                .title("제목")
                .contents("내용")
                .createDate(time)
                .updateDate(time)
                .build());
        FakeModel fakeModel = new FakeModel();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));

        // when
        String result = testContainer.freeBoardController.read(1L, fakeHttpSession, fakeModel);
        FreeBoardResponseWithAuthority freeboards =
                (FreeBoardResponseWithAuthority) fakeModel.getAttribute("freeBoard");

        // then
        assertThat(freeboards).isNotNull();
        assertThat(freeboards.getId()).isEqualTo(1);
        assertThat(freeboards.getTitle()).isEqualTo("제목");
        assertThat(freeboards.getContents()).isEqualTo("내용");
        assertThat(freeboards.getCount()).isEqualTo(1);
        assertThat(freeboards.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(freeboards.getExistLoginUser()).isTrue();
        assertThat(freeboards.getIsModifyAuthority()).isTrue();
        assertThat(freeboards.getIsDeleteAuthority()).isTrue();
        assertThat(result).isEqualTo("board/freeboard/detail_freeboard");
    }

    @Test
    public void 게시글_하나를_조회할때_글쓴이와_로그인_회원이_같으면_수정_삭제_권한은_false를_얻는다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 4, 9, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .writer(user)
                .count(0)
                .title("제목")
                .contents("내용")
                .createDate(time)
                .updateDate(time)
                .build());
        FakeModel fakeModel = new FakeModel();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        User loginUser = User.builder()
                .id(2L)
                .email("test2@test.test")
                .password("test2")
                .name("테스트2")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(loginUser));

        // when
        String result = testContainer.freeBoardController.read(1L, fakeHttpSession, fakeModel);
        FreeBoardResponseWithAuthority freeboards =
                (FreeBoardResponseWithAuthority) fakeModel.getAttribute("freeBoard");

        // then
        assertThat(freeboards).isNotNull();
        assertThat(freeboards.getId()).isEqualTo(1);
        assertThat(freeboards.getTitle()).isEqualTo("제목");
        assertThat(freeboards.getContents()).isEqualTo("내용");
        assertThat(freeboards.getCount()).isEqualTo(1);
        assertThat(freeboards.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(freeboards.getExistLoginUser()).isTrue();
        assertThat(freeboards.getIsModifyAuthority()).isFalse();
        assertThat(freeboards.getIsDeleteAuthority()).isFalse();
        assertThat(result).isEqualTo("board/freeboard/detail_freeboard");
    }

    @Test
    public void 게시글_하나를_조회할때_글쓴이와_로그인_회원이_다르더라도_관리자의_삭제_권한은_true를_얻는다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 4, 9, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .writer(user)
                .count(0)
                .title("제목")
                .contents("내용")
                .createDate(time)
                .updateDate(time)
                .build());
        FakeModel fakeModel = new FakeModel();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        User loginUser = User.builder()
                .id(2L)
                .email("test2@test.test")
                .password("test2")
                .name("테스트2")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_ADMIN)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(loginUser));

        // when
        String result = testContainer.freeBoardController.read(1L, fakeHttpSession, fakeModel);
        FreeBoardResponseWithAuthority freeboards =
                (FreeBoardResponseWithAuthority) fakeModel.getAttribute("freeBoard");

        // then
        assertThat(freeboards).isNotNull();
        assertThat(freeboards.getId()).isEqualTo(1);
        assertThat(freeboards.getTitle()).isEqualTo("제목");
        assertThat(freeboards.getContents()).isEqualTo("내용");
        assertThat(freeboards.getCount()).isEqualTo(1);
        assertThat(freeboards.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(freeboards.getExistLoginUser()).isTrue();
        assertThat(freeboards.getIsModifyAuthority()).isFalse();
        assertThat(freeboards.getIsDeleteAuthority()).isTrue();
        assertThat(result).isEqualTo("board/freeboard/detail_freeboard");
    }

    @Test(expected = FreeBoardNotFoundException.class)
    public void 존재하지_않는_게시글을_조회하면_예외를_던진다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        FakeModel fakeModel = new FakeModel();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();

        // when
        // then
        String result = testContainer.freeBoardController.read(99L, fakeHttpSession, fakeModel);
    }

    @Test
    public void 게시글_글쓴이와_로그인_회원이_같으면_게시글_정보와_수정_view_page를_받는다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 4, 9, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .writer(user)
                .count(0)
                .title("제목")
                .contents("내용")
                .createDate(time)
                .updateDate(time)
                .build());
        FakeModel fakeModel = new FakeModel();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));

        // when
        String result = testContainer.freeBoardController.modifyForm(1L, fakeHttpSession, fakeModel);
        FreeBoardResponse freeboard =
                (FreeBoardResponse) fakeModel.getAttribute("freeBoard");

        // then
        assertThat(freeboard).isNotNull();
        assertThat(freeboard.getId()).isEqualTo(1);
        assertThat(freeboard.getTitle()).isEqualTo("제목");
        assertThat(freeboard.getContents()).isEqualTo("내용");
        assertThat(freeboard.getCount()).isZero();
        assertThat(freeboard.getWriter().getEmail()).isEqualTo("test@test.test");
        assertThat(result).isEqualTo("board/freeboard/freeboard_updateForm");
    }

    @Test
    public void 로그인_회원이_존재하지_않으면_로그인_view_page를_반환_받는다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        FakeModel fakeModel = new FakeModel();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();

        // when
        String result = testContainer.freeBoardController.modifyForm(1L, fakeHttpSession, fakeModel);

        // then
        assertThat(result).isEqualTo("user/login");
    }

    @Test(expected = NotMatchUserException.class)
    public void 게시글_글쓴이와_로그인_회원이_다르면_예외를_던진다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 4, 9, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .writer(user)
                .count(0)
                .title("제목")
                .contents("내용")
                .createDate(time)
                .updateDate(time)
                .build());
        FakeModel fakeModel = new FakeModel();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        User loginUser = User.builder()
                .id(2L)
                .email("test2@test.test")
                .password("test2")
                .name("테스트2")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_ADMIN)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(loginUser));

        // when
        // then
        String result = testContainer.freeBoardController.modifyForm(1L, fakeHttpSession, fakeModel);
    }

    @Test
    public void 사용자가_게시글을_수정하면_리다이렉트_주소를_받는다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 4, 9, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .writer(user)
                .count(0)
                .title("제목")
                .contents("내용")
                .createDate(time)
                .updateDate(time)
                .build());
        FreeBoardUpdateRequest request = FreeBoardUpdateRequest.builder()
                .id(1L)
                .contents("새로운 내용")
                .build();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));
        FakeModel fakeModel = new FakeModel();

        // when
        String result = testContainer.freeBoardController.modify(request, fakeHttpSession, fakeModel);

        // then
        assertThat(result).isEqualTo("redirect:/freeboards/1");
    }

    @Test
    public void 수정_요청시에_로그인_회원이_존재하지_않으면_로그인_view_page를_반환_받는다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        FakeModel fakeModel = new FakeModel();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        FreeBoardUpdateRequest request = FreeBoardUpdateRequest.builder()
                .id(1L)
                .contents("새로운 내용")
                .build();

        // when
        String result = testContainer.freeBoardController.modify(request, fakeHttpSession, fakeModel);

        // then
        assertThat(result).isEqualTo("user/login");
    }

    @Test(expected = NotMatchUserException.class)
    public void 게시글_수정_요청시_글쓴이와_로그인_회원이_다르면_예외를_던진다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 4, 9, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .writer(user)
                .count(0)
                .title("제목")
                .contents("내용")
                .createDate(time)
                .updateDate(time)
                .build());
        FreeBoardUpdateRequest request = FreeBoardUpdateRequest.builder()
                .id(1L)
                .contents("새로운 내용")
                .build();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        User userB = User.builder()
                .id(2L)
                .email("test2@test.test")
                .password("test2")
                .name("테스트2")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(userB));
        FakeModel fakeModel = new FakeModel();

        // when
        // then
        String result = testContainer.freeBoardController.modify(request, fakeHttpSession, fakeModel);
    }

    @Test
    public void 게시글의_글쓴이가_삭제하면_리다이렉트_주소를_받는다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 4, 9, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .writer(user)
                .count(0)
                .title("제목")
                .contents("내용")
                .createDate(time)
                .updateDate(time)
                .build());
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));
        long id = 1;

        // when
        String result = testContainer.freeBoardController.delete(id, fakeHttpSession);

        // then
        assertThat(result).isEqualTo("redirect:/freeboards");
    }

    @Test
    public void 관리자는_게시글을_삭제하면_리다이렉트_주소를_받는다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 4, 9, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        User user = User.builder()
                .id(2L)
                .email("test2@test.test")
                .password("test2")
                .name("테스트2")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_ADMIN)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .writer(user)
                .count(0)
                .title("제목")
                .contents("내용")
                .createDate(time)
                .updateDate(time)
                .build());
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));
        long id = 1;

        // when
        String result = testContainer.freeBoardController.delete(id, fakeHttpSession);

        // then
        assertThat(result).isEqualTo("redirect:/freeboards");
    }

    @Test(expected = NotMatchUserException.class)
    public void 게시글의_글쓴이가_다르면_예외를_던진다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 4, 9, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        User userA = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        User userB = User.builder()
                .id(2L)
                .email("test2@test.test")
                .password("test2")
                .name("테스트2")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        testContainer.freeBoardRepository.save(FreeBoard.builder()
                .id(1L)
                .writer(userA)
                .count(0)
                .title("제목")
                .contents("내용")
                .createDate(time)
                .updateDate(time)
                .build());
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(userB));
        long id = 1;

        // when
        // then
        String result = testContainer.freeBoardController.delete(id, fakeHttpSession);
    }

    @Test(expected = FreeBoardNotFoundException.class)
    public void 삭제할_게시글_없다면_예외를_던진다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 4, 9, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(time)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, UserEntity.from(user));
        long id = 1;

        // when
        // then
        String result = testContainer.freeBoardController.delete(id, fakeHttpSession);
    }

}