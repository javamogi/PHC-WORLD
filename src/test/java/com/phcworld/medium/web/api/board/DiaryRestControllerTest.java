package com.phcworld.medium.web.api.board;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.web.api.board.DiaryRestController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.good.Good;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.utils.HttpSessionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(DiaryRestController.class)
public class DiaryRestControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private DiaryServiceImpl diaryService;
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void pushUpbuttonWhenEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(put("/api/diaries/{diaryId}/good", 1L)
				.with(csrf())
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("로그인을 해야합니다."));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void pushUpbutton() throws Exception {
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
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.createDate(LocalDateTime.now())
				.build();
		List<Good> list = new ArrayList<Good>();
		list.add(good);
		diary.setGoodPushedUser(list);
		
		SuccessResponse successResponse = SuccessResponse.builder()
				.success(Integer.toString(diary.getCountOfGood()))
				.build();
		when(diaryService.updateGood(diary.getId(), user))
		.thenReturn(successResponse);
		this.mvc.perform(put("/api/diaries/{diaryId}/good", 1L)
						.with(csrf())
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.success").value(successResponse.getSuccess()));
	}

}
