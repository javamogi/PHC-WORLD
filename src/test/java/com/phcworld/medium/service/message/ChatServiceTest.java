package com.phcworld.medium.service.message;

import com.phcworld.domain.message.dto.ChatRoomMessageResponseDto;
import com.phcworld.domain.message.dto.ChatRoomSelectDto;
import com.phcworld.domain.message.dto.MessageRequestDto;
import com.phcworld.domain.message.dto.MessageResponseDto;
import com.phcworld.service.message.ChatService;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class ChatServiceTest {

    @SpyBean
    private ChatService chatService;

    @Autowired
    private EntityManager em;

    @Test
    @Transactional
    public void 메세지_보냄(){
        UserEntity loginUser = UserEntity.builder()
                .id(1L)
                .name("테스트")
                .build();
        List<Long> ids = new ArrayList<>();
        ids.add(2L);
        MessageRequestDto dto = MessageRequestDto.builder()
                .toUserIds(ids)
                .message("hi")
                .build();
        MessageResponseDto responseDto = chatService.sendMessage(loginUser, dto);
        assertThat(responseDto.getMessage()).isEqualTo(dto.getMessage());
        assertThat(responseDto.getWriterName()).isEqualTo(loginUser.getName());
        assertThat(responseDto.getReadCount()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void 대화방_목록(){
        UserEntity loginUser = UserEntity.builder()
                .id(1L)
                .name("테스트")
                .build();
        List<Long> ids = new ArrayList<>();
        ids.add(2L);
        MessageRequestDto dto = MessageRequestDto.builder()
                .toUserIds(ids)
                .message("hi")
                .build();
        chatService.sendMessage(loginUser, dto);
        UserEntity user2 = UserEntity.builder()
                .id(2L)
                .build();
        ids.clear();
        ids.add(1L);
        MessageRequestDto dto2 = MessageRequestDto.builder()
                .toUserIds(ids)
                .message("하이요")
                .build();
        chatService.sendMessage(user2, dto2);
        ids.clear();
        ids.add(3L);
        MessageRequestDto dto3 = MessageRequestDto.builder()
                .toUserIds(ids)
                .message("hiyo")
                .build();
        chatService.sendMessage(user2, dto3);
        List<ChatRoomSelectDto> list = chatService.getChatRoomList(loginUser);
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void 대화방_메세지_목록(){
        UserEntity loginUser = UserEntity.builder()
                .id(1L)
                .email("test@test.test")
                .name("테스트")
                .authority(Authority.ROLE_ADMIN)
                .build();
        List<Long> ids = new ArrayList<>();
        ids.add(2L);
        MessageRequestDto dto = MessageRequestDto.builder()
                .toUserIds(ids)
                .message("hi")
                .build();
        MessageResponseDto message = chatService.sendMessage(loginUser, dto);
        UserEntity user2 = UserEntity.builder()
                .id(2L)
                .email("test2@test.test")
                .name("테스트2")
                .authority(Authority.ROLE_USER)
                .build();
        ids.clear();
        ids.add(1L);
        MessageRequestDto dto2 = MessageRequestDto.builder()
                .toUserIds(ids)
                .message("하이요")
                .build();
        chatService.sendMessage(user2, dto2);
        em.clear();
        List<ChatRoomMessageResponseDto> messages = chatService.getMessagesByChatRoom(message.getChatRoomId(), 1, loginUser);
        assertThat(messages.size()).isEqualTo(2);
        assertThat(messages.get(0).getReadCount()).isEqualTo(0);
        assertThat(messages.get(1).getReadCount()).isEqualTo(1);

    }

    @Test
    @Transactional
    public void 메세지_삭제_내용을_수정(){
        UserEntity loginUser = UserEntity.builder()
                .id(1L)
                .name("테스트")
                .build();
        List<Long> ids = new ArrayList<>();
        ids.add(2L);
        MessageRequestDto dto = MessageRequestDto.builder()
                .toUserIds(ids)
                .message("hi")
                .build();
        MessageResponseDto message = chatService.sendMessage(loginUser, dto);
        em.clear();
        MessageResponseDto responseDto = chatService.deleteMessage(message.getMessageId(), loginUser);
        assertThat(responseDto.getMessage()).isEqualTo("deleted");
    }
}