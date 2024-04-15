package com.phcworld.medium.web.dashboard;

import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.api.dashboard.dto.UserResponseDto;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.embedded.PostInfo;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.freeboard.service.FreeBoardServiceImpl;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.controller.port.DashBoardUser;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.answer.DiaryAnswerServiceImpl;
import com.phcworld.answer.service.FreeBoardAnswerServiceImpl;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.service.dashboard.DashboardService;
import com.phcworld.service.timeline.TimelineServiceImpl;
import com.phcworld.utils.HttpSessionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringJUnit4ClassRunner.class)
//@WebMvcTest(value = DashboardController.class)
@SpringBootTest
@AutoConfigureMockMvc
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
						.with(csrf())
						.session(mockSession)
				)
				.andDo(print())
				.andExpect(view().name(containsString("/user/loginForm")))
				.andExpect(model().attribute("errorMessage", "로그인이 필요합니다."))
				.andExpect(model().size(1));
//				.andExpect(status().isUnauthorized());
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void requestDashboard() throws Exception {
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
		DashBoardUser dashboardUser = DashBoardUser.builder()
				.user(UserResponseDto.of(user))
				.countOfAnswer(0L)
				.countOfFreeBoard(0L)
				.countOfDiary(0L)
				.countOfAlert(0L)
				.timelineList(null)
				.build();
		when(this.dashboardService.getDashBoardUser(user))
		.thenReturn(dashboardUser);
		DashBoardUser dashboard = dashboardService.getDashBoardUser(user);
		
		
		this.mvc.perform(get("/dashboard")
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("dashboard/dashboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("dashboard", dashboard))
		.andExpect(model().size(1));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void redirectFreeBoard() throws Exception {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardEntity freeBoard = FreeBoardEntity.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD)
				.postId(freeBoard.getId())
				.redirectId(freeBoard.getId())
				.build();
		Timeline timeline = Timeline.builder()
				.id(1L)
//				.type("free board")
//				.icon("")
//				.freeBoard(freeBoard)
				.postInfo(postInfo)
				.user(user)
				.saveDate(freeBoard.getCreateDate())
				.build();
		when(this.timelineService.getOneTimeline(1L))
		.thenReturn(timeline);
		this.mvc.perform(get("/dashboard/timeline/{id}", 1L)
						.with(csrf()))
		.andExpect(redirectedUrl("/freeboards/" + timeline.getPostInfo().getRedirectId()));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void redirectFreeBoardAnswer() throws Exception {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardEntity freeBoard = FreeBoardEntity.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		FreeBoardAnswerEntity freeBoardAnswer = FreeBoardAnswerEntity.builder()
				.id(1L)
				.writer(user)
				.freeBoard(freeBoard)
				.contents("test")
				.build();
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.FREE_BOARD_ANSWER)
				.postId(freeBoardAnswer.getId())
				.redirectId(freeBoardAnswer.getFreeBoard().getId())
				.build();
		Timeline timeline = Timeline.builder()
				.id(1L)
//				.type("free board answer")
//				.icon("")
//				.freeBoardAnswer(freeBoardAnswer)
				.postInfo(postInfo)
				.user(user)
				.saveDate(freeBoardAnswer.getCreateDate())
				.build();
		when(this.timelineService.getOneTimeline(1L))
		.thenReturn(timeline);
		this.mvc.perform(get("/dashboard/timeline/{id}", 1L)
						.with(csrf()))
		.andExpect(redirectedUrl("/freeboards/" + timeline.getPostInfo().getRedirectId()));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void redirectDiary() throws Exception {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
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
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY)
				.postId(diary.getId())
				.redirectId(diary.getId())
				.build();
		Timeline timeline = Timeline.builder()
				.id(1L)
//				.type("diary")
//				.icon("")
//				.diary(diary)
				.postInfo(postInfo)
				.user(user)
				.saveDate(diary.getCreateDate())
				.build();
		when(this.timelineService.getOneTimeline(1L))
		.thenReturn(timeline);
		this.mvc.perform(get("/dashboard/timeline/{id}", 1L)
						.with(csrf()))
		.andExpect(redirectedUrl("/diaries/" + timeline.getPostInfo().getRedirectId()));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void redirectDiaryAnswer() throws Exception {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
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
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.DIARY_ANSWER)
				.postId(diaryAnswer.getId())
				.redirectId(diaryAnswer.getDiary().getId())
				.build();
		Timeline timeline = Timeline.builder()
//				.type("diary answer")
//				.icon("")
//				.diaryAnswer(diaryAnswer)
				.postInfo(postInfo)
				.user(user)
				.saveDate(diaryAnswer.getCreateDate())
				.build();
		when(this.timelineService.getOneTimeline(1L))
		.thenReturn(timeline);
		this.mvc.perform(get("/dashboard/timeline/{id}", 1L)
						.with(csrf()))
		.andExpect(redirectedUrl("/diaries/" + timeline.getPostInfo().getRedirectId()));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void redirectGood() throws Exception {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
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
		PostInfo postInfo = PostInfo.builder()
				.saveType(SaveType.GOOD)
				.postId(good.getId())
				.redirectId(good.getDiary().getId())
				.build();
		Timeline timeline = Timeline.builder()
//				.type("good")
//				.icon("")
//				.good(good)
				.postInfo(postInfo)
				.user(good.getUser())
				.saveDate(good.getCreateDate())
				.build();
		when(this.timelineService.getOneTimeline(1L))
		.thenReturn(timeline);
		this.mvc.perform(get("/dashboard/timeline/{id}", 1L)
						.with(csrf()))
		.andExpect(redirectedUrl("/diaries/" + timeline.getPostInfo().getRedirectId()));
	}

}
