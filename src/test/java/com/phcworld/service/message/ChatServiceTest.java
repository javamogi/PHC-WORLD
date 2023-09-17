package com.phcworld.service.message;

import com.phcworld.domain.message.dto.ChatRoomMessageResponseDto;
import com.phcworld.domain.message.dto.ChatRoomSelectDto;
import com.phcworld.domain.message.dto.MessageRequestDto;
import com.phcworld.domain.message.dto.MessageResponseDto;
import com.phcworld.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class ChatServiceTest {

    @SpyBean
    private ChatService chatService;

    @Test
    @Transactional
    public void 메세지_보냄(){
        User loginUser = User.builder()
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
    }

    @Test
    public void 대화방_목록(){
        User loginUser = User.builder()
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
        User user2 = User.builder()
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
    public void 대화방_메세지_목록(){
        User loginUser = User.builder()
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
        User user2 = User.builder()
                .id(2L)
                .build();
        ids.clear();
        ids.add(1L);
        MessageRequestDto dto2 = MessageRequestDto.builder()
                .toUserIds(ids)
                .message("하이요")
                .build();
        chatService.sendMessage(user2, dto2);
        List<ChatRoomMessageResponseDto> messages = chatService.getMessagesByChatRoom(1L, 1);
        assertThat(messages.size()).isEqualTo(2);
    }
}