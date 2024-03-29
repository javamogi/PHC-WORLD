package com.phcworld.user.service.port;

import com.phcworld.domain.statistics.UserStatisticsInterface;
import com.phcworld.user.domain.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    List<UserStatisticsInterface> findRegisterUserStatisticsForNativeQuery(LocalDate startDate, LocalDate endDate);

    User save(User user);
}
