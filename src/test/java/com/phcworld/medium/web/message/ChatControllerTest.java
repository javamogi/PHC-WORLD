package com.phcworld.medium.web.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phcworld.domain.message.dto.ChatRoomSelectDto;
import com.phcworld.domain.message.dto.MessageRequestDto;
import com.phcworld.domain.message.dto.MessageResponseDto;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.service.message.ChatService;
import com.phcworld.utils.HttpSessionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ChatControllerTest {

    @Autowired
    private MockMvc mvc;

    @SpyBean
    private ChatService chatService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private EntityManager em;

    private MockHttpSession mockSession;

    private UserEntity user;

    @Before
    public void setup(){
        mockSession = new MockHttpSession();
        user = UserEntity.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .authority(Authority.ROLE_ADMIN)
                .createDate(LocalDateTime.now())
                .build();
        mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
    }

    @Test
    @WithMockUser(username = "test@test.test", authorities = "ROLE_ADMIN")
    public void 로그인_회원_채팅_목록() throws Exception {
        List<String> names = new ArrayList<>();
        names.add(user.getName());
        names.add("테스트2");
        ChatRoomSelectDto dto = ChatRoomSelectDto.builder()
                .chatRoomId(1L)
                .users(names)
                .lastMessage("hi")
//                .isRead(false)
                .count(0)
                .date(LocalDateTime.now())
                .build();

        List<ChatRoomSelectDto> chatRoomList = new ArrayList<>();
        chatRoomList.add(dto);

        when(chatService.getChatRoomList(user)).thenReturn(chatRoomList);

        this.mvc.perform(get("/chat/rooms")
                        .with(csrf())
                        .session(mockSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].chatRoomId").value(1))
                .andExpect(jsonPath("$[0].users[0]").value("테스트"))
                .andExpect(jsonPath("$[0].users[1]").value("테스트2"))
                .andExpect(jsonPath("$[0].lastMessage").value("hi"))
//                .andExpect(jsonPath("$[0].isRead").value(false))
                .andExpect(jsonPath("$[0].date").value("방금전"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "test@test.test", authorities = "ROLE_ADMIN")
    public void 메세지_보냄() throws Exception {
        List<Long> ids = new ArrayList<>();
        ids.add(2L);
        MessageRequestDto dto = MessageRequestDto.builder()
                .toUserIds(ids)
                .message("hi")
                .build();

        String content = mapper.writeValueAsString(dto);

        MessageResponseDto responseDto = MessageResponseDto.builder()
                .messageId(1L)
                .writerName(user.getName())
                .message(dto.getMessage())
                .sendDate("방금전")
                .build();
        when(chatService.sendMessage(user, dto)).thenReturn(responseDto);

        this.mvc.perform(post("/chat/message")
                        .with(csrf())
                        .session(mockSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageId").value(1))
                .andExpect(jsonPath("$.writerName").value(user.getName()))
                .andExpect(jsonPath("$.message").value(dto.getMessage()))
                .andExpect(jsonPath("$.sendDate").value(responseDto.getSendDate()))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "test@test.test", authorities = "ROLE_ADMIN")
    @Transactional
    public void 메세지_목록() throws Exception {
        List<Long> ids = new ArrayList<>();
        ids.add(2L);
        MessageRequestDto dto = MessageRequestDto.builder()
                .toUserIds(ids)
                .message("hi")
                .build();
        MessageResponseDto message = chatService.sendMessage(user, dto);
        em.clear();
        this.mvc.perform(get("/chat/{chatRoomId}/messages", message.getChatRoomId())
                        .with(csrf())
                        .param("pageNum", "1")
                        .session(mockSession)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].messageId").value(1))
                .andExpect(jsonPath("$[0].message").value("hi"))
                .andExpect(jsonPath("$[0].writerName").value(user.getName()))
                .andExpect(jsonPath("$[0].writerImgUrl").value(user.getProfileImage()))
//                .andExpect(jsonPath("$[0].read").value("읽지 않음"))
                .andExpect(jsonPath("$[0].sendDate").value("방금전"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "test@test.test", authorities = "ROLE_ADMIN")
    @Transactional
    public void 메세지_삭제() throws Exception {

        List<Long> ids = new ArrayList<>();
        ids.add(2L);
        MessageRequestDto dto = MessageRequestDto.builder()
                .toUserIds(ids)
                .message("hi")
                .build();

        MessageResponseDto message = chatService.sendMessage(user, dto);

        this.mvc.perform(patch("/chat/messages/{messageId}", message.getMessageId())
                        .with(csrf())
                        .session(mockSession)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageId").value(message.getMessageId()))
                .andExpect(jsonPath("$.writerName").value("테스트"))
                .andExpect(jsonPath("$.message").value("deleted"))
                .andDo(print());
    }
}