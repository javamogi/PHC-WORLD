package com.phcworld.domain.message.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MessageRequestDto {
    private Long chatRoomId;
    private List<Long> toUserIds;
    private String message;
}
