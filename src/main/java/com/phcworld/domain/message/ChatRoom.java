package com.phcworld.domain.message;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomUser> users;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomMessage> messages;

    @Builder
    public ChatRoom(Long id, List<ChatRoomUser> users, List<ChatRoomMessage> messages) {
        this.id = id;
        this.users = users;
        this.messages = messages;
    }
}
