package com.phcworld.service.dashboard;

import java.util.List;
import java.util.stream.Collectors;

import com.phcworld.api.dashboard.dto.UserResponseDto;
import com.phcworld.domain.dashboard.dto.DashBoardSelectDto;
import com.phcworld.domain.timeline.dto.TimelineResponseDto;
import com.phcworld.repository.dashboard.DashBoardRepositoryCustom;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.phcworld.user.controller.port.DashBoardUser;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
@RequiredArgsConstructor
public class DashboardService {
	private final TimelineServiceImpl timelineService;
	
	private final DashBoardRepositoryCustom dashBoardRepository;

//	private final FreeBoardServiceImpl freeBoardService;
//
//	private final FreeBoardAnswerServiceImpl freeBoardAnswerService;
//
//	private final DiaryServiceImpl diaryService;
//
//	private final DiaryAnswerServiceImpl diaryAnswerService;
//
//	private final AlertServiceImpl alertService;

	public DashBoardUser getDashBoardUser(UserEntity user) {
		DashBoardSelectDto count = dashBoardRepository.findActiveCountByUser(user);

//		List<Timeline> timelineList = timelineService.findTimelineList(0, user);
		List<TimelineResponseDto> timelineList = timelineService.findTimelineList(1, user)
				.stream()
				.map(TimelineResponseDto::of)
				.collect(Collectors.toList());

//		List<FreeBoard> freeBoardList = freeBoardService.findFreeBoardListByWriter(user);
//		List<FreeBoardAnswer> freeBoardAnswerList = freeBoardAnswerService.findFreeBoardAnswerListByWriter(user);
//
//		List<Diary> diaryList = diaryService.findDiaryListByWriter(user);
//		List<DiaryAnswer> diaryAnswerList = diaryAnswerService.findDiaryAnswerListByWriter(user);
//
//		List<Alert> alertList = alertService.findListAlertByPostUser(user);
//
//		List<Timeline> timelineList = timelineService.findTimelineList(0, user);
//
//		Integer countOfAnswers = freeBoardAnswerList.size() + diaryAnswerList.size();
		
		DashBoardUser dashboardUser = DashBoardUser.builder()
				.user(UserResponseDto.of(user))
				.countOfAnswer(count.getAnswerCount())
				.countOfFreeBoard(count.getFreeBoardCount())
				.countOfDiary(count.getDiaryCount())
				.countOfAlert(count.getAlertCount())
//				.countOfAnswer(countOfAnswers.longValue())
//				.countOfFreeBoard(Long.valueOf(freeBoardList.size()))
//				.countOfDiary(Long.valueOf(diaryList.size()))
//				.countOfAlert(Long.valueOf(alertList.size()))
				.timelineList(timelineList)
				.build();
		return dashboardUser;
	}

}
