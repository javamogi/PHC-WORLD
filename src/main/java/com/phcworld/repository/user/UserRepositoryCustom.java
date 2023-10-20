package com.phcworld.repository.user;

import com.phcworld.domain.statistics.StatisticsDto;

import java.time.LocalDate;
import java.util.List;

public interface UserRepositoryCustom {
    List<StatisticsDto> findRegisterUserStatistics(LocalDate startDate, LocalDate endDate);
}
