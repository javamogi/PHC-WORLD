package com.phcworld.domain.message.dto;

import com.phcworld.domain.message.ChatRoomMessage;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public class MessageResponseDto {
    private Long messageId;
    private String writerName;
    private String message;
    private String sendDate;

    public static MessageResponseDto of(ChatRoomMessage message, String writerName){
        return MessageResponseDto.builder()
                .messageId(message.getId())
                .writerName(writerName)
                .message(message.getMessage())
                .sendDate(message.getSendDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")))
                .build();
    }
}
