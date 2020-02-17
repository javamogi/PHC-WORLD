package com.phcworld.service.user;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.user.User;
import com.phcworld.repository.user.UserRepository;
import com.phcworld.service.user.UserService;

import lombok.extern.slf4j.Slf4j;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class UserServiceTest {
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private UserRepository userRepository;
	
	@Test
	public void createUser() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		when(userService.createUser(user)).thenReturn(user);
		User createUser = userService.createUser(user);
		assertThat(createUser.getEmail(), is(user.getEmail()));
	}
	
	@Test
	public void createMeAndSetAdmin() throws Exception {
		User user = new User("pakoh200@naver.com", "test", "주인장");
		when(userService.createUser(user)).thenReturn(user);
		User adminUser = userService.createUser(user);
		adminUser.ifMeSetAdmin(user);
		assertThat(adminUser.getAuthority(), is("ROLE_ADMIN"));
	}
	
	@Test
	@Transactional
	public void findUserByEmail() throws Exception {
		User user = new User("test3@test.test", "test", "테스트");
		userService.createUser(user);
		User findByEmailUser = userService.findUserByEmail("test3@test.test");
		assertThat(findByEmailUser, is(user));
	}
	
	@Test
	@Transactional
	public void updateUser() throws Exception {
		User user = userService.getOneUser(1L);
		User actual = new User("test@test.test", "test1", "테스트1");
		userService.updateUser(user, actual);
		User updatedUser = userService.findUserByEmail(user.getEmail());
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
