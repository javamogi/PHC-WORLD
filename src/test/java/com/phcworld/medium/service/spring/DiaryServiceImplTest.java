package com.phcworld.medium.service.spring;

import com.phcworld.domain.board.dto.DiaryResponse;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.repository.board.dto.DiarySelectDto;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.user.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class DiaryServiceImplTest {

    @Autowired
    private DiaryServiceImpl service;

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void select(){
        UserEntity user = userService.findUserByEmail("test@test.test");
        String searchKeyword = null;
        StopWatch queryStopWatch = new StopWatch();
        queryStopWatch.start();
        Page<DiarySelectDto> list = service.findPageDiary(user, 3, user, searchKeyword);
        queryStopWatch.stop();
        log.info("DB querydsl SELECT 시간 : {}", queryStopWatch.getTotalTimeSeconds());
        List<DiaryResponse> responseList = list.stream()
                .map(DiaryResponse::of)
                .collect(Collectors.toList());
        log.info("size : {}", list.getTotalPages());
        log.info("list size : {}", responseList.size());
        for (DiaryResponse r : responseList) {
            log.info("response : {}", r);
        }
    }

}
