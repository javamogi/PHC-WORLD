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
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.user.User;
import com.phcworld.repository.alert.AlertRepository;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
@Transactional
public class DiaryServiceImpl implements DiaryService {
	
	@Autowired
	private DiaryRepository diaryRepository;
	
	@Autowired
	private AlertRepository alertRepository;
	
	@Autowired
	private TimelineServiceImpl timelineService;
	
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
		diaryRepository.delete(diary);
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
//			timelineService.deleteTimeline(diary, loginUser);
		} else {
			set.add(loginUser);
		}
		
		Diary updatedGoodCount = diaryRepository.save(diary);
		
		timelineService.createTimeline(updatedGoodCount, loginUser);
		//삭제 diaryAndUser
		
		if(!diary.matchUser(loginUser)) {
			Alert alert = Alert.builder()
					.type("Diary")
					.diary(diary)
					.postWriter(diary.getWriter())
					.registerUser(loginUser)
					.createDate(LocalDateTime.now())
					.build();
			alertRepository.save(alert);
			// 삭제 diaryAndRegisterUser
		}
		
		return "{\"success\":\"" + Integer.toString(updatedGoodCount.getCountOfGood()) +"\"}";
	}
}
