package com.phcworld.medium.web.jwt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JwtMockMvcTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void requestDashboardWhenEmptyLoginUser() throws Exception {
        this.mvc.perform(get("/dashboard")
                                .header("Authorization", "Bearer abcd")
                )
                .andDo(print());
    }
}
