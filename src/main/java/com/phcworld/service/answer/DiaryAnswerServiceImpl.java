package com.phcworld.service.answer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.alert.AlertRepository;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.timeline.TimelineRepository;
import com.phcworld.domain.user.User;
import com.phcworld.repository.answer.DiaryAnswerRepository;
import com.phcworld.repository.board.DiaryRepository;

@Service
@Transactional
public class DiaryAnswerServiceImpl implements DiaryAnswerService {
	
	@Autowired
	private DiaryRepository diaryRepository;

	@Autowired
	private DiaryAnswerRepository diaryAnswerRepository;
	
	@Autowired
	private TimelineRepository timelineRepository;
	
	@Autowired
	private AlertRepository alertRepository;
	
	@Override
	public DiaryAnswer createDiaryAnswer(User user, Diary diary, String contents) {
		DiaryAnswer diaryAnswer = new DiaryAnswer(user, diary, contents.replace("\r\n", "<br>"));
		diaryAnswerRepository.save(diaryAnswer);
		
		Timeline timeline = new Timeline("Diary Answer", "comment", diary, diaryAnswer, user, diaryAnswer.getCreateDate());
		timelineRepository.save(timeline);
		
		diaryAnswer.setTimeline(timeline);
		
		Alert alert = new Alert("Diary", diaryAnswer, diary.getWriter(), diaryAnswer.getCreateDate());
		alertRepository.save(alert);
		
		diaryAnswer.setAlert(alert);
		return diaryAnswerRepository.save(diaryAnswer);
	}
	
	@Override
	public String deleteDiaryAnswer(Long id, User loginUser, Long diaryId) {
		DiaryAnswer diaryAnswer = diaryAnswerRepository.getOne(id);
		if(!diaryAnswer.isSameWriter(loginUser)) {
			throw new MatchNotUserExceptioin("본인이 작성한 글만 삭제 가능합니다.");
		}
		diaryAnswerRepository.deleteById(id);
		Diary diary = diaryRepository.getOne(diaryId);
		diary.deleteAnswer();
		diary = diaryRepository.save(diary);
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
