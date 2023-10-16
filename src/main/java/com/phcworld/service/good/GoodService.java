package com.phcworld.service.good;

import java.time.LocalDateTime;
import java.util.List;

import com.phcworld.domain.common.SaveType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.repository.good.GoodRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
@RequiredArgsConstructor
public class GoodService {

	private final GoodRepository goodRepository;
	
	private final TimelineServiceImpl timelineService;
	
	private final AlertServiceImpl alertService;
	
	public Diary pushGood(Diary diary, User loginUser) {
		Good good = goodRepository.findByDiaryAndUser(diary, loginUser);
		if(good == null) {
			Good createdGood = Good.builder()
					.diary(diary)
					.user(loginUser)
					.createDate(LocalDateTime.now())
					.build();
			good = goodRepository.save(createdGood);
			timelineService.createTimeline(good);
			if(!diary.matchUser(loginUser)) {
				alertService.createAlert(good);
			}
			diary.addGood();
		} else {
			goodRepository.delete(good);
			diary.getGoodPushedUser().remove(good);
			timelineService.deleteTimeline(SaveType.GOOD, good.getId());
			if(!diary.matchUser(loginUser)) {
				alertService.deleteAlert(good);
			}
			diary.removeGood();
		}
		return diary;
	}
	
	public List<Good> getGoodList(User user){
		List<Good> goodList = goodRepository.findByUser(user);
		return goodList;
	}
}
