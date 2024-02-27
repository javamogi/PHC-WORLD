package com.phcworld.service.good;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.exception.model.CustomBaseException;
import com.phcworld.exception.model.InternalServerErrorException;
import com.phcworld.repository.good.GoodRepository;
import com.phcworld.service.alert.AlertServiceImpl;
import com.phcworld.service.timeline.TimelineServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoodService {

	private final GoodRepository goodRepository;
	
	private final TimelineServiceImpl timelineService;
	
	private final AlertServiceImpl alertService;

	@Transactional
	public Diary pushGood(Diary diary, User loginUser) {
		Good good = goodRepository.findByDiaryAndUser(diary, loginUser);
		if(good == null) {
			Good createdGood = Good.builder()
					.diary(diary)
					.user(loginUser)
					.createDate(LocalDateTime.now())
					.build();
			try {
				good = goodRepository.save(createdGood);
			} catch (DataIntegrityViolationException e){
				throw new InternalServerErrorException();
			}
			timelineService.createTimeline(good);
			if(!diary.matchUser(loginUser)) {
				alertService.createAlert(good);
			}
			diary.addGood();
		} else {
			goodRepository.delete(good);
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
