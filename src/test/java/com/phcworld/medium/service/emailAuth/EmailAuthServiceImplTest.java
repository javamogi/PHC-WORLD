package com.phcworld.medium.service.emailAuth;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.user.infrastructure.EmailAuth;
import com.phcworld.user.infrastructure.EmailAuthRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EmailAuthServiceImplTest {
	
	@Autowired
	private EmailAuthRepository emailAuthRepository;

	@Test
	@Transactional
	public void sendEmailAndAuthKey() {
		String authKey = UUID.randomUUID().toString().replaceAll("-", "");
		EmailAuth emailAuth = EmailAuth.builder()
				.email("test3@test.test")
				.authKey(authKey)
				.authenticate(false)
				.build();
		EmailAuth saveEmailAuth = emailAuthRepository.save(emailAuth);
		EmailAuth confirmEmailAuth = emailAuthRepository.findByEmail("test3@test.test");
		assertThat(confirmEmailAuth, is(saveEmailAuth));
	}

}
