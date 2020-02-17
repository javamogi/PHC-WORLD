package com.phcworld.service.user;

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

	public User createUser(User user) {
		String password = SecurityUtils.getEncSHA256(user.getPassword());
		user.setPassword(password);
		user.ifMeSetAdmin(user);
		return userRepository.save(user);
	}

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User getOneUser(Long id) {
		return userRepository.getOne(id);
	}

	public User findUserById(Long id) {
		return userRepository.getOne(id);
	}

	public void updateUser(User loginUser, User newUser) {
		loginUser.update(newUser);
		userRepository.save(loginUser);
	}

	public User imageUpdate(User loginUser, String profileImage) {
		loginUser.setProfileImage(profileImage);
		return userRepository.save(loginUser);
	}
	
}
