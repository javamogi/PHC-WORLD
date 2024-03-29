package com.phcworld.user.service;

import com.phcworld.exception.model.DuplicationException;
import com.phcworld.mock.FakeEmailAuthService;
import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.mock.FakePasswordEncode;
import com.phcworld.mock.FakeUserRepository;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.dto.UserRequest;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


public class UserServiceImplTest {

    private NewUserServiceImpl userService;

    @Before
    public void init(){
        LocalDateTime localDateTime = LocalDateTime.of(2024, 3, 29, 12, 00);
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakePasswordEncode fakePasswordEncode = new FakePasswordEncode("test2");
        FakeLocalDateTimeHolder fakeLocalDateTimeHolder = new FakeLocalDateTimeHolder(localDateTime);

        this.userService = NewUserServiceImpl.builder()
                .userRepository(fakeUserRepository)
                .passwordEncoder(fakePasswordEncode)
                .emailAuthService(new FakeEmailAuthService())
                .localDateTimeHolder(fakeLocalDateTimeHolder)
                .build();
        fakeUserRepository.save(User.builder()
                        .id(1L)
                        .email("test@test.test")
                        .password("test")
                        .name("테스트")
                        .profileImage("blank-profile-picture.png")
                        .createDate(localDateTime)
                        .authority(Authority.ROLE_USER)
                .build());
    }

    @Test
    public void UserRequest로_회원가입을_할_수_있다(){
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 3, 29, 12, 00);
        UserRequest request = UserRequest.builder()
                .email("test2@test.test")
                .password("test")
                .name("테스트2")
                .build();

        // when
        User user = userService.registerUser(request);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test2@test.test");
        assertThat(user.getPassword()).isEqualTo("test2");
        assertThat(user.getName()).isEqualTo("테스트2");
        assertThat(user.getCreateDate()).isEqualTo(localDateTime);
        assertThat(user.getAuthority()).isEqualTo(Authority.ROLE_USER);
        assertThat(user.getProfileImage()).isEqualTo("blank-profile-picture.png");
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
}