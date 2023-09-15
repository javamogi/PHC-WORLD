package com.phcworld.service.message;

import com.phcworld.domain.message.ChatRoom;
import com.phcworld.domain.message.ChatRoomMessage;
import com.phcworld.domain.message.ChatRoomUser;
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
        User toUser = userRepository.findById(dto.getToUserId())
                .orElseThrow(NotFoundException::new);
        ChatRoom chatRoom = getChatRoom(loginUser, dto, toUser);

        ChatRoomMessage message = ChatRoomMessage.builder()
                .message(dto.getMessage())
                .chatRoom(chatRoom)
                .writer(loginUser)
                .build();
        chatRoomMessageRepository.save(message);
        return MessageResponseDto.of(message, loginUser.getName());
    }

    private ChatRoom getChatRoom(User loginUser, MessageRequestDto dto, User toUser) {
        ChatRoom chatRoom = new ChatRoom();
        if(Objects.nonNull(dto.getChatRoomId())){
            chatRoom = chatRoomRepository.findById(dto.getChatRoomId())
                    .orElseThrow(NotFoundException::new);
        } else {
            chatRoomRepository.save(chatRoom);
            ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                    .user(toUser)
                    .chatRoom(chatRoom)
                    .build();
            ChatRoomUser chatRoomUser2 = ChatRoomUser.builder()
                    .user(loginUser)
                    .chatRoom(chatRoom)
                    .build();
            chatRoomUserRepository.save(chatRoomUser);
            chatRoomUserRepository.save(chatRoomUser2);
        }
        return chatRoom;
    }

}
