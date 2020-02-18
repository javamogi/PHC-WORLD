package com.phcworld.repository.user;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.user.User;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;

	@Test
	public void createUser() {
		User user = User.builder()
				.email("test3@test.test")
				.password("test3")
				.name("test3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		User newUser = userRepository.save(user);
		log.info("User : {}", newUser);
		assertNotNull(newUser);
	}
	
	@Test
	public void selectUserByEmail() {
		createUser();
		User selectUser = userRepository.findByEmail("user@test.test");
		assertNotNull(selectUser);		
	}
	
	@Test
	public void updateUser() {
		createUser();
		User user = userRepository.findByEmail("user@test.test");
		User updateUser = User.builder()
				.password("user1")
				.name("user1")
				.build();
		user.update(updateUser);
		User updatedUser = userRepository.save(user);
		assertEquals(updateUser.getPassword(), updatedUser.getPassword());
		assertEquals(updateUser.getName(), updatedUser.getName());
	}
	
	@Test
	public void deleteUser() {
		createUser();
		User selectUser = userRepository.findByEmail("user@test.test");
		userRepository.delete(selectUser);
		User deletedUser = userRepository.findByEmail("user@test.test");
		assertNull(deletedUser);
	}
}
