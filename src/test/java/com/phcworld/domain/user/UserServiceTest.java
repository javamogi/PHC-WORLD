package com.phcworld.domain.user;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceTest {
	
	private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);
	
	@Autowired
	private UserService userService;
	
	@Test
	@Transactional
	public void createUser() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		log.debug("User : {}", user);
		User temp = userService.createUser(user);
		User actual = userService.getOneUser(temp.getId());
		log.debug("actual : {}", actual);
		assertThat(actual, is(temp));
	}
	
	@Test
	@Transactional
	public void findUserByEmail() throws Exception {
		User user = new User("test3@test.test", "test", "테스트");
		userService.createUser(user);
		log.debug("User : {}", user);
		User findByEmailUser = userService.findUserByEmail("test3@test.test");
		log.debug("find User : {}", findByEmailUser);
		assertThat(findByEmailUser, is(user));
	}
	
	@Test
	@Transactional
	public void updateUser() throws Exception {
		User user = userService.getOneUser(1L);
		User actual = new User("test@test.test", "test1", "테스트1");
		userService.updateUser(user, actual);
		User updatedUser = userService.findUserByEmail(user.getEmail());
		log.debug("updatedUser : {}", updatedUser);
		assertThat(updatedUser.getPassword(), is(actual.getPassword()));
		assertThat(updatedUser.getName(), is(actual.getName()));
	}
	
	@Test
	@Transactional
	public void updateImage() throws Exception {
		User user = userService.findUserById(1L);
		userService.imageUpdate(user, "test.jpg");
		User actual = userService.findUserById(1L);
		assertThat("test.jpg", is(actual.getProfileImage()));
	}

}
