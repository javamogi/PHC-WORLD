package com.phcworld.service.good;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.repository.good.GoodRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
public class GoodService {

	@Autowired
	private GoodRepository goodRepository;
	
	@Autowired
	private TimelineServiceImpl timelineService;
	
	@Autowired
	private AlertServiceImpl alertService;
	
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
		} else {
			goodRepository.delete(good);
			diary.getGoodPushedUser().remove(good);
			timelineService.deleteTimeline(good);
			if(!diary.matchUser(loginUser)) {
				alertService.deleteAlert(good);
			}
		}
		return diary;
	}
	
	public List<Good> getGoodList(User user){
		List<Good> goodList = goodRepository.findByUser(user);
		return goodList;
	}
}
