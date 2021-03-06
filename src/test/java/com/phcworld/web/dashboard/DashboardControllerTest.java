package com.phcworld.web.dashboard;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.DashBoardUser;
import com.phcworld.domain.user.User;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.answer.DiaryAnswerServiceImpl;
import com.phcworld.service.answer.FreeBoardAnswerServiceImpl;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.service.board.FreeBoardServiceImpl;
import com.phcworld.service.dashboard.DashboardService;
import com.phcworld.service.timeline.TimelineServiceImpl;
import com.phcworld.utils.HttpSessionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(DashboardController.class)
public class DashboardControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private FreeBoardServiceImpl freeBoardService;

	@MockBean
	private FreeBoardAnswerServiceImpl freeBoardAnswerService;
	
	@MockBean
	private DiaryServiceImpl diaryService;
	
	@MockBean
	private DiaryAnswerServiceImpl diaryAnswerService;
	
	@MockBean
	private TimelineServiceImpl timelineService;
	
	@MockBean
	private AlertServiceImpl alertService;
	
	@MockBean
	private DashboardService dashboardService;

	@Test
	public void requestDashboardWhenEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(get("/dashboard")
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "로그인이 필요합니다."))
		.andExpect(model().size(1));
	}
	
	@Test
	public void requestDashboard() throws Exception {
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
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		DashBoardUser dashboardUser = DashBoardUser.builder()
				.user(user)
				.countOfAnswer(0)
				.countOfFreeBoard(0)
				.countOfDiary(0)
				.countOfAlert(0)
				.timelineList(null)
				.build();
		when(this.dashboardService.getDashBoardUser(user))
		.thenReturn(dashboardUser);
		DashBoardUser dashboard = dashboardService.getDashBoardUser(user);
		
		
		this.mvc.perform(get("/dashboard")
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/dashboard/dashboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("dashboard", dashboard))
		.andExpect(model().size(1));
	}
	
	@Test
	public void redirectFreeBoard() throws Exception {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		Timeline timeline = Timeline.builder()
				.id(1L)
				.type("free board")
				.icon("")
				.freeBoard(freeBoard)
				.user(user)
				.saveDate(freeBoard.getCreateDate())
				.build();
		when(this.timelineService.getOneTimeline(1L))
		.thenReturn(timeline);
		this.mvc.perform(get("/dashboard/timeline/{id}", 1L))
		.andExpect(redirectedUrl("/freeboards/" + timeline.getFreeBoard().getId()));
	}
	
	@Test
	public void redirectFreeBoardAnswer() throws Exception {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(user)
				.freeBoard(freeBoard)
				.contents("test")
				.build();
		Timeline timeline = Timeline.builder()
				.id(1L)
				.type("free board answer")
				.icon("")
				.freeBoardAnswer(freeBoardAnswer)
				.user(user)
				.saveDate(freeBoardAnswer.getCreateDate())
				.build();
		when(this.timelineService.getOneTimeline(1L))
		.thenReturn(timeline);
		this.mvc.perform(get("/dashboard/timeline/{id}", 1L))
		.andExpect(redirectedUrl("/freeboards/" + timeline.getFreeBoardAnswer().getFreeBoard().getId()));
	}
	
	@Test
	public void redirectDiary() throws Exception {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.thumbnail("")
				.createDate(LocalDateTime.now())
				.build();
		Timeline timeline = Timeline.builder()
				.id(1L)
				.type("diary")
				.icon("")
				.diary(diary)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		when(this.timelineService.getOneTimeline(1L))
		.thenReturn(timeline);
		this.mvc.perform(get("/dashboard/timeline/{id}", 1L))
		.andExpect(redirectedUrl("/diaries/" + timeline.getDiary().getId()));
	}
	
	@Test
	public void redirectDiaryAnswer() throws Exception {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.thumbnail("")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.id(1L)
				.writer(user)
				.diary(diary)
				.contents("test")
				.build();
		Timeline timeline = Timeline.builder()
				.type("diary answer")
				.icon("")
				.diaryAnswer(diaryAnswer)
				.user(user)
				.saveDate(diaryAnswer.getCreateDate())
				.build();
		when(this.timelineService.getOneTimeline(1L))
		.thenReturn(timeline);
		this.mvc.perform(get("/dashboard/timeline/{id}", 1L))
		.andExpect(redirectedUrl("/diaries/" + timeline.getDiaryAnswer().getDiary().getId()));
	}
	
	@Test
	public void redirectGood() throws Exception {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.thumbnail("")
				.createDate(LocalDateTime.now())
				.build();
		Good good = Good.builder()
				.id(1L)
				.diary(diary)
				.user(user)
				.createDate(LocalDateTime.now())
				.build();
		Timeline timeline = Timeline.builder()
				.type("good")
				.icon("")
				.good(good)
				.user(good.getUser())
				.saveDate(good.getCreateDate())
				.build();
		when(this.timelineService.getOneTimeline(1L))
		.thenReturn(timeline);
		this.mvc.perform(get("/dashboard/timeline/{id}", 1L))
		.andExpect(redirectedUrl("/diaries/" + timeline.getGood().getDiary().getId()));
	}

}
