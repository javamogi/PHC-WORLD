package com.phcworld.medium.web.upload;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.web.upload.UploadController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.image.Image;
import com.phcworld.domain.image.ImageServiceImpl;
import com.phcworld.user.service.UserServiceImpl;
import com.phcworld.utils.HttpSessionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(UploadController.class)
public class UploadControllerTest {
	
	@Autowired
	private MockMvc mvc;

	@MockBean
	private ImageServiceImpl imageService;
	
	@MockBean
	private UserServiceImpl userService;
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void uploadImage() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		Image image = Image.builder()
				.id(1L)
				.writer(user)
				.originalFileName("test.jpg")
				.randFileName("1234.jpg")
				.size(9L)
				.createDate(LocalDateTime.now())
				.build();
		MockMultipartFile multipartFile = new MockMultipartFile("imageFile", "test.jpg", "image/jpeg", "test.jpg".getBytes());
		when(this.imageService.createImage(user, "baba2.jpg", "1234.jpg", 9L))
		.thenReturn(image);
		this.mvc.perform(multipart("/upload/imageUpload")
				.file(multipartFile)
				.with(csrf())
				.session(mockSession))
		.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void deleteImage() throws Exception {
		this.mvc.perform(get("/upload/deleteImage")
						.with(csrf())
				.param("randFileName", "123.jpg"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.success").value("파일이 존재하지 않습니다."));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void findImage() throws Exception {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		Image image = Image.builder()
				.id(1L)
				.writer(user)
				.originalFileName("test.jpg")
				.randFileName("123.jpg")
				.size(113L)
				.createDate(LocalDateTime.now())
				.build();
		when(this.imageService.findImageByRandFileName("123.jpg"))
		.thenReturn(image);
		this.mvc.perform(get("/upload/findImage")
				.param("randFileName", "123.jpg"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(image.getId()))
		.andExpect(jsonPath("$.originalFileName").value(image.getOriginalFileName()))
		.andExpect(jsonPath("$.randFileName").value(image.getRandFileName()))
		.andExpect(jsonPath("$.size").value(image.getSize()));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void uploadProfileImage() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		
		MockMultipartFile multipartFile = new MockMultipartFile("profileImage", "test.jpg", "image/jpeg", "test.jpg".getBytes());
		user.setProfileImage("123.jpg");
		when(this.userService.imageUpdate(user, "123.jpg"))
		.thenReturn(user);
		this.mvc.perform(multipart("/upload/profileUpload")
				.file(multipartFile)
				.with(csrf())
				.session(mockSession))
		.andExpect(status().isOk());
	}
	
}
