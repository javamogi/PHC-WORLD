package com.phcworld.domain.good;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.alert.AlertRepository;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryRepository;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.timeline.TimelineRepository;
import com.phcworld.domain.user.User;

@Service
public class GoodServiceImpl implements GoodService {
	
	private static final Logger log = LoggerFactory.getLogger(GoodServiceImpl.class);
	
	@Autowired
	private DiaryRepository diaryRepository;

	@Autowired
	private GoodRepository goodRepository;
	
	@Autowired
	private TimelineRepository timelineRepository;
	
	@Autowired
	private AlertRepository alertRepository;
	
	@Override
	public String upGood(Long diaryId, User loginUser) {
		PageRequest pageRequest = PageRequest.of(0, 5, new Sort(Direction.DESC, "id"));
		Page<Good> checkGood = goodRepository.findByUser(loginUser, pageRequest);
		List<Good> goodList = checkGood.getContent();
		Diary diary = diaryRepository.getOne(diaryId);
		log.debug("diary : {}", diary.getCountOfGood());
		if(goodList.size() > 0) {
			for(int i = 0; i < goodList.size(); i++) {
				if(goodList.get(i).getDiary().matchId(diaryId)) {
					diary.minusGood();
					goodRepository.deleteById(goodList.get(i).getId());
					return Integer.toString(diary.getCountOfGood());
				}
			}
		}
		
		diary.addGood();
		Good good = new Good(diary, loginUser);
		goodRepository.save(good);
		
		Timeline timeline = new Timeline("good", "thumbs-up", diary, good, loginUser, good.getSaveDate());
		timelineRepository.save(timeline);
		
		good.setTimeline(timeline);
		goodRepository.save(good);

		if(!diary.matchUser(loginUser)) {
			Alert alert = new Alert("Diary", good, diary.getWriter(), good.getSaveDate());
			alertRepository.save(alert);
			good.setAlert(alert);
			goodRepository.save(good);
		}
		return Integer.toString(diary.getCountOfGood());
	}
}
