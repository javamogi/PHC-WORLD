package com.phcworld.user.controller;

import com.phcworld.exception.model.BadRequestException;
import com.phcworld.exception.model.DuplicationException;
import com.phcworld.exception.model.LoginUserNotFoundException;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.mock.FakeHttpSession;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.mock.FakeModel;
import com.phcworld.mock.TestContainer;
import com.phcworld.user.controller.port.SessionUser;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.UserStatus;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.utils.HttpSessionUtils;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class UserControllerTest {

    @Test
    public void 회원가입에_성공하면_리다이렉트_주소를_받는다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 29, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        UserRequest requestDto = UserRequest.builder()
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .build();

        // when
        String result = testContainer.userController.register(requestDto);

        // then
        assertThat(result).isEqualTo("redirect:/users/loginForm");
    }

    @Test(expected = DuplicationException.class)
    public void 중복된_이메일은_예외를_던진다(){
        // given
        LocalDateTime time = LocalDateTime.of(2024, 3, 29, 12, 0);
        TestContainer testContainer = TestContainer.builder()
                .localDateTimeHolder(new FakeLocalDateTimeHolder(time))
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(LocalDateTime.now())
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build());
        UserRequest requestDto = UserRequest.builder()
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .build();

        // when
        // then
        testContainer.userController.register(requestDto);
    }

    @Test
    public void 이메일_인증이_성공하면_login_view_page를_반환한다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(LocalDateTime.now())
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build());
        String email = "test@test.test";
        String certificationCode = "1a2b3c";

        // when
        String result = testContainer.userController.verifyCertificationCode(email,
                certificationCode,
                new FakeModel());

        // then
        assertThat(result).isEqualTo("user/login");
    }

    @Test(expected = NotFoundException.class)
    public void 가입된_이메일이_아닌경우_예외를_던진다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        String email = "test@test.test";
        String certificationCode = "1a2b3c";

        // when
        // then
        testContainer.userController.verifyCertificationCode(email,
                certificationCode,
                new FakeModel());
    }

    @Test(expected = BadRequestException.class)
    public void 인증코드가_다를_경우_예외를_던진다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(LocalDateTime.now())
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build());
        String email = "test@test.test";
        String certificationCode = "12345";

        // when
        // then
        testContainer.userController.verifyCertificationCode(email,
                certificationCode,
                new FakeModel());
    }

    @Test
    public void session에_저장된_정보가_있으면_회원가입_화면은_dashboard_리다이렉트_주소를_반환한다(){
        // given
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
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

        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, SessionUser.from(user));

        // when
        String result = testContainer.userController.form(fakeHttpSession);

        // then
        assertThat(result).isEqualTo("redirect:/dashboard");
    }

    @Test
    public void session에_저장된_정보가_없으며_회원가입_화면_view_page를_반환한다(){
        // given
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        TestContainer testContainer = TestContainer.builder()
                .build();

        // when
        String result = testContainer.userController.form(fakeHttpSession);

        // then
        assertThat(result).isEqualTo("user/form");
    }

    @Test
    public void session에_저장된_정보가_있으면_로그인_화면은_dashboard_리다이렉트_주소를_반환한다(){
        // given
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
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

        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, SessionUser.from(user));

        // when
        String result = testContainer.userController.loginForm(fakeHttpSession);

        // then
        assertThat(result).isEqualTo("redirect:/dashboard");
    }

    @Test
    public void session에_저장된_정보가_없으며_로그인_화면_view_page를_반환한다(){
        // given
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
        TestContainer testContainer = TestContainer.builder()
                .build();

        // when
        String result = testContainer.userController.loginForm(fakeHttpSession);

        // then
        assertThat(result).isEqualTo("user/login");
    }

    @Test
    public void 로그아웃_요청은_session에_저장된_정보를_삭제하고_회원가입_화면_view_page를_반환한다(){
        // given
        FakeHttpSession fakeHttpSession = new FakeHttpSession();
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

        fakeHttpSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, SessionUser.from(user));

        // when
        String result = testContainer.userController.logout(fakeHttpSession);

        // then
        assertThat(fakeHttpSession.getAttribute(HttpSessionUtils.USER_SESSION_KEY)).isNull();
        assertThat(result).isEqualTo("redirect:/users/loginForm");
    }

}