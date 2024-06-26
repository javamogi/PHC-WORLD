package com.phcworld.user.domain;

import com.phcworld.mock.FakeLocalDateTimeHolder;
import com.phcworld.mock.FakePasswordEncode;
import com.phcworld.mock.FakeUuidHolder;
import com.phcworld.user.domain.dto.UserRequest;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void UserRequest로_회원을_만들_수_있다(){
        // given
        UserRequest request = UserRequest.builder()
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .build();
        LocalDateTime now = LocalDateTime.now();

        // when
        User user = User.from(request,
                new FakePasswordEncode("test2"),
                new FakeLocalDateTimeHolder(now),
                new FakeUuidHolder("1a2b3c"));

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("test@test.test");
        assertThat(user.getPassword()).isEqualTo("test2");
        assertThat(user.getName()).isEqualTo("테스트");
        assertThat(user.getAuthority()).isEqualTo(Authority.ROLE_USER);
        assertThat(user.getProfileImage()).isEqualTo("blank-profile-picture.png");
        assertThat(user.getCreateDate()).isEqualTo(now);
        assertThat(user.getUserStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("1a2b3c");
    }

}