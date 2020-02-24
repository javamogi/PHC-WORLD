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

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.alert.AlertRepository;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.timeline.TimelineRepository;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.DiaryRepository;

@Service
@Transactional
public class DiaryServiceImpl implements DiaryService {
	
	@Autowired
	private DiaryRepository diaryRepository;
	
	@Autowired
	private TimelineRepository timelineRepository;
	
	@Autowired
	private AlertRepository alertRepository;
	
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
		diaryRepository.save(diary);

		Timeline timeline = new Timeline("diary", "edit", diary, user, diary.getCreateDate());
		timelineRepository.save(timeline);

//		diary.setTimeline(timeline);
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
	public List<Diary> findDiaryListByWriter(User loginUser) {
		return diaryRepository.findByWriter(loginUser);
	}
	
	public String updateGood(Long diaryId, User loginUser) {
		Diary diary = diaryRepository.getOne(diaryId);
		Set<User> set = diary.getGoodPushedUser();
		if(set.contains(loginUser)) {
			set.remove(loginUser);
		} else {
			set.add(loginUser);
		}
		
		Diary updatedGoodCount = diaryRepository.save(diary);
		
		Timeline timeline = new Timeline("good", "thumbs-up", diary, loginUser, LocalDateTime.now());
		timelineRepository.save(timeline);
		
		if(!diary.matchUser(loginUser)) {
			Alert alert = new Alert("Diary", diary, diary.getWriter(), loginUser, LocalDateTime.now());
			alertRepository.save(alert);
		}
		
		return "{\"success\":\"" + Integer.toString(updatedGoodCount.getCountOfGood()) +"\"}";
	}
}
