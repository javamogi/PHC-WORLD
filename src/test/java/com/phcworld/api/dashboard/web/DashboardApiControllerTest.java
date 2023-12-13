package com.phcworld.api.dashboard.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class DashboardApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void 대시보드_가져오기() throws Exception {
        this.mvc.perform(get("/api/dashboard")
                        .with(csrf()))
                .andDo(print());
    }
}