package com.phcworld.domain.message;

import com.phcworld.domain.user.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = @Index(name = "idx__chat_room_id_send_date", columnList = "chat_room_id, sendDate"))
@DynamicUpdate
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

    @OneToMany(mappedBy = "message")
    private List<MessageReadUser> readUsers;

    @Builder
    public ChatRoomMessage(Long id, ChatRoom chatRoom, User writer, String message, LocalDateTime sendDate, List<MessageReadUser> readUsers) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.writer = writer;
        this.message = message;
        this.sendDate = sendDate;
        this.readUsers = readUsers;
    }

    public void deleteMessage() {
        this.message = "deleted";
        this.readUsers = null;
    }

    public boolean isSameWriter(User loginUser) {
        return this.getWriter().getId().equals(loginUser.getId());
    }

    public String getWriterName() {
        return writer.getName();
    }

    public String getWriterProfileImage() {
        return writer.getProfileImage();
    }

    public void removeReadUser(MessageReadUser readUser) {
        this.readUsers.remove(readUser);
    }
}

