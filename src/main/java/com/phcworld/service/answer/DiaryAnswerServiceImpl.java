package com.phcworld.service.answer;

import java.util.List;

import com.phcworld.domain.common.SaveType;
import com.phcworld.exception.model.ErrorCode;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.exception.model.NotMatchUserException;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.api.model.request.DiaryAnswerRequest;
import com.phcworld.domain.api.model.response.DiaryAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.Diary;
import com.phcworld.ifs.CrudInterface;
import com.phcworld.repository.answer.DiaryAnswerRepository;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.alert.service.AlertServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryAnswerServiceImpl implements CrudInterface<DiaryAnswerRequest, DiaryAnswerApiResponse, SuccessResponse> {
	
	private final DiaryRepository diaryRepository;

	private final DiaryAnswerRepository diaryAnswerRepository;
	
	private final AlertServiceImpl alertService;
	
	private final TimelineServiceImpl timelineService;
	
	@Override
	public DiaryAnswerApiResponse create(UserEntity loginUser, Long diaryId, DiaryAnswerRequest request) {
		Diary diary = diaryRepository.findById(diaryId)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.writer(loginUser)
				.diary(diary)
				.contents(request.getContents().replace("\r\n", "<br>"))
				.build();
		
		DiaryAnswer createdDiaryAnswer = diaryAnswerRepository.save(diaryAnswer);

		timelineService.createTimeline(createdDiaryAnswer);
		
		if(!diary.matchUser(loginUser)) {
			alertService.createAlert(createdDiaryAnswer);
		}
		
		return DiaryAnswerApiResponse.of(createdDiaryAnswer);
	}
	
	@Override
	public DiaryAnswerApiResponse read(Long id, UserEntity loginUser) {
		DiaryAnswer diaryAnswer = diaryAnswerRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
		if(!diaryAnswer.isSameWriter(loginUser)) {
			throw new NotMatchUserException();
		}
		return DiaryAnswerApiResponse.of(diaryAnswer);
	}
	
	@Override
	public DiaryAnswerApiResponse update(DiaryAnswerRequest request, UserEntity loginUser) {
		DiaryAnswer answer = diaryAnswerRepository.findById(request.getId())
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
		if(!answer.isSameWriter(loginUser)) {
			throw new NotMatchUserException();
		}
		answer.update(request.getContents().replace("\r\n", "<br>"));
		
		DiaryAnswer updatedDiaryAnswer = diaryAnswerRepository.save(answer);

		return DiaryAnswerApiResponse.of(updatedDiaryAnswer);
	}
	
	@Override
	public SuccessResponse delete(Long id, UserEntity loginUser) {
		DiaryAnswer diaryAnswer = diaryAnswerRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
		if(!diaryAnswer.isSameWriter(loginUser)) {
			throw new NotMatchUserException();
		}
		
		timelineService.deleteTimeline(SaveType.DIARY_ANSWER, id);
		alertService.deleteAlert(SaveType.DIARY_ANSWER, id);
		
		diaryAnswerRepository.deleteById(id);

		return SuccessResponse.builder()
				.success("삭제성공")
				.build();
	}
	
	public List<DiaryAnswer> findDiaryAnswerListByWriter(UserEntity loginUser) {
		return diaryAnswerRepository.findByWriter(loginUser);
	}

}
