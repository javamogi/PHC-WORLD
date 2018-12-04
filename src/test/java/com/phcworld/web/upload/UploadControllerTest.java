package com.phcworld.web.upload;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.image.Image;
import com.phcworld.domain.image.ImageServiceImpl;
import com.phcworld.domain.user.User;
import com.phcworld.domain.user.UserService;
import com.phcworld.web.HttpSessionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(UploadController.class)
public class UploadControllerTest {
	
	@Autowired
	private MockMvc mvc;

	@MockBean
	private ImageServiceImpl imageService;
	
	@MockBean
	private UserService userService;
	
	@Test
	public void uploadImage() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		Image image = new Image(user, "test.jpg", "1234.jpg", 9L);
		image.setId(1L);
		MockMultipartFile multipartFile = new MockMultipartFile("imageFile", "test.jpg", "image/jpeg", "test.jpg".getBytes());
		given(this.imageService.createImage(user, "baba2.jpg", "1234.jpg", 9L))
		.willReturn(image);
		this.mvc.perform(multipart("/upload/imageUpload")
				.file(multipartFile)
				.session(mockSession))
		.andExpect(status().isOk());
	}

	@Test
	public void deleteImage() throws Exception {
		this.mvc.perform(get("/upload/deleteImage")
				.param("randFileName", "123.jpg"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.success").value("success"));
	}
	
	@Test
	public void findImage() throws Exception {
		User user = new User("test@test.test", "test", "테스트");
		Image image = new Image(user, "test.jpg", "123.jpg", 113L);
		image.setId(1L);
		given(this.imageService.findImageByRandFileName("123.jpg"))
		.willReturn(image);
		this.mvc.perform(get("/upload/findImage")
				.param("randFileName", "123.jpg"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(image.getId()))
		.andExpect(jsonPath("$.originalFileName").value(image.getOriginalFileName()))
		.andExpect(jsonPath("$.randFileName").value(image.getRandFileName()))
		.andExpect(jsonPath("$.size").value(image.getSize()));
	}
	
	@Test
	public void uploadProfileImage() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		Image image = new Image(user, "test.jpg", "123.jpg", 123L);
		image.setId(1L);
		MockMultipartFile multipartFile = new MockMultipartFile("profileImage", "test.jpg", "image/jpeg", "test.jpg".getBytes());
		user.setProfileImage("123.jpg");
		given(this.userService.imageUpdate(user, "123.jpg"))
		.willReturn(user);
		this.mvc.perform(multipart("/upload/profileUpload")
				.file(multipartFile)
				.session(mockSession))
		.andExpect(status().isOk());
	}
	
}
