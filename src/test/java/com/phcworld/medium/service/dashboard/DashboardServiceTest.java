package com.phcworld.medium.service.dashboard;

import com.phcworld.service.dashboard.DashboardService;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.user.infrastructure.UserJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
@ActiveProfiles("dev")
public class DashboardServiceTest {

    @Autowired
    private DashboardService service;

    @Autowired
    private UserJpaRepository userRepository;

    @Test
    public void getCount(){
        UserEntity user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("없음"));
        long start = System.currentTimeMillis();
        service.getDashBoardUser(user);
        long end = System.currentTimeMillis();
        log.info("time : {}", end - start);
    }

}