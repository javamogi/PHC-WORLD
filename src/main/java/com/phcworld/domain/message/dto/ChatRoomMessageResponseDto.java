package com.phcworld.domain.message.dto;

import com.phcworld.domain.message.ChatRoomMessage;
import com.phcworld.utils.LocalDateTimeUtils;
import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class ChatRoomMessageResponseDto {
    private Long messageId;
    private String message;
    private String writerName;
    private String writerImgUrl;
    private String sendDate;
    private String read;

    public static ChatRoomMessageResponseDto of(ChatRoomMessage message){
        return ChatRoomMessageResponseDto.builder()
                .messageId(message.getId())
                .message(message.getMessage())
                .writerName(message.getWriter().getName())
                .writerImgUrl(message.getWriter().getProfileImage())
                .sendDate(LocalDateTimeUtils.getTime(message.getSendDate()))
                .read(message.getIsRead() ? "읽음" : "읽지 않음")
                .build();
    }
}
