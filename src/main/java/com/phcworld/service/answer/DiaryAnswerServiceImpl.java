package com.phcworld.service.answer;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.api.model.response.DiaryAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.user.User;
import com.phcworld.ifs.CrudInterface;
import com.phcworld.repository.answer.DiaryAnswerRepository;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
@Transactional
public class DiaryAnswerServiceImpl implements CrudInterface<DiaryAnswerApiResponse, SuccessResponse> {
	
	@Autowired
	private DiaryRepository diaryRepository;

	@Autowired
	private DiaryAnswerRepository diaryAnswerRepository;
	
	@Autowired
	private AlertServiceImpl alertService;
	
	@Autowired
	private TimelineServiceImpl timelineService;
	
	@Override
	public DiaryAnswerApiResponse create(User loginUser, Long diaryId, String contents) {
		Diary diary = diaryRepository.getOne(diaryId);
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.writer(loginUser)
				.diary(diary)
				.contents(contents.replace("\r\n", "<br>"))
				.build();
		
		DiaryAnswer createdDiaryAnswer = diaryAnswerRepository.save(diaryAnswer);
		DiaryAnswerApiResponse diaryAnswerApiResponse = DiaryAnswerApiResponse.builder()
				.id(createdDiaryAnswer.getId())
				.writer(createdDiaryAnswer.getWriter())
				.contents(createdDiaryAnswer.getContents())
				.diaryId(createdDiaryAnswer.getDiary().getId())
				.countOfAnswers(createdDiaryAnswer.getDiary().getCountOfAnswer())
				.updateDate(createdDiaryAnswer.getFormattedUpdateDate())
				.build();
		
		timelineService.createTimeline(createdDiaryAnswer);
		
		if(!diary.matchUser(loginUser)) {
			alertService.createAlert(createdDiaryAnswer);
		}
		
		return diaryAnswerApiResponse;
	}
	
	@Override
	public DiaryAnswerApiResponse read(Long id, User loginUser) {
		DiaryAnswer diaryAnswer = diaryAnswerRepository.getOne(id);
		if(!diaryAnswer.isSameWriter(loginUser)) {
			throw new MatchNotUserExceptioin("본인이 작성한 글만 수정 가능합니다.");
		}
		DiaryAnswerApiResponse diaryAnswerApiResponse = DiaryAnswerApiResponse.builder()
				.id(diaryAnswer.getId())
				.writer(diaryAnswer.getWriter())
				.contents(diaryAnswer.getContents())
				.diaryId(diaryAnswer.getDiary().getId())
				.countOfAnswers(diaryAnswer.getDiary().getCountOfAnswer())
				.updateDate(diaryAnswer.getFormattedUpdateDate())
				.build();
		return diaryAnswerApiResponse;
	}
	
	@Override
	public DiaryAnswerApiResponse update(Long id, String contents, User loginUser) {
		DiaryAnswer answer = diaryAnswerRepository.getOne(id);
		if(!answer.isSameWriter(loginUser)) {
			throw new MatchNotUserExceptioin("본인이 작성한 글만 수정 가능합니다.");
		}
		answer.update(contents.replace("\r\n", "<br>"));
		
		DiaryAnswer updatedDiaryAnswer = diaryAnswerRepository.save(answer);
		DiaryAnswerApiResponse diaryAnswerApiResponse = DiaryAnswerApiResponse.builder()
				.id(updatedDiaryAnswer.getId())
				.writer(updatedDiaryAnswer.getWriter())
				.contents(updatedDiaryAnswer.getContents())
				.diaryId(updatedDiaryAnswer.getDiary().getId())
				.countOfAnswers(updatedDiaryAnswer.getDiary().getCountOfAnswer())
				.updateDate(updatedDiaryAnswer.getFormattedUpdateDate())
				.build();
		
		return diaryAnswerApiResponse;
	}
	
	@Override
	public SuccessResponse delete(Long id, User loginUser) {
		DiaryAnswer diaryAnswer = diaryAnswerRepository.getOne(id);
		if(!diaryAnswer.isSameWriter(loginUser)) {
			throw new MatchNotUserExceptioin("본인이 작성한 글만 삭제 가능합니다.");
		}
		
		timelineService.deleteTimeline(diaryAnswer);
		if(diaryAnswer.isSameWriter(loginUser)) {
			alertService.deleteAlert(diaryAnswer);
		}
		
		diaryAnswerRepository.deleteById(id);
		Diary diary = diaryRepository.getOne(diaryAnswer.getDiary().getId());
		diary.getDiaryAnswers().remove(diaryAnswer);
		
		return SuccessResponse.builder()
				.success(diary.getCountOfAnswer())
				.build();
	}
	
	public List<DiaryAnswer> findDiaryAnswerListByWriter(User loginUser) {
		return diaryAnswerRepository.findByWriter(loginUser);
	}

}
