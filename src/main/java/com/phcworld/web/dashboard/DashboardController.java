package com.phcworld.web.dashboard;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.answer.DiaryAnswerServiceImpl;
import com.phcworld.service.answer.FreeBoardAnswerServiceImpl;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.service.board.FreeBoardServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;
import com.phcworld.utils.HttpSessionUtils;


@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	
	@Autowired
	private FreeBoardServiceImpl freeBoardService;

	@Autowired
	private FreeBoardAnswerServiceImpl freeBoardAnswerService;
	
	@Autowired
	private DiaryServiceImpl diaryService;
	
	@Autowired
	private DiaryAnswerServiceImpl diaryAnswerService;
	
	@Autowired
	private TimelineServiceImpl timelineService;
	
	@Autowired
	private AlertServiceImpl alertService;
	
//	@Autowired
//	private TempTimelineService tempTimelineService;
	
	@GetMapping("")
	public String requestDashboard(HttpSession session, Model model) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			model.addAttribute("errorMessage", "로그인이 필요합니다.");
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		
		List<FreeBoard> freeBoardList = freeBoardService.findFreeBoardListByWriter(loginUser);
		List<FreeBoardAnswer> freeBoardAnswerList = freeBoardAnswerService.findFreeBoardAnswerListByWriter(loginUser);

		List<Diary> diaryList = diaryService.findDiaryListByWriter(loginUser);
		List<DiaryAnswer> diaryAnswerList = diaryAnswerService.findDiaryAnswerListByWriter(loginUser);
		
		List<Alert> alertList = alertService.findListAlertByPostUser(loginUser);
		
		List<Timeline> timelineList = timelineService.findTimelineList(0, loginUser);
		
		Integer countAnswers = freeBoardAnswerList.size() + diaryAnswerList.size();

		model.addAttribute("countAnswers", countAnswers);
		model.addAttribute("countFreeboards", freeBoardList.size());
		model.addAttribute("countDiaries", diaryList.size());
		model.addAttribute("countAlerts", alertList.size());
		
		model.addAttribute("timelines", timelineList);

//		List<TempTimeline> tempTimelineList = tempTimelineService.getTimeline(loginUser);
//		model.addAttribute("timelines", tempTimelineList);
		
		return "/dashboard/dashboard";
	}
	
	@GetMapping("/timeline/{id}")
	public String redirectToTimeline(@PathVariable Long id) {
		Timeline timeline = timelineService.getOneTimeline(id);
		return timeline.redirectUrl();
	}
	
}
