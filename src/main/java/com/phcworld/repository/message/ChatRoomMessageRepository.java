package com.phcworld.repository.message;

import com.phcworld.domain.message.ChatRoom;
import com.phcworld.domain.message.ChatRoomMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomMessageRepository extends JpaRepository<ChatRoomMessage, Long> {

    List<ChatRoomMessage> findByChatRoomOrderBySendDateDesc(ChatRoom chatRoom, Pageable pageable);
}
