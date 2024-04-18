package com.phcworld.freeboard.infrastructure.dto;

import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.utils.LocalDateTimeUtils;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreeBoardSelectDto {
    private Long id;
    private UserEntity writer;
    private String title;
    private String contents;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer count;
    private Long countOfAnswer;

    private Boolean isDeleted;

    public String getFormattedCreateDate() {
        return LocalDateTimeUtils.getTime(createDate);
    }

    public String getFormattedUpdateDate() {
        return LocalDateTimeUtils.getTime(updateDate);
    }

}
