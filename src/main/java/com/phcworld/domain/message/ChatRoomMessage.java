package com.phcworld.domain.message;

import com.phcworld.domain.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class ChatRoomMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChatRoom chatRoom;

    @ManyToOne
    private User writer;

    @Lob
    private String message;

    @CreatedDate
    private LocalDateTime sendDate;

    @Builder
    public ChatRoomMessage(Long id, ChatRoom chatRoom, User writer, String message, LocalDateTime sendDate) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.writer = writer;
        this.message = message;
        this.sendDate = sendDate;
    }
}

