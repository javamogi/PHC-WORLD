package com.phcworld.api.dashboard.dto;

import com.phcworld.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long id;

    private String email;

    private String name;

    private String createDate;

    private String profileImage;

    public static UserResponseDto of(User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .createDate(user.getFormattedCreateDate())
                .profileImage(user.getProfileImage())
                .build();
    }
}
