package com.phcworld.service.user;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.user.User;
import com.phcworld.repository.user.UserRepository;
import com.phcworld.web.SecurityUtils;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User createUser(User user) throws NoSuchAlgorithmException {
		String password = SecurityUtils.getEncSHA256(user.getPassword());
		user.setPassword(password);
		user.setAuthority("ROLE_USER");
		user.setCreateDate(LocalDateTime.now());
		user.setProfileImage("blank-profile-picture.png");
		user.ifMeSetAdmin(user);
		
		return userRepository.save(user);
	}

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User findUserById(Long id) {
		return userRepository.getOne(id);
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
