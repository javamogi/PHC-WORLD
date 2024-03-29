package com.phcworld.user.infrastructure;

import com.phcworld.domain.statistics.UserStatisticsInterface;
import com.phcworld.user.domain.User;
import com.phcworld.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserEntity::toModel);
    }

    @Override
    public List<UserStatisticsInterface> findRegisterUserStatisticsForNativeQuery(LocalDate startDate, LocalDate endDate) {
        return userJpaRepository.findRegisterUserStatisticsForNativeQuery(startDate, endDate);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.from(user)).toModel();
    }
}
