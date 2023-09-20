package com.phcworld.web.message;

import com.phcworld.domain.message.dto.ChatRoomSelectDto;
import com.phcworld.domain.user.User;
import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.service.message.ChatService;
import com.phcworld.utils.HttpSessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/rooms")
    public List<ChatRoomSelectDto> getChatRoomListByUser(HttpSession session){
        if(!HttpSessionUtils.isLoginUser(session)) {
            throw new NotMatchUserException();
        }
        User loginUser = HttpSessionUtils.getUserFromSession(session);

        return chatService.getChatRoomList(loginUser);
    }
}
