package com.phcworld.repository.message;

import com.phcworld.domain.message.ChatRoom;
import com.phcworld.domain.message.dto.ChatRoomSelectDto;
import com.phcworld.domain.user.User;

import java.util.List;

public interface ChatRoomRepositoryCustom {

    List<ChatRoomSelectDto> findChatRoomListByUser(User user);
    ChatRoom findByChatRoomByUsers(List<Long> ids);
}
