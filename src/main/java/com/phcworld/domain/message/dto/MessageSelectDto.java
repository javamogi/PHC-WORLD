package com.phcworld.domain.message.dto;

import com.phcworld.user.infrastructure.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MessageSelectDto {
    private Long messageId;
    private String writerName;
    private String profileImgUrl;
    private String message;
    private LocalDateTime sendDate;
    private List<UserEntity> readUsers;

    public void removeReadUser(UserEntity readUser) {
        this.readUsers.remove(readUser);
    }
}
