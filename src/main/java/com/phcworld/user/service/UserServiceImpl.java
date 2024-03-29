package com.phcworld.user.service;

import com.phcworld.common.infrastructure.LocalDateTimeHolder;
import com.phcworld.exception.model.DuplicationException;
import com.phcworld.exception.model.EmailSendErrorException;
import com.phcworld.user.controller.port.UserService;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
import com.phcworld.user.domain.dto.LoginRequestUser;
import com.phcworld.user.domain.dto.UserRequest;
import com.phcworld.user.infrastructure.EmailAuthServiceImpl;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.jwt.TokenProvider;
import com.phcworld.jwt.dto.TokenDto;
import com.phcworld.jwt.service.CustomUserDetailsService;
import com.phcworld.user.infrastructure.UserJpaRepository;
import com.phcworld.security.utils.SecurityUtil;
import com.phcworld.user.service.port.EmailAuthService;
import com.phcworld.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Builder
public class UserServiceImpl {

	private final UserJpaRepository userJpaRepository;
	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsService userDetailsService;
	private final TokenProvider tokenProvider;
	private final EmailAuthServiceImpl emailAuthServiceImpl;

	public UserEntity createUser(UserEntity user) {
//		String password = SecurityUtils.getEncSHA256(user.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setAuthority(Authority.ROLE_USER);
		user.setCreateDate(LocalDateTime.now());
		user.setProfileImage("blank-profile-picture.png");
		user.ifMeSetAdmin();
		
		return userJpaRepository.save(user);
	}

	public UserEntity findUserByEmail(String email) {
		return userJpaRepository.findByEmail(email).get();
	}

	public UserEntity findUserById(Long id) {
		return userJpaRepository.findById(id)
				.orElseThrow(NotFoundException::new);
	}
	
	public UserEntity updateUser(UserEntity loginUser, UserEntity newUser) {
		loginUser.update(newUser);
		return userJpaRepository.save(loginUser);
	}

	public UserEntity imageUpdate(UserEntity loginUser, String profileImage) {
		loginUser.setProfileImage(profileImage);
		return userJpaRepository.save(loginUser);
	}

	public TokenDto tokenLogin(LoginRequestUser requestUser) {
		String email = requestUser.getEmail();
		UserEntity user = userJpaRepository.findByEmail(email).get();
		if(user == null){
			throw new NotFoundException();
		}

		// 비밀번호 확인 + spring security 객체 생성 후 JWT 토큰 생성
		UsernamePasswordAuthenticationToken authenticationToken = user.toAuthentication(requestUser.getPassword());
		Authentication authentication = SecurityUtil.getAuthentication(authenticationToken, userDetailsService, passwordEncoder);
//		Authentication authentication = getAuthentication(authenticationToken);
//		Authentication authentication = authenticationManagerBuilder
//				.getObject()
//				.authenticate(authenticationToken);

		// 5. 토큰 발급
		return tokenProvider.generateTokenDto(authentication);
	}

}
