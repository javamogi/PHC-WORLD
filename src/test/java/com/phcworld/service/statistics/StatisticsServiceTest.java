package com.phcworld.service.statistics;

import com.phcworld.domain.statistics.UserStatistics;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class StatisticsServiceTest {

    @SpyBean
    private StatisticsService statisticsService;

    @Test
    public void test(){
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        List<UserStatistics> list = statisticsService.getRegisterMemberStatistics(startDate, endDate);
//        List<UserStatistics> list = statisticsService.getRegisterMemberStatisticsForNativeQuery(startDate, endDate);
        log.info("list : {}", list);
    }
}