package com.phcworld.domain.message.dto;

import com.phcworld.domain.message.ChatRoomMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@Builder
@Getter
public class MessageResponseDto {
    private Long messageId;
    private String writerName;
    private String message;
    private String sendDate;
    private Long chatRoomId;
    private Integer readCount;

    public static MessageResponseDto of(ChatRoomMessage message){
        return MessageResponseDto.builder()
                .messageId(message.getId())
                .writerName(message.getWriterName())
                .message(message.getMessage())
                .sendDate(message.getSendDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")))
                .chatRoomId(message.getChatRoom().getId())
                .readCount(message.getReadUsers() != null ? message.getReadUsers().size() : 0)
                .build();
    }
}
