package com.phcworld.web.mail;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.emailAuth.EmailAuth;
import com.phcworld.repository.emailAuth.EmailAuthRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(EmailController.class)
public class EmailControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private EmailAuthRepository emailAuthRepository;

	@Test
	public void whenNotLoginUserEmailAuthKeyConfirm() throws Exception{
		given(emailAuthRepository.findByEmail("email@test.test"))
		.willReturn(null);
		this.mvc.perform(get("/email/emailConfirm")
				.param("email", "email@test.test")
				.param("authKey", "1234"))
		.andExpect(view().name(containsString("/user/form")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "없는 이메일 주소입니다. 가입 먼저하세요!"))
		.andExpect(model().size(1));
	}
	
	@Test
	public void whenNotEqualAuthKeyEmailUser() throws Exception{
		EmailAuth emailAuth = EmailAuth.builder()
				.email("email@test.test")
				.authKey("1234")
				.authenticate(false)
				.build();
		given(emailAuthRepository.findByEmail("email@test.test"))
		.willReturn(emailAuth);
		this.mvc.perform(get("/email/emailConfirm")
				.param("email", "email@test.test")
				.param("authKey", "2345"))
		.andExpect(view().name(containsString("/user/form")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "인증키가 맞지 않습니다."))
		.andExpect(model().size(1));
	}
	
	@Test
	public void successAuthKeyConfirm() throws Exception{
		EmailAuth emailAuth = EmailAuth.builder()
				.email("email@test.test")
				.authKey("1234")
				.authenticate(false)
				.build();
		given(emailAuthRepository.findByEmail("email@test.test"))
		.willReturn(emailAuth);
		this.mvc.perform(get("/email/emailConfirm")
				.param("email", "email@test.test")
				.param("authKey", "1234"))
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("authMessage", "인증되었습니다. 로그인하세요!"))
		.andExpect(model().size(1));
	}

}
