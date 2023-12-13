package com.phcworld.service.user;

import com.phcworld.api.user.dto.UserRequestDto;
import com.phcworld.domain.user.Authority;
import com.phcworld.domain.user.LoginRequestUser;
import com.phcworld.domain.user.User;
import com.phcworld.exception.model.BadRequestException;
import com.phcworld.exception.model.CustomException;
import com.phcworld.exception.model.DuplicationException;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.jwt.TokenProvider;
import com.phcworld.jwt.dto.TokenDto;
import com.phcworld.jwt.service.CustomUserDetailsService;
import com.phcworld.repository.user.UserRepository;
import com.phcworld.security.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsService userDetailsService;
	private final TokenProvider tokenProvider;

	public User registerUser(UserRequestDto requestUser) {
		User findedUser = userRepository.findByEmail(requestUser.getEmail());
		if(findedUser != null){
			throw new DuplicationException();
		}

		User user = User.builder()
				.email(requestUser.getEmail())
				.name(requestUser.getName())
				.password(passwordEncoder.encode(requestUser.getPassword()))
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.profileImage("blank-profile-picture.png")
				.build();

		return userRepository.save(user);
	}

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
		Authentication authentication = SecurityUtil.getAuthentication(authenticationToken, userDetailsService, passwordEncoder);
//		Authentication authentication = getAuthentication(authenticationToken);
//		Authentication authentication = authenticationManagerBuilder
//				.getObject()
//				.authenticate(authenticationToken);

		// 5. 토큰 발급
		return tokenProvider.generateTokenDto(authentication);
	}

//	private Authentication getAuthentication(UsernamePasswordAuthenticationToken authenticationToken) {
//		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//		authenticationProvider.setUserDetailsService(userDetailsService);
//		authenticationProvider.setPasswordEncoder(passwordEncoder);
//		return authenticationProvider.authenticate(authenticationToken);
//	}
}
