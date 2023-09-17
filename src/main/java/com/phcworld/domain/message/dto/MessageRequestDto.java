package com.phcworld.domain.message.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MessageRequestDto {
    private Long chatRoomId;
    private List<Long> toUserIds;
    private String message;
}
