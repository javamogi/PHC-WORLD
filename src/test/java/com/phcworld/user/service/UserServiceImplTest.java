package com.phcworld.user.service;

import com.phcworld.exception.model.*;
import com.phcworld.mock.*;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.UserStatus;
import com.phcworld.user.domain.dto.LoginRequestUser;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.domain.dto.UserUpdateRequest;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


public class UserServiceImplTest {

    private NewUserServiceImpl userService;

    @Before
    public void init(){
        LocalDateTime localDateTime = LocalDateTime.of(2024, 3, 29, 12, 0);
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakePasswordEncode fakePasswordEncode = new FakePasswordEncode("test2");
        FakeLocalDateTimeHolder fakeLocalDateTimeHolder = new FakeLocalDateTimeHolder(localDateTime);
        FakeUuidHolder fakeUuidHolder = new FakeUuidHolder("1a2b3c");

        this.userService = NewUserServiceImpl.builder()
                .userRepository(fakeUserRepository)
                .passwordEncoder(fakePasswordEncode)
                .certificateService(new CertificateService(new FakeMailSender()))
                .localDateTimeHolder(fakeLocalDateTimeHolder)
                .uuidHolder(fakeUuidHolder)
                .build();
        fakeUserRepository.save(User.builder()
                        .id(1L)
                        .email("test@test.test")
                        .password("test")
                        .name("테스트")
                        .profileImage("blank-profile-picture.png")
                        .createDate(localDateTime)
                        .authority(Authority.ROLE_USER)
                        .userStatus(UserStatus.ACTIVE)
                        .certificationCode("1a2b3c")
                .build());
        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("test2@test.test")
                .password("test2")
                .name("테스트2")
                .profileImage("blank-profile-picture.png")
                .createDate(localDateTime)
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.PENDING)
                .certificationCode("1a2b3c")
                .build());
    }

    @Test
    public void UserRequest로_회원가입을_할_수_있다(){
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 3, 29, 12, 00);
        UserRequest request = UserRequest.builder()
                .email("pakoh200@test.test")
                .password("test")
                .name("박호철")
                .build();

        // when
        User user = userService.registerUser(request);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("pakoh200@test.test");
        assertThat(user.getPassword()).isEqualTo("test2");
        assertThat(user.getName()).isEqualTo("박호철");
        assertThat(user.getCreateDate()).isEqualTo(localDateTime);
        assertThat(user.getAuthority()).isEqualTo(Authority.ROLE_USER);
        assertThat(user.getProfileImage()).isEqualTo("blank-profile-picture.png");
        assertThat(user.getCertificationCode()).isEqualTo("1a2b3c");
        assertThat(user.getUserStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test(expected = DuplicationException.class)
    public void 중복된_이메일_가입은_예외를_던진다(){
        // given
        UserRequest request = UserRequest.builder()
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .build();

        // when
        // then
        userService.registerUser(request);
    }

    @Test
    public void 이메일과_인증코드로_회원의_상태_ACTIVE로_변경할_수_있다(){
        // given
        String email = "test2@test.test";
        String certificationCode = "1a2b3c";

        // when
        User user = userService.verifyCertificationCode(email, certificationCode);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test2@test.test");
        assertThat(user.getPassword()).isEqualTo("test2");
        assertThat(user.getName()).isEqualTo("테스트2");
        assertThat(user.getAuthority()).isEqualTo(Authority.ROLE_USER);
        assertThat(user.getProfileImage()).isEqualTo("blank-profile-picture.png");
        assertThat(user.getCertificationCode()).isEqualTo("1a2b3c");
        assertThat(user.getUserStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test(expected = NotFoundException.class)
    public void 가입하지_않은_이메일은_예외를_던진다(){
        // given
        String email = "temp@test.test";
        String certificationCode = "1a2b3c";

        // when
        // then
        userService.verifyCertificationCode(email, certificationCode);
    }

    @Test(expected = BadRequestException.class)
    public void 인증코드가_같지_않으면_예외를_던진다(){
        // given
        String email = "test2@test.test";
        String certificationCode = "11111";

        // when
        // then
        userService.verifyCertificationCode(email, certificationCode);
    }

    @Test
    public void ACTIVE인_회원은_저장된_비밀번호를_확인하고_같으면_로그인할_수_있다(){
        // given
        LoginRequestUser requestUser = LoginRequestUser.builder()
                .email("test@test.test")
                .password("test")
                .build();

        // when
        User user = userService.login(requestUser);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@test.test");
        assertThat(user.getPassword()).isEqualTo("test");
        assertThat(user.getName()).isEqualTo("테스트");
        assertThat(user.getAuthority()).isEqualTo(Authority.ROLE_USER);
        assertThat(user.getProfileImage()).isEqualTo("blank-profile-picture.png");
        assertThat(user.getCertificationCode()).isEqualTo("1a2b3c");
        assertThat(user.getUserStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test(expected = LoginUserNotFoundException.class)
    public void 로그인_시_존재하지_않는_회원은_예외를_던진다(){
        // given
        LoginRequestUser requestUser = LoginRequestUser.builder()
                .email("pakoh200@test.test")
                .password("pakoh200")
                .build();

        // when
        // then
        userService.login(requestUser);
    }

    @Test(expected = PasswordNotMatchException.class)
    public void 로그인_시_ACTIVE인_회원의_비밀번호가_다르면_예외를_던진다(){
        // given
        LoginRequestUser requestUser = LoginRequestUser.builder()
                .email("test@test.test")
                .password("1234")
                .build();

        // when
        // then
        userService.login(requestUser);
    }

    @Test(expected = UnauthenticatedException.class)
    public void 로그인_시_이메일_인증하지_않은_회원은_예외를_던진다(){
        // given
        LoginRequestUser requestUser = LoginRequestUser.builder()
                .email("test2@test.test")
                .password("test2")
                .build();

        // when
        // then
        userService.login(requestUser);
    }

    @Test
    public void UserUpdateRequest_요청으로_회원정보를_수정할_수_있다(){
        // given
        UserUpdateRequest requestUser = UserUpdateRequest.builder()
                .id(1L)
                .password("test2")
                .name("이름수정")
                .build();

        // when
        User user = userService.update(requestUser);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@test.test");
        assertThat(user.getPassword()).isEqualTo("test2");
        assertThat(user.getName()).isEqualTo("이름수정");
        assertThat(user.getAuthority()).isEqualTo(Authority.ROLE_USER);
        assertThat(user.getProfileImage()).isEqualTo("blank-profile-picture.png");
        assertThat(user.getCertificationCode()).isEqualTo("1a2b3c");
        assertThat(user.getUserStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test(expected = UnauthenticatedException.class)
    public void 회원정보_수정시_상태가_PENDING인_회원은_예외를_던진다(){
        // given
        UserUpdateRequest requestUser = UserUpdateRequest.builder()
                .id(2L)
                .password("test2")
                .name("이름수정")
                .build();

        // when
        // then
        userService.update(requestUser);
    }

    @Test(expected = LoginUserNotFoundException.class)
    public void 회원정보_수정시_존재하지_않는_회원은_예외를_던진다(){
        // given
        UserUpdateRequest requestUser = UserUpdateRequest.builder()
                .id(999L)
                .password("test2")
                .name("이름수정")
                .build();

        // when
        // then
        userService.update(requestUser);
    }

}