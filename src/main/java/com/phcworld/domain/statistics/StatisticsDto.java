package com.phcworld.domain.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDto {
    private String date;
    private Long count;
}
