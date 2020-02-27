package com.phcworld.service.alert;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.user.User;
import com.phcworld.repository.alert.AlertRepository;

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
	public List<Alert> findPageRequestAlertByPostUser(User loginUser) {
		PageRequest pageRequest = PageRequest.of(0, 5, new Sort(Direction.DESC, "id"));
		Page<Alert> pageAlert = alertRepository.findByPostWriter(loginUser, pageRequest);
		return pageAlert.getContent();
	}
	
	public Alert createAlert(Diary diary, User loginUser) {
		Alert alert = Alert.builder()
				.type("Diary")
				.diary(diary)
				.postWriter(diary.getWriter())
				.registerUser(loginUser)
				.createDate(LocalDateTime.now())
				.build();
		return alertRepository.save(alert);
	}
	
	public Alert createAlert(DiaryAnswer diaryAnswer) {
		Alert alert = Alert.builder()
				.type("Diary")
				.diaryAnswer(diaryAnswer)
				.postWriter(diaryAnswer.getDiary().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		return alertRepository.save(alert);
	}
	
	public Alert createAlert(FreeBoardAnswer freeBoardAnswer) {
		Alert alert = Alert.builder()
				.type("FreeBoard")
				.freeBoardAnswer(freeBoardAnswer)
				.postWriter(freeBoardAnswer.getFreeBoard().getWriter())
				.createDate(LocalDateTime.now())
				.build();
		return alertRepository.save(alert);
	}
	
	public void deleteAlert(Diary diary, User loginUser) {
		Alert alert = alertRepository.findByDiaryAndRegisterUser(diary, loginUser);
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
