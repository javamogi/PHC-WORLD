package com.phcworld.user.service;

import com.phcworld.common.infrastructure.LocalDateTimeHolder;
import com.phcworld.exception.model.DuplicationException;
import com.phcworld.user.controller.port.UserService;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.service.port.EmailAuthService;
import com.phcworld.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Builder
public class NewUserServiceImpl implements UserService {

	private final PasswordEncoder passwordEncoder;
	private final LocalDateTimeHolder localDateTimeHolder;
	private final UserRepository userRepository;
	private final EmailAuthService emailAuthService;

	@Override
	public User registerUser(UserRequest requestUser) {
		userRepository.findByEmail(requestUser.getEmail())
				.ifPresent(
						user -> {
							throw new DuplicationException();
						}
				);

		User user = User.from(requestUser, passwordEncoder, localDateTimeHolder);
		user = userRepository.save(user);
		emailAuthService.sendEmail(user.getEmail());
		return user;
	}

}
