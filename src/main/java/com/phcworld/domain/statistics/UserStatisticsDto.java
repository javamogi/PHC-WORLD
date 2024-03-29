package com.phcworld.domain.statistics;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserStatisticsDto {
    private Long totalPostCount;
    private Long diaryPostCount;
    private Long freeBoardPostCount;
    private Long diaryAnswerPostCount;
    private Long freeBoardAnswerPostCount;
    private String userId;

    public Long getTotalPostCount(){
        return diaryPostCount + freeBoardPostCount;
    }
}
