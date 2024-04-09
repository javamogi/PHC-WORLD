package com.phcworld.mock;

import com.phcworld.common.infrastructure.LocalDateTimeHolder;
import com.phcworld.common.infrastructure.UuidHolder;
import com.phcworld.freeboard.controller.NewFreeBoardController;
import com.phcworld.freeboard.controller.port.NewFreeBoardService;
import com.phcworld.freeboard.service.NewFreeBoardServiceImpl;
import com.phcworld.freeboard.service.port.FreeBoardRepository;
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

    public final UuidHolder uuidHolder;

    public final NewFreeBoardController freeBoardController;

    public final NewFreeBoardService freeBoardService;

    public final FreeBoardRepository freeBoardRepository;

    @Builder
    public TestContainer(LocalDateTimeHolder localDateTimeHolder){
        this.passwordEncoder = new FakePasswordEncode("test2");
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.uuidHolder = new FakeUuidHolder("1a2b3c");
        this.userService = NewUserServiceImpl.builder()
                .userRepository(userRepository)
                .certificateService(new CertificateService(mailSender))
                .passwordEncoder(passwordEncoder)
                .uuidHolder(uuidHolder)
                .localDateTimeHolder(localDateTimeHolder)
                .build();
        this.userController = UserController.builder()
                .userService(userService)
                .build();
        this.freeBoardRepository = new FakeFreeBoardRepository();
        this.freeBoardService = NewFreeBoardServiceImpl.builder()
                .freeBoardRepository(freeBoardRepository)
                .localDateTimeHolder(localDateTimeHolder)
                .build();
        this.freeBoardController = NewFreeBoardController.builder()
                .freeBoardService(freeBoardService)
                .build();
    }
}
