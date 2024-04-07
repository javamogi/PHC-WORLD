package com.phcworld.service.statistics;

import com.phcworld.domain.statistics.HashtagStatisticsDto;
import com.phcworld.domain.statistics.StatisticsDto;
import com.phcworld.domain.statistics.UserStatisticsDto;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.repository.statistics.StatisticsRepository;
import com.phcworld.user.infrastructure.UserJpaRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final UserJpaRepository userRepository;

    private final StatisticsRepository statisticsRepository;

    @Transactional
    public List<StatisticsDto> getRegisterMemberStatistics(LocalDate startDate, LocalDate endDate){
        return statisticsRepository.findRegisterUserStatistics(startDate, endDate);
    }

    @Transactional
    public List<StatisticsDto> getRegisterMemberStatisticsForNativeQuery(LocalDate startDate, LocalDate endDate){
        return userRepository.findRegisterUserStatisticsForNativeQuery(startDate, endDate)
                .stream()
                .map(r -> {
                    return StatisticsDto.builder()
                            .date(r.getDate())
                            .count(r.getCount())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<StatisticsDto> getPostDiaryStatistics(LocalDate startDate, LocalDate endDate){
        return statisticsRepository.findPostDiaryStatistics(startDate, endDate);
    }

    @Transactional
    public List<UserStatisticsDto> getUserPostStatistics(UserEntity user){
        return statisticsRepository.findUserPostRegisterStatistics(user);
    }

    @Transactional
    public List<UserStatisticsDto> getUserPostStatistics2(){
        return statisticsRepository.findUserPostRegisterStatistics2();
    }

    @Transactional(readOnly = true)
    public List<HashtagStatisticsDto> getDiaryHashtagStatistics(UserEntity user){
        return statisticsRepository.findDiaryHashtagStatistics(user);
    }

    @Transactional(readOnly = true)
    public List<Tuple> getGoodCountByMember(){
        return statisticsRepository.findGoodCountByMember();
    }
}
