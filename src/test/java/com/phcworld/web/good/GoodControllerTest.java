package com.phcworld.web.good;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.good.GoodServiceImpl;
import com.phcworld.domain.user.User;
import com.phcworld.web.HttpSessionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(GoodController.class)
public class GoodControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private GoodServiceImpl goodService;

	@Test
	public void whenNotLoginUserUpToGood() throws Exception {
		this.mvc.perform(get("/good/{diaryId}", 1L))
		.andExpect(jsonPath("$.error").value("로그인을 해야합니다."));
	}
	
	@Test
	public void upGood() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		Diary diary = new Diary(user, "test3", "test3", "no-image-icon.gif");
		diary.setId(1L);
		given(this.goodService.upGood(diary.getId(), user))
		.willReturn("1");
		this.mvc.perform(get("/good/{diaryId}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.success").value("1"));
	}

}
