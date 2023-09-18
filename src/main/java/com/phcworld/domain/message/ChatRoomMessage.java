package com.phcworld.domain.message;

import com.phcworld.domain.user.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
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

    private Boolean isRead;

    @Builder
    public ChatRoomMessage(Long id, ChatRoom chatRoom, User writer, String message, LocalDateTime sendDate, Boolean isRead) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.writer = writer;
        this.message = message;
        this.sendDate = sendDate;
        this.isRead = isRead;
    }

    public void deleteMessage() {
        this.message = "deleted";
    }

    public boolean isSameWriter(User loginUser) {
        return this.getWriter().getId().equals(loginUser.getId());
    }
}

