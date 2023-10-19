package com.phcworld.repository.user;

import com.phcworld.domain.statistics.UserStatistics;

import java.time.LocalDate;
import java.util.List;

public interface UserRepositoryCustom {
    List<UserStatistics> findRegisterUserStatistics(LocalDate startDate, LocalDate endDate);
}
