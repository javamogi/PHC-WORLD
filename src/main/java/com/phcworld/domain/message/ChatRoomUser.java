package com.phcworld.domain.message;

import com.phcworld.user.infrastructure.UserEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@ToString
@Setter
public class ChatRoomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private ChatRoom chatRoom;

    @Builder
    public ChatRoomUser(Long id, UserEntity user, ChatRoom chatRoom) {
        this.id = id;
        this.user = user;
        this.chatRoom = chatRoom;
    }
}
