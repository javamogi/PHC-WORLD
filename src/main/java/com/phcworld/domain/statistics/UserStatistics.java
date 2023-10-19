package com.phcworld.domain.statistics;

import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class UserStatistics {
    private String date;
    private Long count;
}
