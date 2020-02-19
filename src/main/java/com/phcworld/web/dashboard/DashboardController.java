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
import com.phcworld.domain.alert.AlertServiceImpl;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.DiaryAnswerServiceImpl;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.answer.FreeBoardAnswerServiceImpl;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.timeline.TimelineServiceImpl;
import com.phcworld.domain.user.User;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.service.board.FreeBoardServiceImpl;
import com.phcworld.web.HttpSessionUtils;

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
	
	@GetMapping("")
	public String dashboard(HttpSession session, Model model) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			model.addAttribute("errorMessage", "로그인이 필요합니다.");
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		
		List<FreeBoard> freeBoards = freeBoardService.findFreeBoardListByWriter(loginUser);
		List<FreeBoardAnswer> freeBoardAnswer = freeBoardAnswerService.findFreeBoardAnswerListByWriter(loginUser);

		List<Diary> diaries = diaryService.findDiaryListByWriter(loginUser);
		List<DiaryAnswer> diaryAnswer = diaryAnswerService.findDiaryAnswerListByWriter(loginUser);
		
		List<Alert> alerts = alertService.findPageRequestAlertByUser(loginUser);
		
		List<Timeline> timelines = timelineService.findPageTimelineByUser(loginUser).getContent();

		model.addAttribute("countAnswers", freeBoardAnswer.size() + diaryAnswer.size());
		model.addAttribute("countFreeboards", freeBoards.size());
		model.addAttribute("countDiaries", diaries.size());
		model.addAttribute("countAlerts", alerts.size());
		
		model.addAttribute("timelines", timelines);

		return "/dashboard/dashboard";
	}
	
	@GetMapping("/timeline/{id}")
	public String redirectToTimeline(@PathVariable Long id) {
		Timeline timeline = timelineService.getOneTimeline(id);
		if(timeline.getFreeBoard() == null) {
			return "redirect:/diary/"+ timeline.getDiary().getId() + "/detail";
		}
		return "redirect:/freeboard/"+ timeline.getFreeBoard().getId() + "/detail";
	}
	
}
