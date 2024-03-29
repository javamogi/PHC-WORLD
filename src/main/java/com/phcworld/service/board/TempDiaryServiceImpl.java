package com.phcworld.service.board;

import java.util.List;
import java.util.stream.Collectors;

import com.phcworld.exception.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.TempDiaryAnswer;
import com.phcworld.domain.api.model.response.DiaryAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.TempDiary;
import com.phcworld.domain.board.dto.TempDiaryRequest;
import com.phcworld.domain.board.dto.TempDiaryResponse;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.repository.board.TempDiaryRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.good.TempGoodService;
import com.phcworld.service.timeline.TempTimelineServiceImpl;


@Service
@Transactional
@RequiredArgsConstructor
public class TempDiaryServiceImpl implements TempDiaryService {
	
	private final TempDiaryRepository diaryRepository;
	
	private final AlertServiceImpl alertService;
	
	private final TempGoodService goodService;
	
	private final TempTimelineServiceImpl timelineService;
	
	public List<TempDiaryResponse> getDiaryResponseList(List<TempDiary> diaries) {
		List<TempDiaryResponse> diaryResponseList = diaries.stream()
				.map(diary -> {
					TempDiaryResponse diaryResponse = TempDiaryResponse.builder()
							.id(diary.getId())
							.writer(diary.getWriter())
							.title(diary.getTitle())
							.contents(diary.getContents())
							.thumbnail(diary.getThumbnail())
							.countOfAnswers(diary.getCountOfAnswer())
							.countOfGood(diary.getCountOfGood())
							.updateDate(diary.getFormattedUpdateDate())
							.build();
					return diaryResponse;
				})
				.collect(Collectors.toList());
		return diaryResponseList;
	}
	
	@Override
	public Page<TempDiary> findPageDiary(UserEntity loginUser, Integer pageNum, UserEntity requestUser) {
//		PageRequest pageRequest = PageRequest.of(pageNum - 1, 6, new Sort(Direction.DESC, "id"));
		PageRequest pageRequest = PageRequest.of(pageNum - 1, 6, Sort.by("id").descending());
		if(isLoginUser(loginUser, requestUser)) {
			return diaryRepository.findByWriter(requestUser, pageRequest);
		}
		return diaryRepository.findByWriter(loginUser, pageRequest);
	}

	private boolean isLoginUser(UserEntity loginUser, UserEntity requestUser) {
		return loginUser == null || !requestUser.matchId(loginUser.getId());
	}

	@Override
	public TempDiaryResponse createDiary(UserEntity user, TempDiaryRequest request) {
		TempDiary diary = TempDiary.builder()
				.writer(user)
				.title(request.getTitle())
				.contents(request.getContents())
				.thumbnail(request.getThumbnail())
				.build();
		TempDiary createdDiary = diaryRepository.save(diary);
		
		timelineService.createTimeline("diary", createdDiary, createdDiary.getId());
		
		return response(diary);
	}

	@Override
	public TempDiaryResponse getOneDiary(Long id) {
		TempDiary diary = diaryRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		return response(diary);
	}

	@Override
	public TempDiaryResponse updateDiary(TempDiaryRequest request) {
		TempDiary diary = diaryRepository.findById(request.getId())
				.orElseThrow(NotFoundException::new);
		diary.update(request);
		return response(diaryRepository.save(diary));
	}

	@Override
	public void deleteDiary(Long id) {
//		TempDiary diary = diaryRepository.getOne(id);
//		timelineService.deleteTimeline(diary);
////		List<TempGood> goodList = diary.getTempGoodPushedUser();
////		for(int i = 0; i < goodList.size(); i++) {
////			timelineService.deleteTimeline(goodList.get(i));
////			alertService.deleteAlert(goodList.get(i));
////		}
//		List<TempDiaryAnswer> answerList = diary.getTempDiaryAnswers();
//		for(int i = 0; i < answerList.size(); i++) {
//			TempDiaryAnswer diaryAnswer = answerList.get(i);
//			timelineService.deleteTimeline(diaryAnswer);
////			alertService.deleteAlert(diaryAnswer);
//		}
//		diaryRepository.delete(diary);
	}
	
	@Override
	public List<TempDiary> findDiaryListByWriter(UserEntity loginUser) {
		return diaryRepository.findByWriter(loginUser);
	}
	
	public SuccessResponse updateGood(Long diaryId, UserEntity loginUser) {
		TempDiary diary = diaryRepository.findById(diaryId)
				.orElseThrow(NotFoundException::new);
		TempDiary updateDiary = diaryRepository.save(goodService.pushGood(diary, loginUser));
		return SuccessResponse.builder()
				.success(Integer.toString(updateDiary.getCountOfGood()))
				.build();
	}
	
	private TempDiaryResponse response(TempDiary diary) {
		TempDiaryResponse diaryResponse = TempDiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.countOfAnswers(diary.getCountOfAnswer())
				.countOfGood(diary.getCountOfGood())
				.updateDate(diary.getFormattedUpdateDate())
				.build();
		List<TempDiaryAnswer> answerList = diary.getTempDiaryAnswers();
		if(answerList != null) {
			List<DiaryAnswerApiResponse> diaryAnswerApiResponseList = answerList.stream()
					.map(answer -> {
						DiaryAnswerApiResponse diaryAnswerApiResponse = DiaryAnswerApiResponse.builder()
								.id(answer.getId())
								.writer(answer.getWriter())
								.contents(answer.getContents())
								.diaryId(answer.getTempDiary().getId())
								.countOfAnswers(answer.getTempDiary().getCountOfAnswer())
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
