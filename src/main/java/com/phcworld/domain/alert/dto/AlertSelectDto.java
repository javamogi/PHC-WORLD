package com.phcworld.domain.alert.dto;

import com.phcworld.domain.common.SaveType;
import com.phcworld.utils.LocalDateTimeUtils;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
public class AlertSelectDto {
    private Long id;
    private SaveType saveType;
    private String diaryTitle;
    private String freeBoardTitle;
    private String userName;
    private LocalDateTime createDate;

    public String getFormattedCreateDate() {
        return LocalDateTimeUtils.getTime(createDate);
    }
}
