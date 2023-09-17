package com.phcworld.repository.message;

import com.phcworld.domain.message.ChatRoom;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ChatRoomRepositoryTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    @Transactional
    public void create(){
        ChatRoom chatRoom = new ChatRoom();
        chatRoomRepository.save(chatRoom);
        assertNotNull(chatRoom.getId());
    }

}