package com.phcworld.service.dashboard;

import java.util.List;

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
	private final FreeBoardServiceImpl freeBoardService;

	private final FreeBoardAnswerServiceImpl freeBoardAnswerService;
	
	private final DiaryServiceImpl diaryService;
	
	private final DiaryAnswerServiceImpl diaryAnswerService;
	
	private final TimelineServiceImpl timelineService;
	
	private final AlertServiceImpl alertService;
	
	public DashBoardUser getDashBoardUser(User user) {
		List<FreeBoard> freeBoardList = freeBoardService.findFreeBoardListByWriter(user);
		List<FreeBoardAnswer> freeBoardAnswerList = freeBoardAnswerService.findFreeBoardAnswerListByWriter(user);

		List<Diary> diaryList = diaryService.findDiaryListByWriter(user);
		List<DiaryAnswer> diaryAnswerList = diaryAnswerService.findDiaryAnswerListByWriter(user);
		
		List<Alert> alertList = alertService.findListAlertByPostUser(user);
		
		List<Timeline> timelineList = timelineService.findTimelineList(0, user);
		
		Integer countOfAnswers = freeBoardAnswerList.size() + diaryAnswerList.size();

		DashBoardUser dashboardUser = DashBoardUser.builder()
				.user(user)
				.countOfAnswer(countOfAnswers)
				.countOfFreeBoard(freeBoardList.size())
				.countOfDiary(diaryList.size())
				.countOfAlert(alertList.size())
				.timelineList(timelineList)
				.build();
		return dashboardUser;
	}

}
