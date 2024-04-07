package com.phcworld.service.good;

import java.time.LocalDateTime;

import com.phcworld.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.phcworld.domain.board.TempDiary;
import com.phcworld.domain.good.TempGood;
import com.phcworld.repository.good.TempGoodRepository;
import com.phcworld.service.timeline.TempTimelineServiceImpl;

@Service
@RequiredArgsConstructor
public class TempGoodService {

	private final TempGoodRepository goodRepository;
	
	private final TempTimelineServiceImpl timelineService;
	
//	@Autowired
//	private AlertServiceImpl alertService;
	
	public TempDiary pushGood(TempDiary diary, UserEntity loginUser) {
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
