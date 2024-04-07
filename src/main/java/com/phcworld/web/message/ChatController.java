package com.phcworld.web.message;

import com.phcworld.domain.message.dto.ChatRoomMessageResponseDto;
import com.phcworld.domain.message.dto.ChatRoomSelectDto;
import com.phcworld.domain.message.dto.MessageRequestDto;
import com.phcworld.domain.message.dto.MessageResponseDto;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.service.message.ChatService;
import com.phcworld.utils.HttpSessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
        UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);

        return chatService.getChatRoomList(loginUser);
    }

    @PostMapping("/message")
    public MessageResponseDto sendMessage(@RequestBody MessageRequestDto dto, HttpSession session){
        if(!HttpSessionUtils.isLoginUser(session)) {
            throw new NotMatchUserException();
        }
        UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
        return chatService.sendMessage(loginUser, dto);
    }

    @GetMapping("/{chatRoomId}/messages")
    public List<ChatRoomMessageResponseDto> getMessagesByChatRoom(@PathVariable Long chatRoomId,
                                                                  @RequestParam(defaultValue = "1") int pageNum,
                                                                  HttpSession session){
        if(!HttpSessionUtils.isLoginUser(session)) {
            throw new NotMatchUserException();
        }
        UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
        return chatService.getMessagesByChatRoom(chatRoomId, pageNum, loginUser);
    }

    @PatchMapping("/messages/{messageId}")
    public MessageResponseDto deleteMessage(@PathVariable Long messageId,
                                            HttpSession session){
        if(!HttpSessionUtils.isLoginUser(session)) {
            throw new NotMatchUserException();
        }
        UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
        return chatService.deleteMessage(messageId, loginUser);
    }
}
