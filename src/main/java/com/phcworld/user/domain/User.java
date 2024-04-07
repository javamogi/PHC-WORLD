package com.phcworld.user.domain;

import com.phcworld.common.infrastructure.LocalDateTimeHolder;
import com.phcworld.common.infrastructure.UuidHolder;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.domain.dto.UserUpdateRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Builder
public class User {
    private Long id;

    private String email;

    private String password;

    private String name;

    private Authority authority;

    private LocalDateTime createDate;

    private String profileImage;

    private String certificationCode;

    private UserStatus userStatus;

    public static User from(UserRequest userRequest,
                            PasswordEncoder passwordEncoder,
                            LocalDateTimeHolder timeHolder,
                            UuidHolder uuidHolder){
        return User.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .name(userRequest.getName())
                .authority(Authority.ROLE_USER)
                .createDate(timeHolder.now())
                .profileImage("blank-profile-picture.png")
                .certificationCode(uuidHolder.random())
                .userStatus(UserStatus.PENDING)
                .build();
    }

    public boolean matchCertificationCode(String authKey) {
        return certificationCode.equals(authKey);
    }

    public User verify() {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .authority(authority)
                .createDate(createDate)
                .profileImage(profileImage)
                .certificationCode(certificationCode)
                .userStatus(UserStatus.ACTIVE)
                .build();
    }

    public boolean matchId(Long id) {
        return this.id.equals(id);
    }

    public User update(UserUpdateRequest request, PasswordEncoder passwordEncoder) {
        return User.builder()
                .id(id)
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .authority(authority)
                .createDate(createDate)
                .profileImage(profileImage)
                .certificationCode(certificationCode)
                .userStatus(UserStatus.ACTIVE)
                .build();
    }
}
