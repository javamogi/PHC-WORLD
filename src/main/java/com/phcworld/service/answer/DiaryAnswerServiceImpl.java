package com.phcworld.service.answer;

import java.util.List;

import com.phcworld.exception.model.ErrorCode;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.exception.model.NotMatchUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.api.model.request.DiaryAnswerRequest;
import com.phcworld.domain.api.model.response.DiaryAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.exception.MatchNotUserException;
import com.phcworld.domain.user.User;
import com.phcworld.ifs.CrudInterface;
import com.phcworld.repository.answer.DiaryAnswerRepository;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.service.alert.AlertServiceImpl;
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
	public DiaryAnswerApiResponse create(User loginUser, Long diaryId, DiaryAnswerRequest request) {
		Diary diary = diaryRepository.findById(diaryId)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.writer(loginUser)
				.diary(diary)
				.contents(request.getContents().replace("\r\n", "<br>"))
				.build();
		
		DiaryAnswer createdDiaryAnswer = diaryAnswerRepository.save(diaryAnswer);
		DiaryAnswerApiResponse diaryAnswerApiResponse = DiaryAnswerApiResponse.of(createdDiaryAnswer);
		
		timelineService.createTimeline(createdDiaryAnswer);
		
		if(!diary.matchUser(loginUser)) {
			alertService.createAlert(createdDiaryAnswer);
		}
		
		return diaryAnswerApiResponse;
	}
	
	@Override
	public DiaryAnswerApiResponse read(Long id, User loginUser) {
		DiaryAnswer diaryAnswer = diaryAnswerRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
		if(!diaryAnswer.isSameWriter(loginUser)) {
			throw new NotMatchUserException();
		}
		return DiaryAnswerApiResponse.of(diaryAnswer);
	}
	
	@Override
	public DiaryAnswerApiResponse update(DiaryAnswerRequest request, User loginUser) {
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
	public SuccessResponse delete(Long id, User loginUser) {
		DiaryAnswer diaryAnswer = diaryAnswerRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
		if(!diaryAnswer.isSameWriter(loginUser)) {
			throw new NotMatchUserException();
		}
		
		timelineService.deleteTimeline(diaryAnswer);
		if(diaryAnswer.isSameWriter(loginUser)) {
			alertService.deleteAlert(diaryAnswer);
		}
		
		diaryAnswerRepository.deleteById(id);
		Diary diary = diaryRepository.findById(diaryAnswer.getDiary().getId())
				.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
		diary.getDiaryAnswers().remove(diaryAnswer);
		
		return SuccessResponse.builder()
				.success(diary.getCountOfAnswer())
				.build();
	}
	
	public List<DiaryAnswer> findDiaryAnswerListByWriter(User loginUser) {
		return diaryAnswerRepository.findByWriter(loginUser);
	}

}
