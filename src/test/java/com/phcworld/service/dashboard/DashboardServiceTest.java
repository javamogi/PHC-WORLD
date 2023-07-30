package com.phcworld.service.dashboard;

import com.phcworld.domain.user.User;
import com.phcworld.repository.user.UserRepository;
import com.phcworld.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
@ActiveProfiles("dev")
public class DashboardServiceTest {

    @Autowired
    private DashboardService service;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void getCount(){
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("없음"));
        long start = System.currentTimeMillis();
        service.getDashBoardUser(user);
        long end = System.currentTimeMillis();
        log.info("time : {}", end - start);
    }

}