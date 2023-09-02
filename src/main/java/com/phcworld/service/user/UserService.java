package com.phcworld.service.user;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import com.phcworld.exception.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.user.User;
import com.phcworld.repository.user.UserRepository;
import com.phcworld.utils.SecurityUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User createUser(User user) throws NoSuchAlgorithmException {
		String password = SecurityUtils.getEncSHA256(user.getPassword());
		user.setPassword(password);
		user.setAuthority("ROLE_USER");
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
	
}
