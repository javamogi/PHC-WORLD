package com.phcworld.user.controller.port;

import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import lombok.Builder;

@Builder
public class SessionUser {
    private Long id;
    private String name;
    private String email;
    private Authority authority;

    public static SessionUser from(User user){
        return SessionUser.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .authority(user.getAuthority())
                .build();
    }

    public boolean matchId(Long id) {
        return this.id.equals(id);
    }
}
