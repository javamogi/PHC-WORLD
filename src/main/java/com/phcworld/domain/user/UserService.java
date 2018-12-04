package com.phcworld.domain.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.web.SecurityUtils;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User findUserByEmail(String email) {
		return  userRepository.findByEmail(email);
	}
	
	public User getOneUser(Long id) {
		return userRepository.getOne(id);
	}
	
	public User createUser(User user) {
		String password = SecurityUtils.getEncSHA256(user.getPassword());
		user.setPassword(password);
		if(user.getEmail().equals("pakoh200@naver.com")) {
			user.setAuthority("ROLE_ADMIN");
		}
		user.setCreateDate();
		return userRepository.save(user);
	}
	
	public void updateUser(User loginUser, User newUser) {
		loginUser.update(newUser);
		userRepository.save(loginUser);
	}
	
	public User imageUpdate(User loginUser, String profileImage) {
		loginUser.setProfileImage(profileImage);
		return userRepository.save(loginUser);
	}

	public User findUserById(Long id) {
		return userRepository.getOne(id);
	}
}
