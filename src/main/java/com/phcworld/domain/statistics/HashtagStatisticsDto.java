package com.phcworld.domain.statistics;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HashtagStatisticsDto {
    private String hashtagName;
    private Long count;
}
