package com.phcworld.web.dashboard;

import javax.servlet.http.HttpSession;

import com.phcworld.domain.alert.dto.AlertResponseDto;
import com.phcworld.domain.message.MessageResponse;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.message.MessageService;
import com.phcworld.service.message.MessageServiceImpl;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.phcworld.domain.timeline.Timeline;
import com.phcworld.user.controller.port.DashBoardUser;
import com.phcworld.service.dashboard.DashboardService;
import com.phcworld.service.timeline.TimelineServiceImpl;
import com.phcworld.utils.HttpSessionUtils;

import java.util.List;


@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
	
	private final TimelineServiceImpl timelineService;
	
	private final DashboardService dashboardService;

	private final MessageServiceImpl messageService;

	private final AlertServiceImpl alertService;
	
//	@Autowired
//	private TempTimelineService tempTimelineService;
	
	@GetMapping("")
	public String requestDashboard(HttpSession session, Model model) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			model.addAttribute("errorMessage", "로그인이 필요합니다.");
			return "user/login";
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		DashBoardUser dashBoardUser = dashboardService.getDashBoardUser(loginUser);

		//		List<Message> allMessages = messageService.findMessageAllBySenderAndNotConfirmUseProfile(user, "읽지 않음");
		List<MessageResponse> allMessages = messageService.findMessageAllBySenderAndNotConfirmUseProfile(loginUser, "읽지 않음");

		List<MessageResponse> messages = messageService.findMessageBySenderAndConfirmUseMenu(loginUser, "읽지 않음");
//		List<Message> messages = messageService.findMessageBySenderAndConfirmUseMenu(user, "읽지 않음");
		String countOfMessages = Integer.toString(allMessages.size());
		if(allMessages.size() == 0) {
			countOfMessages = "";
		}
		session.setAttribute("messages", messages);
		session.setAttribute("countMessages", countOfMessages);

//		List<Alert> alerts = alertService.findListAlertByPostUser(user);
		List<AlertResponseDto> alerts = alertService.findByAlertListByPostUser(loginUser);
		session.setAttribute("alerts", alerts);

//		List<TempTimeline> tempTimelineList = tempTimelineService.getTimeline(loginUser);
//		model.addAttribute("timelines", tempTimelineList);

		model.addAttribute("dashboard", dashBoardUser);
		return "dashboard/dashboard";
	}
	
	@GetMapping("/timeline/{id}")
	public String redirectToTimeline(@PathVariable Long id) {
		Timeline timeline = timelineService.getOneTimeline(id);
		return timeline.redirectUrl();
	}
	
}
