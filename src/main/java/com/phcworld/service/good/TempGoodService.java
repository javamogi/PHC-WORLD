package com.phcworld.service.good;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phcworld.domain.board.TempDiary;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.good.TempGood;
import com.phcworld.domain.user.User;
import com.phcworld.repository.good.TempGoodRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;

@Service
public class TempGoodService {

	@Autowired
	private TempGoodRepository goodRepository;
	
	@Autowired
	private TimelineServiceImpl timelineService;
	
	@Autowired
	private AlertServiceImpl alertService;
	
	public TempDiary pushGood(TempDiary diary, User loginUser) {
		TempGood good = goodRepository.findByTempDiaryAndUser(diary, loginUser);
		if(good == null) {
			TempGood createdGood = TempGood.builder()
					.tempDiary(diary)
					.user(loginUser)
					.createDate(LocalDateTime.now())
					.build();
			good = goodRepository.save(createdGood);
//			timelineService.createTimeline(good);
//			if(!diary.matchUser(loginUser)) {
//				alertService.createAlert(good);
//			}
		} else {
			goodRepository.delete(good);
			diary.getTempGoodPushedUser().remove(good);
//			timelineService.deleteTimeline(good);
//			if(!diary.matchUser(loginUser)) {
//				alertService.deleteAlert(good);
//			}
		}
		return diary;
	}
	
	public List<TempGood> getGoodList(User user){
		List<TempGood> goodList = goodRepository.findByUser(user);
		return goodList;
	}
}
