package com.phcworld.service.board;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.repository.answer.DiaryAnswerRepository;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.good.GoodService;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
@Transactional
public class DiaryServiceImpl implements DiaryService {
	
	@Autowired
	private DiaryRepository diaryRepository;
	
	@Autowired
	private AlertServiceImpl alertService;
	
	@Autowired
	private TimelineServiceImpl timelineService;
	
	@Autowired
	private DiaryAnswerRepository diaryAnswerRepository;
	
	@Autowired
	private GoodService goodService;
	
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
		timelineService.deleteTimeline(diary);
		List<Good> goodList = diary.getGoodPushedUser();
		for(int i = 0; i < goodList.size(); i++) {
			timelineService.deleteTimeline(goodList.get(i));
			alertService.deleteAlert(goodList.get(i).getDiary(), goodList.get(i).getUser());
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
	
	public String updateGood(Long diaryId, User loginUser) {
		Diary diary = diaryRepository.getOne(diaryId);
		
		goodService.pushGood(diary, loginUser);

		Diary updatedGoodCount = diaryRepository.save(diary);
		
		return "{\"success\":\"" + Integer.toString(updatedGoodCount.getCountOfGood()) +"\"}";
	}
	
}
