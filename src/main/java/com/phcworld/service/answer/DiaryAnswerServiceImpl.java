package com.phcworld.service.answer;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.user.User;
import com.phcworld.repository.alert.AlertRepository;
import com.phcworld.repository.answer.DiaryAnswerRepository;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
@Transactional
public class DiaryAnswerServiceImpl implements DiaryAnswerService {
	
	@Autowired
	private DiaryRepository diaryRepository;

	@Autowired
	private DiaryAnswerRepository diaryAnswerRepository;
	
	@Autowired
	private AlertRepository alertRepository;
	
	@Autowired
	private TimelineServiceImpl timelineService;
	
	@Override
	public DiaryAnswer createDiaryAnswer(User user, Diary diary, String contents) {
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.writer(user)
				.diary(diary)
				.contents(contents.replace("\r\n", "<br>"))
				.createDate(LocalDateTime.now())
				.build();
		
		DiaryAnswer createdDiaryAnswer = diaryAnswerRepository.save(diaryAnswer);
		timelineService.createTimeline(createdDiaryAnswer);
		
		if(!diary.matchUser(user)) {
//			Alert alert = new Alert("Diary", diaryAnswer, diary.getWriter(), diaryAnswer.getCreateDate());
			Alert alert = Alert.builder()
					.type("Diary")
					.diaryAnswer(diaryAnswer)
					.postWriter(diary.getWriter())
					.createDate(LocalDateTime.now())
					.build();
			alertRepository.save(alert);
		}
		
		return createdDiaryAnswer;
	}
	
	@Override
	public String deleteDiaryAnswer(Long id, User loginUser, Long diaryId) {
		DiaryAnswer diaryAnswer = diaryAnswerRepository.getOne(id);
		if(!diaryAnswer.isSameWriter(loginUser)) {
			throw new MatchNotUserExceptioin("본인이 작성한 글만 삭제 가능합니다.");
		}
		
		timelineService.deleteTimeline(diaryAnswer);
		Alert alert = alertRepository.findByDiaryAnswer(diaryAnswer);
		alertRepository.delete(alert);
		
		diaryAnswerRepository.deleteById(id);
		Diary diary = diaryRepository.getOne(diaryId);
		diary.getDiaryAnswers().remove(diaryAnswer);
		return "{\"success\":\"" + diary.getCountOfAnswer() +"\"}";
	}
	
	@Override
	public List<DiaryAnswer> findDiaryAnswerListByWriter(User loginUser) {
		return diaryAnswerRepository.findByWriter(loginUser);
	}

	public DiaryAnswer getOneDiaryAnswer(Long id) {
		return diaryAnswerRepository.getOne(id);
	}
}
