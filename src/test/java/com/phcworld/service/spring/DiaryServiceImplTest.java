package com.phcworld.service.spring;

import com.phcworld.domain.board.dto.DiaryResponse;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.dto.DiarySelectDto;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.service.user.UserService;
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
    private UserService userService;

    @Test
    public void select(){
        User user = userService.findUserByEmail("test@test.test");
        StopWatch queryStopWatch = new StopWatch();
        queryStopWatch.start();
        Page<DiarySelectDto> list = service.findPageDiary(user, 3, user);
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
