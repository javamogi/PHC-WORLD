package com.phcworld.mock;

import com.phcworld.common.infrastructure.LocalDateTimeHolder;
import com.phcworld.user.controller.UserController;
import com.phcworld.user.controller.port.UserService;
import com.phcworld.user.service.CertificateService;
import com.phcworld.user.service.NewUserServiceImpl;
import com.phcworld.user.service.port.MailSender;
import com.phcworld.user.service.port.UserRepository;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestContainer {
    public final UserController userController;

    public final PasswordEncoder passwordEncoder;

    public final UserService userService;

    public final MailSender mailSender;

    public final UserRepository userRepository;

    @Builder
    public TestContainer(LocalDateTimeHolder localDateTimeHolder){
        this.passwordEncoder = new FakePasswordEncode("test2");
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.userService = NewUserServiceImpl.builder()
                .userRepository(userRepository)
                .certificateService(new CertificateService(mailSender))
                .passwordEncoder(passwordEncoder)
                .localDateTimeHolder(localDateTimeHolder)
                .build();
        this.userController = UserController.builder()
                .userService(userService)
                .build();
    }
}
