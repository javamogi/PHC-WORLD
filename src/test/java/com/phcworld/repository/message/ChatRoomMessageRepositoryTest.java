package com.phcworld.repository.message;

import com.phcworld.domain.message.ChatRoom;
import com.phcworld.domain.message.ChatRoomMessage;
import com.phcworld.domain.message.ChatRoomUser;
import com.phcworld.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ChatRoomMessageRepositoryTest {

    @Autowired
    private ChatRoomUserRepository chatRoomUserRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomMessageRepository messageRepository;

    @Test
    @Transactional
    public void registerMessage(){
        User fromUser = User.builder()
                .id(1L)
                .build();
        User toUser = User.builder()
                .id(2L)
                .build();
        ChatRoom chatRoom = new ChatRoom();
        chatRoomRepository.save(chatRoom);
        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .user(fromUser)
                .chatRoom(chatRoom)
                .build();
        ChatRoomUser chatRoomUser2 = ChatRoomUser.builder()
                .user(toUser)
                .chatRoom(chatRoom)
                .build();
        chatRoomUserRepository.save(chatRoomUser);
        chatRoomUserRepository.save(chatRoomUser2);
        ChatRoomMessage message = ChatRoomMessage.builder()
                .message("hi")
                .chatRoom(chatRoom)
                .writer(fromUser)
                .build();
        messageRepository.save(message);
        assertNotNull(message.getId());

    }

}