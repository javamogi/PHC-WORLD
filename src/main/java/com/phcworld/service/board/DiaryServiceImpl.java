package com.phcworld.service.board;

import java.util.List;
import java.util.stream.Collectors;

import com.phcworld.domain.board.DiaryResponseDto;
import com.phcworld.service.user.UserService;
import com.phcworld.utils.HttpSessionUtils;
import com.phcworld.utils.PageNationsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.api.model.response.DiaryAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryRequest;
import com.phcworld.domain.board.DiaryResponse;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.good.GoodService;
import com.phcworld.service.timeline.TimelineServiceImpl;


@Service
@Transactional
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {
	
	private final DiaryRepository diaryRepository;
	
	private final AlertServiceImpl alertService;
	
	private final TimelineServiceImpl timelineService;
	
	private final GoodService goodService;

	private final UserService userService;
	
	public List<DiaryResponse> getDiaryResponseList(List<Diary> diaries) {
		List<DiaryResponse> diaryResponseList = diaries.stream()
				.map(DiaryResponse::of)
				.collect(Collectors.toList());
		return diaryResponseList;
	}

	@Transactional(readOnly = true)
	public DiaryResponseDto getDiaryResponseListTemp(User loginUser, String email, int pageNum) {
		User requestUser = userService.findUserByEmail(email);

		Page<Diary> diaryPage = findPageDiary(loginUser, pageNum, requestUser);

		List<DiaryResponse> diaryResponseList = diaryPage.getContent().stream()
				.map(DiaryResponse::of)
				.collect(Collectors.toList());
		DiaryResponseDto diaryResponseDto = DiaryResponseDto.builder()
				.totalPages(diaryPage.getTotalPages())
				.diaries(diaryResponseList)
				.build();
		return diaryResponseDto;
	}
	
	@Override
	public Page<Diary> findPageDiary(User loginUser, Integer pageNum, User requestUser) {
//		PageRequest pageRequest = PageRequest.of(pageNum - 1, 6, new Sort(Direction.DESC, "id"));
		PageRequest pageRequest = PageRequest.of(pageNum - 1, 6, Sort.by("id").descending());
		if(isLoginUser(loginUser, requestUser)) {
			return diaryRepository.findByWriter(requestUser, pageRequest);
		}
		return diaryRepository.findByWriter(loginUser, pageRequest);
	}

	private boolean isLoginUser(User loginUser, User requestUser) {
		return loginUser == null || !requestUser.matchId(loginUser.getId());
	}

	@Override
	public DiaryResponse createDiary(User user, DiaryRequest request) {
		Diary diary = Diary.builder()
				.writer(user)
				.title(request.getTitle())
				.contents(request.getContents())
				.thumbnail(request.getThumbnail())
				.build();
		Diary createdDiary = diaryRepository.save(diary);
		
		timelineService.createTimeline(createdDiary);
		
		return response(diary);
	}

	@Override
	public DiaryResponse getOneDiary(Long id) {
		Diary diary = diaryRepository.getOne(id);
		return response(diary);
	}

	@Override
	public DiaryResponse updateDiary(DiaryRequest request) {
		Diary diary = diaryRepository.getOne(request.getId());
		diary.update(request);
		return response(diaryRepository.save(diary));
	}

	@Override
	public void deleteDiary(Long id) {
		Diary diary = diaryRepository.getOne(id);
		timelineService.deleteTimeline(diary);
		List<Good> goodList = diary.getGoodPushedUser();
		for(int i = 0; i < goodList.size(); i++) {
			timelineService.deleteTimeline(goodList.get(i));
			alertService.deleteAlert(goodList.get(i));
		}
		List<DiaryAnswer> answerList = diary.getDiaryAnswers();
		for(int i = 0; i < answerList.size(); i++) {
			DiaryAnswer diaryAnswer = answerList.get(i);
			timelineService.deleteTimeline(diaryAnswer);
			alertService.deleteAlert(diaryAnswer);
		}
		diaryRepository.delete(diary);
	}
	
	@Override
	public List<Diary> findDiaryListByWriter(User loginUser) {
		return diaryRepository.findByWriter(loginUser);
	}
	
	public SuccessResponse updateGood(Long diaryId, User loginUser) {
		Diary diary = diaryRepository.getOne(diaryId);
		
		Diary updatedGoodCount = diaryRepository.save(goodService.pushGood(diary, loginUser));
		
		return SuccessResponse.builder()
				.success(Integer.toString(updatedGoodCount.getCountOfGood()))
				.build();
	}
	
	private DiaryResponse response(Diary diary) {
		DiaryResponse diaryResponse = DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.countOfAnswers(diary.getCountOfAnswer())
				.countOfGood(diary.getCountOfGood())
				.updateDate(diary.getFormattedUpdateDate())
				.build();
		List<DiaryAnswer> answerList = diary.getDiaryAnswers();
		if(answerList != null) {
			List<DiaryAnswerApiResponse> diaryAnswerApiResponseList = answerList.stream()
					.map(answer -> {
						DiaryAnswerApiResponse diaryAnswerApiResponse = DiaryAnswerApiResponse.builder()
								.id(answer.getId())
								.writer(answer.getWriter())
								.contents(answer.getContents())
								.diaryId(answer.getDiary().getId())
								.countOfAnswers(answer.getDiary().getCountOfAnswer())
								.updateDate(answer.getFormattedUpdateDate())
								.build();
						return diaryAnswerApiResponse;
					})
					.collect(Collectors.toList());
			diaryResponse.setDiaryAnswerList(diaryAnswerApiResponseList);
		}
		return diaryResponse;
	}
	
}
