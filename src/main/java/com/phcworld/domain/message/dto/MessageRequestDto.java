package com.phcworld.domain.message.dto;

import lombok.Getter;

@Getter
public class MessageRequestDto {
    private Long chatRoomId;
    private Long toUserId;
    private String message;
}
