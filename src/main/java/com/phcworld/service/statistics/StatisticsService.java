package com.phcworld.service.statistics;

import com.phcworld.domain.statistics.UserStatistics;
import com.phcworld.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final UserRepository userRepository;

    @Transactional
    public List<UserStatistics> getRegisterMemberStatistics(LocalDate startDate, LocalDate endDate){
        return userRepository.findRegisterUserStatistics(startDate, endDate);
    }

    @Transactional
    public List<UserStatistics> getRegisterMemberStatisticsForNativeQuery(LocalDate startDate, LocalDate endDate){
        return userRepository.findRegisterUserStatisticsForNativeQuery(startDate, endDate)
                .stream()
                .map(r -> {
                    return UserStatistics.builder()
                            .date(r.getDate())
                            .count(r.getCount())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
