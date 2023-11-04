package com.phcworld.service.statistics;

import com.phcworld.domain.statistics.HashtagStatisticsDto;
import com.phcworld.domain.statistics.StatisticsDto;
import com.phcworld.domain.statistics.UserStatisticsDto;
import com.phcworld.domain.user.User;
import com.querydsl.core.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

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

        List<StatisticsDto> userRegsiterList = statisticsService.getRegisterMemberStatistics(startDate, endDate);
        log.info("user list : {}", userRegsiterList);
//        List<UserStatistics> list = statisticsService.getRegisterMemberStatisticsForNativeQuery(startDate, endDate);
        List<StatisticsDto> diaryPostList = statisticsService.getPostDiaryStatistics(startDate, endDate);
        log.info("diary list : {}", diaryPostList);
    }

    @Test
    public void 회원_게시물_등록_카운트_by_서브쿼리(){
//        User user = User.builder()
//                .id(1L)
//                .build();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<UserStatisticsDto> countList = statisticsService.getUserPostStatistics(null);
        stopWatch.stop();
        log.info("select 시간 : {}", stopWatch.getTotalTimeSeconds());
        log.info("count list : {}", countList);
    }

    @Test
    @Ignore
    public void 회원_게시물_등록_카운트_by_조인(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<UserStatisticsDto> countList = statisticsService.getUserPostStatistics2();
        stopWatch.stop();
        log.info("select 시간 : {}", stopWatch.getTotalTimeSeconds());
        log.info("count list : {}", countList);
    }

    @Test
    public void 회원_일기_게시물_해시태그_통계(){
        User user = User.builder()
                .id(1L)
                .build();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<HashtagStatisticsDto> list = statisticsService.getDiaryHashtagStatistics(null);
        stopWatch.stop();
        log.info("select 시간 : {}", stopWatch.getTotalTimeSeconds());
        log.info("list : {}", list);
    }

    @Test
    public void 회원별_좋아요_카운트_통계(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<Tuple> list = statisticsService.getGoodCountByMember();
        stopWatch.stop();
        log.info("select 시간 : {}", stopWatch.getTotalTimeSeconds());
        log.info("list : {}", list);
    }
}