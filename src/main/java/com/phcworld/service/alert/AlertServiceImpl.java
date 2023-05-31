package com.phcworld.service.alert;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.repository.alert.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AlertServiceImpl implements AlertService {
	
	@Autowired
	private AlertRepository alertRepository;
	
	@Override
	public Alert getOneAlert(Long id) {
		return alertRepository.getOne(id);
	}
	
	@Override
	public List<Alert> findListAlertByPostUser(User loginUser) {
//		PageRequest pageRequest = PageRequest.of(0, 5, new Sort(Direction.DESC, "id"));
		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("id").descending());
		Page<Alert> pageAlert = alertRepository.findByPostWriter(loginUser, pageRequest);
		return pageAlert.getContent();
	}
	
	public Alert createAlert(Good good) {
		Alert alert = Alert.builder()
				.type("Diary")
				.good(good)
				.postWriter(good.getDiary().getWriter())
				.createDate(good.getCreateDate())
				.build();
		return alertRepository.save(alert);
	}
	
	public Alert createAlert(DiaryAnswer diaryAnswer) {
		Alert alert = Alert.builder()
				.type("Diary")
				.diaryAnswer(diaryAnswer)
				.postWriter(diaryAnswer.getDiary().getWriter())
				.createDate(diaryAnswer.getCreateDate())
				.build();
		return alertRepository.save(alert);
	}
	
	public Alert createAlert(FreeBoardAnswer freeBoardAnswer) {
		Alert alert = Alert.builder()
				.type("FreeBoard")
				.freeBoardAnswer(freeBoardAnswer)
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(freeBoardAnswer.getCreateDate())
				.build();
		return alertRepository.save(alert);
	}
	
	public void deleteAlert(Good good) {
		Alert alert = alertRepository.findByGood(good);
		if(alert != null) {
			alertRepository.delete(alert);
		}
	}
	
	public void deleteAlert(DiaryAnswer diaryAnswer) {
		Alert alert = alertRepository.findByDiaryAnswer(diaryAnswer);
		if(alert != null) {
			alertRepository.delete(alert);
		}
	}
	
	public void deleteAlert(FreeBoardAnswer freeBoardAnswer) {
		Alert alert = alertRepository.findByFreeBoardAnswer(freeBoardAnswer);
		if(alert != null) {
			alertRepository.delete(alert);
		}
	}
	
}
