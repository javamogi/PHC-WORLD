package com.phcworld.medium.repository.message;

import com.phcworld.domain.message.ChatRoom;
import com.phcworld.domain.message.ChatRoomUser;
import com.phcworld.repository.message.ChatRoomRepository;
import com.phcworld.repository.message.ChatRoomUserRepository;
import com.phcworld.user.infrastructure.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ChatRoomUserRepositoryTest {

    @Autowired
    private ChatRoomUserRepository chatRoomUserRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    @Transactional
    public void joinChatRoom(){
        UserEntity fromUser = UserEntity.builder()
                .id(1L)
                .build();
        UserEntity toUser = UserEntity.builder()
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
        assertNotNull(chatRoomUser.getId());
        assertNotNull(chatRoomUser2.getId());
    }

}