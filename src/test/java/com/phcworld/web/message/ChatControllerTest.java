package com.phcworld.web.message;

import com.phcworld.domain.message.dto.ChatRoomSelectDto;
import com.phcworld.domain.message.dto.MessageRequestDto;
import com.phcworld.domain.user.User;
import com.phcworld.repository.message.ChatRoomMessageRepository;
import com.phcworld.repository.message.ChatRoomRepository;
import com.phcworld.repository.message.ChatRoomUserRepository;
import com.phcworld.repository.user.UserRepository;
import com.phcworld.service.message.ChatService;
import com.phcworld.utils.HttpSessionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ChatControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ChatService chatService;

    @Test
    public void 로그인_회원_채팅_목록() throws Exception {
        MockHttpSession mockSession = new MockHttpSession();
        User user = User.builder()
                .id(1L)
                .email("test3@test.test")
                .password("test3")
                .name("테스트3")
                .profileImage("blank-profile-picture.png")
                .authority("ROLE_USER")
                .createDate(LocalDateTime.now())
                .build();
        mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);

        List<String> names = new ArrayList<>();
        names.add(user.getName());
        names.add("테스트2");
        ChatRoomSelectDto dto = ChatRoomSelectDto.builder()
                .chatRoomId(1L)
                .users(names)
                .lastMessage("hi")
                .isRead(false)
                .date(LocalDateTime.now())
                .build();

        List<ChatRoomSelectDto> chatRoomList = new ArrayList<>();
        chatRoomList.add(dto);

        when(chatService.getChatRoomList(user)).thenReturn(chatRoomList);

        this.mvc.perform(get("/chat/rooms")
                        .session(mockSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].chatRoomId").value(1))
                .andExpect(jsonPath("$[0].users[0]").value("테스트3"))
                .andExpect(jsonPath("$[0].users[1]").value("테스트2"))
                .andExpect(jsonPath("$[0].lastMessage").value("hi"))
                .andExpect(jsonPath("$[0].isRead").value(false))
                .andExpect(jsonPath("$[0].date").value("방금전"))
                .andDo(print());
    }
}