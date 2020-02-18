package com.phcworld.domain.image;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ImageServiceImplTest {
	
	@Autowired
	private ImageServiceImpl imageService;

	@Test
	@Transactional
	public void createImage() {
		User user = User.builder()
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		user.setId(1L);
		Image image = imageService.createImage(user, "haha.jpg", "1234.jpg", 100L);
		Image actual = imageService.getOneImage(image.getId());
		assertThat(actual, is(image));
	}
	
	@Test
	@Transactional
	public void findImageByRandFileName() throws Exception {
		User user = User.builder()
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		user.setId(1L);
		Image image = imageService.createImage(user, "haha.jpg", "1234.jpg", 100L);
		Image actual = imageService.findImageByRandFileName("1234.jpg");
		assertThat(actual, is(image));
	}
	
	@Test(expected = JpaObjectRetrievalFailureException.class)
	@Transactional
	public void deleteImageById() throws Exception {
		User user = User.builder()
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		user.setId(1L);
		Image image = imageService.createImage(user, "haha.jpg", "1234.jpg", 100L);
		Long id = image.getId();
		imageService.deleteImageById(id);
		imageService.getOneImage(id);
	}

}
