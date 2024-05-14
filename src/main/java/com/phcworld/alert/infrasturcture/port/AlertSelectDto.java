package com.phcworld.alert.infrasturcture.port;

import com.phcworld.domain.common.SaveType;
import com.phcworld.utils.LocalDateTimeUtils;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AlertSelectDto {
    private Long id;
    private SaveType saveType;
//    private String diaryTitle;
//    private String freeBoardTitle;
    private String title;
    private String userName;
    private LocalDateTime createDate;

    public String getFormattedCreateDate() {
        return LocalDateTimeUtils.getTime(createDate);
    }
}
