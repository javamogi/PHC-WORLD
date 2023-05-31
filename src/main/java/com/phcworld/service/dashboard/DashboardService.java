package com.phcworld.service.dashboard;

import java.util.List;

import com.phcworld.domain.dashboard.DashBoardSelectDto;
import com.phcworld.repository.dashboard.DashBoardRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.DashBoardUser;
import com.phcworld.domain.user.User;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.answer.DiaryAnswerServiceImpl;
import com.phcworld.service.answer.FreeBoardAnswerServiceImpl;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.service.board.FreeBoardServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
@RequiredArgsConstructor
public class DashboardService {
	private final TimelineServiceImpl timelineService;
	
	private final DashBoardRepositoryCustom dashBoardRepository;
	
	public DashBoardUser getDashBoardUser(User user) {
		DashBoardSelectDto count = dashBoardRepository.findActiveCountByUser(user);

		List<Timeline> timelineList = timelineService.findTimelineList(0, user);
		
		DashBoardUser dashboardUser = DashBoardUser.builder()
				.user(user)
				.countOfAnswer(count.getAnswerCount())
				.countOfFreeBoard(count.getFreeBoardCount())
				.countOfDiary(count.getDiaryCount())
				.countOfAlert(count.getAlertCount())
				.timelineList(timelineList)
				.build();
		return dashboardUser;
	}

}
