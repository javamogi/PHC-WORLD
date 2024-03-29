package com.phcworld.service.message;

import com.phcworld.domain.message.ChatRoom;
import com.phcworld.domain.message.ChatRoomMessage;
import com.phcworld.domain.message.ChatRoomUser;
import com.phcworld.domain.message.MessageReadUser;
import com.phcworld.domain.message.dto.*;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.repository.message.ChatRoomMessageRepository;
import com.phcworld.repository.message.ChatRoomRepository;
import com.phcworld.repository.message.ChatRoomUserRepository;
import com.phcworld.repository.message.MessageReadUserRepository;
import com.phcworld.user.infrastructure.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatRoomMessageRepository chatRoomMessageRepository;
    private final UserJpaRepository userRepository;
    private final MessageReadUserRepository messageReadUserRepository;

    @Transactional
    public MessageResponseDto sendMessage(UserEntity loginUser, MessageRequestDto dto){
        ChatRoom chatRoom = getChatRoom(loginUser, dto);

        ChatRoomMessage message = ChatRoomMessage.builder()
                .message(dto.getMessage())
                .chatRoom(chatRoom)
                .writer(loginUser)
//                .isRead(false)
                .build();
        chatRoomMessageRepository.save(message);

        List<MessageReadUser> readUsers = saveReadUser(dto, message);
        message.setReadUsers(readUsers);

        return MessageResponseDto.of(message);
    }

    private List<MessageReadUser> saveReadUser(MessageRequestDto dto, ChatRoomMessage message) {
        List<MessageReadUser> readUsers = new ArrayList<>();
        for (Long id : dto.getToUserIds()) {
            UserEntity readUser = userRepository.findById(id)
                    .orElseThrow(NotFoundException::new);
            MessageReadUser messageReadUser = MessageReadUser.builder()
                    .user(readUser)
                    .message(message)
                    .build();
            readUsers.add(messageReadUserRepository.save(messageReadUser));
        }
        return readUsers;
    }

    private ChatRoom getChatRoom(UserEntity loginUser, MessageRequestDto dto) {
        ChatRoom chatRoom = null;
        if(Objects.nonNull(dto.getChatRoomId())){
            chatRoom = chatRoomRepository.findById(dto.getChatRoomId())
                    .orElseThrow(NotFoundException::new);
        } else {
            List<Long> ids = dto.getToUserIds();
            chatRoom = chatRoomRepository.findChatRoomByUsers(ids);

            if (chatRoom == null){
                chatRoom = getNewChatRoom(loginUser, ids);
            }
        }
        return chatRoom;
    }

    private ChatRoom getNewChatRoom(UserEntity loginUser, List<Long> ids) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoomRepository.save(chatRoom);

        for (Long id : ids) {
            UserEntity toUser = userRepository.findById(id)
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
    public List<ChatRoomSelectDto> getChatRoomList(UserEntity loginUser){
        return chatRoomRepository.findChatRoomListByUser(loginUser);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomMessageResponseDto> getMessagesByChatRoom(Long chatRoomId, int pageNum, UserEntity loginUser){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(NotFoundException::new);
        if(!chatRoom.containsUser(loginUser)){
            throw new NotMatchUserException();
        }
        PageRequest pageRequest = PageRequest.of(pageNum - 1, 10);
        List<MessageSelectDto> list = chatRoomRepository.findMessagesByChatRoom(chatRoom, pageRequest);
//        List<ChatRoomMessage> list = chatRoomMessageRepository.findByChatRoomOrderBySendDateDesc(chatRoom, pageRequest);
        removeReader(loginUser, list);
        return list.stream()
                .map(ChatRoomMessageResponseDto::of)
                .collect(Collectors.toList());
    }

    private void removeReader(UserEntity loginUser, List<MessageSelectDto> list) {
        for (int i = 0; i < list.size(); i++){
            MessageSelectDto message = list.get(0);
            for(int j = 0; j < message.getReadUsers().size(); j++){
                UserEntity readUser = message.getReadUsers().get(j);
                if(readUser.equals(loginUser)){
                    message.removeReadUser(readUser);
                }
            }
        }
    }

//    private void removeReader(User loginUser, List<ChatRoomMessage> list) {
//        for (int i = 0; i < list.size(); i++){
//            ChatRoomMessage message = list.get(0);
//            for(int j = 0; j < message.getReadUsers().size(); j++){
//                MessageReadUser readUser = message.getReadUsers().get(j);
//                if(readUser.readUser(loginUser)){
//                    message.removeReadUser(readUser);
//                }
//            }
//        }
//    }

    @Transactional
    public MessageResponseDto deleteMessage(Long messageId, UserEntity loginUser){
        ChatRoomMessage message = chatRoomMessageRepository.findById(messageId)
                .orElseThrow(NotFoundException::new);
        if(!message.isSameWriter(loginUser)){
            throw new NotMatchUserException();
        }
        message.deleteMessage();
        return MessageResponseDto.of(message);
    }
}
