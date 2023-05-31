package com.phcworld.web.dashboard;

import javax.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.DashBoardUser;
import com.phcworld.domain.user.User;
import com.phcworld.service.dashboard.DashboardService;
import com.phcworld.service.timeline.TimelineServiceImpl;
import com.phcworld.utils.HttpSessionUtils;


@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
	
	private final TimelineServiceImpl timelineService;
	
	private final DashboardService dashboardService;
	
//	@Autowired
//	private TempTimelineService tempTimelineService;
	
	@GetMapping("")
	public String requestDashboard(HttpSession session, Model model) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			model.addAttribute("errorMessage", "로그인이 필요합니다.");
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		DashBoardUser dashBoardUser = dashboardService.getDashBoardUser(loginUser);
		
//		List<TempTimeline> tempTimelineList = tempTimelineService.getTimeline(loginUser);
//		model.addAttribute("timelines", tempTimelineList);

		model.addAttribute("dashboard", dashBoardUser);
		return "/dashboard/dashboard";
	}
	
	@GetMapping("/timeline/{id}")
	public String redirectToTimeline(@PathVariable Long id) {
		Timeline timeline = timelineService.getOneTimeline(id);
		return timeline.redirectUrl();
	}
	
}
