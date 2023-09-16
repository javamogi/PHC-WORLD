package com.phcworld.service.message;

import com.phcworld.domain.message.ChatRoom;
import com.phcworld.domain.message.ChatRoomMessage;
import com.phcworld.domain.message.ChatRoomUser;
import com.phcworld.domain.message.dto.ChatRoomSelectDto;
import com.phcworld.domain.message.dto.MessageRequestDto;
import com.phcworld.domain.message.dto.MessageResponseDto;
import com.phcworld.domain.user.User;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.repository.message.ChatRoomMessageRepository;
import com.phcworld.repository.message.ChatRoomRepository;
import com.phcworld.repository.message.ChatRoomUserRepository;
import com.phcworld.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatService {
    
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatRoomMessageRepository chatRoomMessageRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageResponseDto sendMessage(User loginUser, MessageRequestDto dto){
        ChatRoom chatRoom = getChatRoom(loginUser, dto);

        ChatRoomMessage message = ChatRoomMessage.builder()
                .message(dto.getMessage())
                .chatRoom(chatRoom)
                .writer(loginUser)
                .isRead(false)
                .build();
        chatRoomMessageRepository.save(message);
        return MessageResponseDto.of(message, loginUser.getName());
    }

    private ChatRoom getChatRoom(User loginUser, MessageRequestDto dto) {
        ChatRoom chatRoom = null;
        if(Objects.nonNull(dto.getChatRoomId())){
            chatRoom = chatRoomRepository.findById(dto.getChatRoomId())
                    .orElseThrow(NotFoundException::new);
        } else {
            List<Long> ids = dto.getToUserIds();
            chatRoom = chatRoomRepository.findByChatRoomByUsers(ids);

            if (chatRoom == null){
                chatRoom = getNewChatRoom(loginUser, ids);
            }
        }
        return chatRoom;
    }

    private ChatRoom getNewChatRoom(User loginUser, List<Long> ids) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoomRepository.save(chatRoom);

        for (Long id : ids) {
            User toUser = userRepository.findById(id)
                    .orElseThrow(NotFoundException::new);
            ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                    .user(toUser)
                    .chatRoom(chatRoom)
                    .build();
            chatRoomUserRepository.save(chatRoomUser);
        }

        ChatRoomUser fromUser = ChatRoomUser.builder()
                .user(loginUser)
                .chatRoom(chatRoom)
                .build();
        chatRoomUserRepository.save(fromUser);
        return chatRoom;
    }

    @Transactional(readOnly = true)
    public List<ChatRoomSelectDto> getChatRoomList(User loginUser){
        return chatRoomRepository.findChatRoomListByUser(loginUser);
    }
}
