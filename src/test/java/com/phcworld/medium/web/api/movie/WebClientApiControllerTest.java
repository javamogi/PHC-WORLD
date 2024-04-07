package com.phcworld.medium.web.api.movie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WebClientApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
    public void 영화_검색_네이버_API() throws Exception {
        this.mvc.perform(get("/api/movies/pre")
                        .param("title", "어벤져스")
                )
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
    public void 영화_검색_네이버_API_webClient() throws Exception {
        this.mvc.perform(get("/api/movies")
                        .param("title", "오브젝트")
                )
                .andDo(print());
    }
}