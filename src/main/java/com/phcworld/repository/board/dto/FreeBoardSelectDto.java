package com.phcworld.repository.board.dto;

import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.utils.LocalDateTimeUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class FreeBoardSelectDto {
    private Long id;
    private UserEntity writer;
    private String title;
    private String contents;
    private String icon;
    private String badge;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer count;
    private Long countOfAnswer;

    public String getFormattedCreateDate() {
        return LocalDateTimeUtils.getTime(createDate);
    }

    public String getFormattedUpdateDate() {
        return LocalDateTimeUtils.getTime(updateDate);
    }
}
