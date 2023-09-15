package com.phcworld.service.message;

import com.phcworld.domain.message.dto.MessageRequestDto;
import com.phcworld.domain.message.dto.MessageResponseDto;
import com.phcworld.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ChatServiceTest {

    @SpyBean
    private ChatService chatService;

    @Test
    public void 메세지_보냄(){
        User loginUser = User.builder()
                .id(1L)
                .name("테스트")
                .build();
        MessageRequestDto dto = MessageRequestDto.builder()
                .toUserId(2L)
                .message("hi")
                .build();
        MessageResponseDto responseDto = chatService.sendMessage(loginUser, dto);
        assertThat(responseDto.getMessage()).isEqualTo(dto.getMessage());
        assertThat(responseDto.getWriterName()).isEqualTo(loginUser.getName());
    }
}