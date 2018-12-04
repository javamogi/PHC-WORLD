package com.phcworld.web.dashboard;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.alert.AlertServiceImpl;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.DiaryAnswerServiceImpl;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.answer.FreeBoardAnswerServiceImpl;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryServiceImpl;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.board.FreeBoardServiceImpl;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.timeline.TimelineServiceImpl;
import com.phcworld.domain.user.User;
import com.phcworld.web.HttpSessionUtils;

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

	@Test
	public void whenNotLoginUserDashboard() throws Exception {
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
	public void dashboard() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		
		FreeBoard freeBoard = new FreeBoard(user, "test", "", "test");
		freeBoard.setId(1L);
		List<FreeBoard> freeBoardList = new ArrayList<>();
		freeBoardList.add(freeBoard);
		given(this.freeBoardService.findFreeBoardListByWriter(user))
		.willReturn(freeBoardList);
		
		FreeBoardAnswer freeBoardAnswer = new FreeBoardAnswer(user, freeBoard, "test");
		freeBoardAnswer.setId(1L);
		List<FreeBoardAnswer> freeBoardAnswerList = new ArrayList<>();
		freeBoardAnswerList.add(freeBoardAnswer);
		given(this.freeBoardAnswerService.findFreeBoardAnswerListByWriter(user))
		.willReturn(freeBoardAnswerList);
		
		Diary diary = new Diary(user, "test", "test", "");
		diary.setId(1L);
		List<Diary> diaryList = new ArrayList<>();
		diaryList.add(diary);
		given(this.diaryService.findDiaryListByWriter(user))
		.willReturn(diaryList);
		
		DiaryAnswer diaryAnswer = new DiaryAnswer(user, diary, "test");
		diaryAnswer.setId(1L);
		List<DiaryAnswer> diaryAnswerList = new ArrayList<>();
		diaryAnswerList.add(diaryAnswer);
		given(this.diaryAnswerService.findDiaryAnswerListByWriter(user))
		.willReturn(diaryAnswerList);
		
		Alert alert = new Alert("FreeBoard Answer", freeBoardAnswer, user, freeBoardAnswer.getCreateDate());
		alert.setId(1L);
		List<Alert> alertList = new ArrayList<>();
		alertList.add(alert);
		given(this.alertService.findPageRequestAlertByUser(user))
		.willReturn(alertList);
		
		Timeline timeline = new Timeline("FreeBoard", "", freeBoard, user, freeBoard.getCreateDate());
		timeline.setId(1L);
		List<Timeline> timelineList = new ArrayList<>();
		timelineList.add(timeline);
		Page<Timeline> pageTimeline = new PageImpl<Timeline>(timelineList);
		given(this.timelineService.findPageTimelineByUser(user))
		.willReturn(pageTimeline);
		
		this.mvc.perform(get("/dashboard")
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/dashboard/dashboard")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("countAnswers", freeBoardAnswerList.size() + diaryAnswerList.size()))
		.andExpect(model().attribute("countFreeboards", freeBoardList.size()))
		.andExpect(model().attribute("countDiaries", diaryList.size()))
		.andExpect(model().attribute("countAlerts", alertList.size()))
		.andExpect(model().attribute("timelines", timelineList))
		.andExpect(model().size(5));
	}
	
	@Test
	public void redirectToTimeline() throws Exception {
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		
		FreeBoard freeBoard = new FreeBoard(user, "test", "", "test");
		freeBoard.setId(1L);
		
		FreeBoardAnswer freeBoardAnswer = new FreeBoardAnswer(user, freeBoard, "test");
		freeBoardAnswer.setId(1L);
		
		Diary diary = new Diary(user, "test", "test", "");
		diary.setId(1L);
		
		DiaryAnswer diaryAnswer = new DiaryAnswer(user, diary, "test");
		diaryAnswer.setId(1L);
		
		Timeline timeline = new Timeline("FreeBoard", "", freeBoard, user, freeBoard.getCreateDate());
		timeline.setId(1L);
		given(this.timelineService.getOneTimeline(1L))
		.willReturn(timeline);
		
		this.mvc.perform(get("/dashboard/timeline/{id}", 1L))
		.andExpect(redirectedUrl("/freeboard/" + timeline.getFreeBoard().getId() + "/detail"));
		
		Timeline timeline2 = new Timeline("FreeBoard Answer", "", freeBoard, freeBoardAnswer, user, freeBoardAnswer.getCreateDate());
		timeline2.setId(2L);
		given(this.timelineService.getOneTimeline(2L))
		.willReturn(timeline2);
		
		this.mvc.perform(get("/dashboard/timeline/{id}", 2L))
		.andExpect(redirectedUrl("/freeboard/" + timeline2.getFreeBoard().getId() + "/detail"));
		
		Timeline timeline3 = new Timeline("Diary", "", diary, user, diary.getCreateDate());
		timeline3.setId(3L);
		given(this.timelineService.getOneTimeline(3L))
		.willReturn(timeline3);
		
		this.mvc.perform(get("/dashboard/timeline/{id}", 3L))
		.andExpect(redirectedUrl("/diary/" + timeline3.getDiary().getId() + "/detail"));

		Timeline timeline4 = new Timeline("Diary Answer", "", diary, diaryAnswer, user, diaryAnswer.getCreateDate());
		timeline4.setId(4L);
		given(this.timelineService.getOneTimeline(4L))
		.willReturn(timeline4);
		
		this.mvc.perform(get("/dashboard/timeline/{id}", 4L))
		.andExpect(redirectedUrl("/diary/" + timeline4.getDiary().getId() + "/detail"));
		
		Good good = new Good(diary, user);
				
		Timeline timeline5 = new Timeline("Good", "", diary, good, user, good.getSaveDate());
		timeline4.setId(5L);
		given(this.timelineService.getOneTimeline(5L))
		.willReturn(timeline5);
		
		this.mvc.perform(get("/dashboard/timeline/{id}", 5L))
		.andExpect(redirectedUrl("/diary/" + timeline5.getDiary().getId() + "/detail"));
	}

}
