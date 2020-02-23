package com.phcworld.web.api.board;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.user.User;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.web.HttpSessionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(DiaryRestController.class)
public class DiaryRestControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private DiaryServiceImpl diaryService;
	
	@Test
	public void pushUpbuttonWhenEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(put("/api/diary/{diaryId}/good", 1L)
				.session(mockSession))
		.andExpect(jsonPath("$.error").value("로그인을 해야합니다."));
	}
	
	@Test
	public void pushUpbutton() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		Set<User> set = new HashSet<User>();
		set.add(user);
		diary.setGoodPushedUser(set);
		when(diaryService.updateGood(diary.getId(), user))
		.thenReturn("{\"success\":\""+Integer.toString(diary.getGoodPushedUser().size())+"\"}");
		this.mvc.perform(put("/api/diary/{diaryId}/good", 1L)
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.success").value(1));
	}

}