package com.phcworld.domain.message.dto;

import com.phcworld.domain.user.User;
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
    private List<User> readUsers;

    public void removeReadUser(User readUser) {
        this.readUsers.remove(readUser);
    }
}
