package com.phcworld.domain.message.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageRequestDto {
    private Long chatRoomId;
    private Long toUserId;
    private String message;
}
