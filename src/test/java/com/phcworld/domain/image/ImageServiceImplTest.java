package com.phcworld.domain.image;

import com.phcworld.domain.user.User;
import com.phcworld.exception.model.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ImageServiceImplTest {
	
	@Autowired
	private ImageServiceImpl imageService;

	private User user;

	@Before
	public void createUser(){
		user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
	}

	@Test
	@Transactional
	public void createImage() {
		Image image = imageService.createImage(user, "haha.jpg", "1234.jpg", 100L);
		Image actual = imageService.getOneImage(image.getId());
		assertThat(actual).isEqualTo(image);
	}
	
	@Test
	@Transactional
	public void findImageByRandFileName() throws Exception {
		Image image = imageService.createImage(user, "haha.jpg", "1234.jpg", 100L);
		Image actual = imageService.findImageByRandFileName("1234.jpg");
		assertThat(actual).isEqualTo(image);
	}
	
	@Test(expected = NotFoundException.class)
	@Transactional
	public void deleteImageById() throws Exception {
		Image image = imageService.createImage(user, "haha.jpg", "1234.jpg", 100L);
		Long id = image.getId();
		imageService.deleteImageById(id);
		imageService.getOneImage(id);
	}

}
