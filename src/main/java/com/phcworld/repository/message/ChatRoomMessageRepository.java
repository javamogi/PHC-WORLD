package com.phcworld.repository.message;

import com.phcworld.domain.message.ChatRoomMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMessageRepository extends JpaRepository<ChatRoomMessage, Long> {
}
