package com.phcworld.domain.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.timeline.TimelineRepository;
import com.phcworld.domain.user.User;

@Service
@Transactional
public class DiaryServiceImpl implements DiaryService {
	
	@Autowired
	private DiaryRepository diaryRepository;
	
	@Autowired
	private TimelineRepository timelineRepository;
	
	@Override
	public Page<Diary> findPageDiaryByWriter(User loginUser, PageRequest pageRequest, User requestUser) {
		if(loginUser == null || !requestUser.matchId(loginUser.getId())) {
			return diaryRepository.findByWriter(requestUser, pageRequest);
		}
		return diaryRepository.findByWriter(loginUser, pageRequest);
	}

	@Override
	public Diary createDiary(User user, String title, String contents, String thumbnail) {
		Diary diary = new Diary(user, title, contents, thumbnail);
		diaryRepository.save(diary);

		Timeline timeline = new Timeline("diary", "edit", diary, user, diary.getCreateDate());
		timelineRepository.save(timeline);

		diary.setTimeline(timeline);
		return diaryRepository.save(diary);
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
	public void deleteDiaryById(Long id) {
		diaryRepository.deleteById(id);
	}
	
	@Override
	public Diary addDiaryAnswer(Long id) {
		Diary diary = diaryRepository.getOne(id);
		diary.addAnswer();
		return diaryRepository.save(diary);
	}
	
	@Override
	public Diary deleteDiaryAnswer(Long id) {
		Diary diary = diaryRepository.getOne(id);
		diary.deleteAnswer();
		return diaryRepository.save(diary);
	}
	
	@Override
	public List<Diary> findDiaryListByWriter(User loginUser) {
		return diaryRepository.findByWriter(loginUser);
	}

}
