package com.phcworld.service.user;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;

import com.phcworld.domain.user.Authority;
import com.phcworld.domain.user.LoginRequestUser;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.jwt.TokenProvider;
import com.phcworld.jwt.dto.TokenDto;
import com.phcworld.jwt.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.user.User;
import com.phcworld.repository.user.UserRepository;
import com.phcworld.utils.SecurityUtils;

import javax.security.auth.login.CredentialException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsService userDetailsService;
	private final TokenProvider tokenProvider;

	public User createUser(User user) {
//		String password = SecurityUtils.getEncSHA256(user.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setAuthority(Authority.ROLE_USER);
		user.setCreateDate(LocalDateTime.now());
		user.setProfileImage("blank-profile-picture.png");
		user.ifMeSetAdmin();
		
		return userRepository.save(user);
	}

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User findUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(NotFoundException::new);
	}
	
	public User updateUser(User loginUser, User newUser) {
		loginUser.update(newUser);
		return userRepository.save(loginUser);
	}

	public User imageUpdate(User loginUser, String profileImage) {
		loginUser.setProfileImage(profileImage);
		return userRepository.save(loginUser);
	}

	public TokenDto tokenLogin(LoginRequestUser requestUser) {
		String email = requestUser.getEmail();
		User user = userRepository.findByEmail(email);
		if(user == null){
			throw new NotFoundException();
		}

		// 비밀번호 확인 + spring security 객체 생성 후 JWT 토큰 생성
		UsernamePasswordAuthenticationToken authenticationToken = user.toAuthentication(requestUser.getPassword());
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		Authentication authentication = authenticationProvider.authenticate(authenticationToken);

		// 5. 토큰 발급
		return tokenProvider.generateTokenDto(authentication);
	}
}
