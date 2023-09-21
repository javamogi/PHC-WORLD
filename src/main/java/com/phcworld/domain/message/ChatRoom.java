package com.phcworld.domain.message;

import com.phcworld.domain.user.User;
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

    public boolean containsUser(User loginUser) {
        for (ChatRoomUser chatRoomUser : users){
            if(chatRoomUser.getUser().equals(loginUser))
                return true;
        }
        return false;
    }
}
