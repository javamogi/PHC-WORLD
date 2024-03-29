package com.phcworld.domain.message;

import com.phcworld.user.infrastructure.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MessageReadUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private ChatRoomMessage message;

    public boolean readUser(UserEntity loginUser) {
        return this.user.equals(loginUser);
    }
}
