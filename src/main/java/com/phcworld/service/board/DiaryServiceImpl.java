package com.phcworld.service.board;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;
import com.phcworld.repository.answer.DiaryAnswerRepository;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class DiaryServiceImpl implements DiaryService {
	
	@Autowired
	private DiaryRepository diaryRepository;
	
	@Autowired
	private AlertServiceImpl alertService;
	
	@Autowired
	private TimelineServiceImpl timelineService;
	
	@Autowired
	private DiaryAnswerRepository diaryAnswerRepository;
	
	@Override
	public Page<Diary> findPageDiary(User loginUser, Integer pageNum, User requestUser) {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, 6, new Sort(Direction.DESC, "id"));
		if(isWriter(loginUser, requestUser)) {
			return diaryRepository.findByWriter(requestUser, pageRequest);
		}
		return diaryRepository.findByWriter(loginUser, pageRequest);
	}

	private boolean isWriter(User loginUser, User requestUser) {
		return loginUser == null || !requestUser.matchId(loginUser.getId());
	}

	@Override
	public Diary createDiary(User user, String title, String contents, String thumbnail) {
		Diary diary = Diary.builder()
				.writer(user)
				.title(title)
				.contents(contents)
				.thumbnail(thumbnail)
				.createDate(LocalDateTime.now())
				.build();
		Diary createdDiary = diaryRepository.save(diary);

		timelineService.createTimeline(createdDiary);

		return createdDiary;
	}

	@Override
	public Diary getOneDiary(Long id) {
		return diaryRepository.getOne(id);
	}

	@Override
	public Diary updateDiary(Diary diary, String contents, String thumbnail) {
		diary.update(contents, thumbnail);
		return diaryRepository.save(diary);
	}

	@Override
	public void deleteDiary(Diary diary) {
		timelineService.deleteTimeline("diary", diary);
		List<DiaryAnswer> answerList = diary.getDiaryAnswers();
		for(int i = 0; i < answerList.size(); i++) {
			DiaryAnswer diaryAnswer = answerList.get(i);
			timelineService.deleteTimeline(diaryAnswer);
			alertService.deleteAlert(diaryAnswer);
			diaryAnswerRepository.delete(diaryAnswer);
		}
		diaryRepository.delete(diary);
	}
	
	@Override
	public List<Diary> findDiaryListByWriter(User loginUser) {
		return diaryRepository.findByWriter(loginUser);
	}
	
	public String updateGood(Long diaryId, User loginUser) {
		Diary diary = diaryRepository.getOne(diaryId);
//		boolean isRemove = diary.pushGoodUser(loginUser); 
		boolean isRemove = false; 
		Set<User> set = diary.getGoodPushedUser();
		if(set.contains(loginUser)) {
			set.remove(loginUser);
			isRemove = true;
		} else {
			set.add(loginUser);
		}
		
		Diary updatedGoodCount = diaryRepository.save(diary);
		
		if(isRemove) {
			timelineService.deleteTimeline("good", updatedGoodCount, loginUser);
			if(!diary.matchUser(loginUser)) {
				alertService.deleteAlert(updatedGoodCount, loginUser);
			}
		} else {
			timelineService.createTimeline("good", updatedGoodCount, loginUser);
			if(!diary.matchUser(loginUser)) {
				alertService.createAlert(updatedGoodCount, loginUser);
			}
		}
		
		return "{\"success\":\"" + Integer.toString(diary.getCountOfGood()) +"\"}";
	}
}
