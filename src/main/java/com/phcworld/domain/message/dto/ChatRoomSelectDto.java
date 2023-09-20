package com.phcworld.domain.message.dto;

import com.phcworld.domain.message.ChatRoomUser;
import com.phcworld.utils.LocalDateTimeUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ToString
@Builder
public class ChatRoomSelectDto {

    private Long chatRoomId;
    private List<String> users;
    private String lastMessage;
    private Boolean isRead;
    private LocalDateTime date;

    public String getDate(){
        return LocalDateTimeUtils.getTime(date);
    }
}
