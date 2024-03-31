package com.phcworld.user.service;

import com.phcworld.mock.FakeMailSender;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.UserStatus;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


public class CertificateServiceTest {

    @Test
    public void 이메일과_인증키가_제데로_만들어져서_보내지는지_테스트(){
        // given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificateService certificateService = new CertificateService(fakeMailSender);
        User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .password("test")
                .name("테스트")
                .profileImage("blank-profile-picture.png")
                .createDate(LocalDateTime.now())
                .authority(Authority.ROLE_USER)
                .userStatus(UserStatus.ACTIVE)
                .certificationCode("1a2b3c")
                .build();

        // when
        certificateService.sendEmail(user);

        // then
        assertThat(fakeMailSender.email).isEqualTo("test@test.test");
        assertThat(fakeMailSender.certificationCode).isEqualTo("1a2b3c");
    }

}