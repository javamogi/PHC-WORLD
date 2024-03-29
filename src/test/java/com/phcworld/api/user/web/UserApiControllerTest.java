package com.phcworld.api.user.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void 회원가입_성공() throws Exception {
        this.mvc.perform(post("/api/users")
                        .with(csrf())
                        .param("email", "abcdefg@test.test")
                        .param("password", "abcde")
                        .param("name", "에이비씨디이"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 회원가입_실패_중복_이메일() throws Exception {
        this.mvc.perform(post("/api/users")
                        .with(csrf())
                        .param("email", "test@test.test")
                        .param("password", "abcde")
                        .param("name", "에이비씨디이"))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void 회원가입_실패_모든_요소_빈값() throws Exception {
        this.mvc.perform(post("/api/users")
                        .with(csrf())
                        .param("email", "")
                        .param("password", "")
                        .param("name", ""))
                .andDo(print())
                .andExpect(jsonPath("$.messages.length()").value(6))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입_실패_이메일_입력_없음() throws Exception {
        this.mvc.perform(post("/api/users")
                        .with(csrf())
                        .param("email", "")
                        .param("password", "password")
                        .param("name", "name"))
                .andDo(print())
                .andExpect(jsonPath("$.messages.length()").value(1))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입_실패_이메일_형식_아님() throws Exception {
        this.mvc.perform(post("/api/users")
                        .with(csrf())
                        .param("email", "test")
                        .param("password", "password")
                        .param("name", "name"))
                .andDo(print())
                .andExpect(jsonPath("$.messages.[0]").value("이메일 형식이 아닙니다."))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입_실패_비밀번호_입력_없음() throws Exception {
        this.mvc.perform(post("/api/users")
                        .with(csrf())
                        .param("email", "testttt@test.test")
                        .param("password", "")
                        .param("name", "name"))
                .andDo(print())
                .andExpect(jsonPath("$.messages.length()").value(2))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입_실패_비밀번호_입력_최소입력_미만() throws Exception {
        this.mvc.perform(post("/api/users")
                        .with(csrf())
                        .param("email", "testttt@test.test")
                        .param("password", "tes")
                        .param("name", "name"))
                .andDo(print())
                .andExpect(jsonPath("$.messages.[0]").value("비밀번호는 4자 이상으로 해야합니다."))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입_실패_이름_입력_없음() throws Exception {
        this.mvc.perform(post("/api/users")
                        .with(csrf())
                        .param("email", "testttt@test.test")
                        .param("password", "password")
                        .param("name", ""))
                .andDo(print())
                .andExpect(jsonPath("$.messages.length()").value(3))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입_실패_이름_특수문자_입력() throws Exception {
        this.mvc.perform(post("/api/users")
                        .with(csrf())
                        .param("email", "testttt@test.test")
                        .param("password", "password")
                        .param("name", "!@#$$"))
                .andDo(print())
                .andExpect(jsonPath("$.messages.[0]").value("이름은 한글, 영문, 숫자만 가능합니다."))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입_실패_이름_최소입력_미만() throws Exception {
        this.mvc.perform(post("/api/users")
                        .with(csrf())
                        .param("email", "testttt@test.test")
                        .param("password", "password")
                        .param("name", "ab"))
                .andDo(print())
                .andExpect(jsonPath("$.messages.[0]").value("이름은 영문 3자 이상 20자 이하 또는 한글 두자이상 6자 이하로 해야합니다."))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입_실패_이름_최대입력_초과() throws Exception {
        this.mvc.perform(post("/api/users")
                        .with(csrf())
                        .param("email", "testttt@test.test")
                        .param("password", "password")
                        .param("name", "aaaaabbbbbcccccddddde"))
                .andDo(print())
                .andExpect(jsonPath("$.messages.[0]").value("이름은 영문 3자 이상 20자 이하 또는 한글 두자이상 6자 이하로 해야합니다."))
                .andExpect(status().isBadRequest());
    }

}