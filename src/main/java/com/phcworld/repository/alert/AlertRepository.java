package com.phcworld.repository.alert;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;

public interface AlertRepository extends JpaRepository<Alert, Long> {
	Page<Alert> findByPostWriter(User user, Pageable Pageable);
	
	Alert findByGood(Good good);
	
	Alert findByDiaryAnswer(DiaryAnswer diaryAnswer);
	
	Alert findByFreeBoardAnswer(FreeBoardAnswer freeBoardAnswer);
}
