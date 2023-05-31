package com.phcworld.service.good;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phcworld.domain.board.TempDiary;
import com.phcworld.domain.good.TempGood;
import com.phcworld.domain.user.User;
import com.phcworld.repository.good.TempGoodRepository;
import com.phcworld.service.timeline.TempTimelineServiceImpl;

@Service
@RequiredArgsConstructor
public class TempGoodService {

	private TempGoodRepository goodRepository;
	
	private TempTimelineServiceImpl timelineService;
	
//	@Autowired
//	private AlertServiceImpl alertService;
	
	public TempDiary pushGood(TempDiary diary, User loginUser) {
		TempGood good = goodRepository.findByTempDiaryAndUser(diary, loginUser);
		if(good == null) {
			TempGood createdGood = TempGood.builder()
					.tempDiary(diary)
					.user(loginUser)
					.createDate(LocalDateTime.now())
					.build();
			good = goodRepository.save(createdGood);
			timelineService.createTimeline("good", good, diary.getId());
//			if(!diary.matchUser(loginUser)) {
//				alertService.createAlert(good);
//			}
		} else {
			goodRepository.delete(good);
			timelineService.deleteTimeline(good);
//			if(!diary.matchUser(loginUser)) {
//				alertService.deleteAlert(good);
//			}
		}
		diary.updateGood(good);
		return diary;
	}
}
