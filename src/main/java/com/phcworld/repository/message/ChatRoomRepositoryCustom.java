package com.phcworld.repository.message;

import com.phcworld.domain.message.ChatRoom;
import com.phcworld.domain.message.dto.ChatRoomSelectDto;
import com.phcworld.domain.message.dto.MessageSelectDto;
import com.phcworld.domain.user.User;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ChatRoomRepositoryCustom {

    List<ChatRoomSelectDto> findChatRoomListByUser(User user);
    ChatRoom findChatRoomByUsers(List<Long> ids);
    List<MessageSelectDto> findMessagesByChatRoom(ChatRoom chatRoom, PageRequest pageRequest);
}
