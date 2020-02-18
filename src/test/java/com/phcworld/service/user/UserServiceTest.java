package com.phcworld.service.user;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phcworld.domain.user.User;
import com.phcworld.repository.user.UserRepository;
import com.phcworld.service.user.UserService;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceTest {
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private UserRepository userRepository;
	
	@Test
	public void createUser() throws Exception {
		User user = User.builder()
				.email("test3@test.test")
				.password("test3")
				.name("test3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		when(userService.createUser(user)).thenReturn(user);
		User createUser = userService.createUser(user);
		assertThat(createUser.getEmail(), is(user.getEmail()));
	}
	
	@Test
	public void createMeAndSetAdmin() throws Exception {
		User user = User.builder()
				.email("pakoh200@naver.com")
				.password("test")
				.name("주인장")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		when(userService.createUser(user)).thenReturn(user);
		User adminUser = userService.createUser(user);
		adminUser.ifMeSetAdmin(user);
		assertThat(adminUser.getAuthority(), is("ROLE_ADMIN"));
	}
	
	@Test
	public void findUserByEmail() throws Exception {
		User user = User.builder()
				.email("test3@test.test")
				.password("test3")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		when(userService.createUser(user)).thenReturn(user);
		User createUser = userService.createUser(user);
		when(userService.findUserByEmail("test3@test.test")).thenReturn(createUser);
		User findUser = userService.findUserByEmail("test3@test.test");
		assertThat(findUser, is(createUser));
	}
	
	@Test
	public void updateUser() throws Exception {
		User user = User.builder()
				.email("test@test.test")
				.password("test1")
				.name("테스트1")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		when(userService.createUser(user)).thenReturn(user);
		User createUser = userService.createUser(user);
		User updateUser = User.builder()
				.email("test@test.test")
				.password("test2")
				.name("테스트2")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		createUser.update(updateUser);
		when(userService.findUserByEmail(user.getEmail())).thenReturn(updateUser);
		User updatedUser = userService.findUserByEmail(user.getEmail());
		assertThat(updatedUser, is(createUser));
	}
	
	@Test
	public void updateImage() throws Exception {
		User user = User.builder()
				.email("test@test.test")
				.password("test1")
				.name("테스트1")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		User updateUser = User.builder()
				.email("test@test.test")
				.password("test1")
				.name("테스트1")
				.profileImage("test.jpg")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		when(userService.imageUpdate(user, "test.jpg")).thenReturn(updateUser);
		User updatedUser = userService.imageUpdate(user, "test.jpg");
		assertThat("test.jpg", is(updatedUser.getProfileImage()));
	}

}
