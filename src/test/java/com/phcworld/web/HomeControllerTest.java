package com.phcworld.web;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.alert.AlertServiceImpl;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private AlertServiceImpl alertService;
	
	@Test
	public void alertServiceNotNull() {
		assertNotNull(alertService);
	}
	
	@Test
	public void index() throws Exception {
		this.mvc.perform(get(""))
		.andExpect(view().name(containsString("index")))
		.andExpect(status().isOk());
	}

	@Test
	public void redirectToAlert() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		Diary diary = new Diary(user, "test3", "test3", "no-image-icon.gif");
		diary.setId(1L);
		DiaryAnswer diaryAnswer = new DiaryAnswer(user, diary, "test");
		diaryAnswer.setId(1L); //pk값을 참조하기 때문에 pk값이 필요하다
		
		given(this.alertService.getOneAlert(1L))
		.willReturn(new Alert("Diary", diaryAnswer, diary.getWriter(), diaryAnswer.getCreateDate()));
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(get("/alert/{id}", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(redirectedUrl("/diary/" + 1L + "/detail"));

		FreeBoard freeBoard = new FreeBoard(user, "test", "test", "");
		freeBoard.setId(1L);
		FreeBoardAnswer freeBoardAnswer = new FreeBoardAnswer(user, freeBoard, "test");
		freeBoardAnswer.setId(1L);
		
		given(this.alertService.getOneAlert(2L))
		.willReturn(new Alert("FreeBoard", freeBoardAnswer, freeBoard.getWriter(), freeBoardAnswer.getCreateDate()));
		this.mvc.perform(get("/alert/{id}", 2L)
				.session(mockSession))
		.andDo(print())
		.andExpect(redirectedUrl("/freeboard/" + 1L + "/detail"));
		
		Good good = new Good(diary, user);
		good.setId(1L);
		given(this.alertService.getOneAlert(3L))
		.willReturn(new Alert("Diary", good, diary.getWriter(), good.getSaveDate()));
		this.mvc.perform(get("/alert/{id}", 3L)
				.session(mockSession))
		.andDo(print())
		.andExpect(redirectedUrl("/diary/" + 1L + "/detail"));
	}
}
