package com.phcworld.domain.alert.dto;

import com.phcworld.domain.common.SaveType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class AlertResponseDto {
    private String userName;
    private String postTitle;
    private String formattedCreateDate;
    private Boolean isGood;

    public static AlertResponseDto of(AlertSelectDto alert){
        String postTitle = "";
        boolean isGood = true;
        if(alert.getSaveType() == SaveType.FREE_BOARD_ANSWER){
            postTitle = alert.getFreeBoardTitle();
            isGood = false;
        } else {
            postTitle = alert.getDiaryTitle();
        }

        return AlertResponseDto.builder()
                .userName(alert.getUserName())
                .postTitle(postTitle)
                .formattedCreateDate(alert.getFormattedCreateDate())
                .isGood(isGood)
                .build();
    }
}
