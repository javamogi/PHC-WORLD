package com.phcworld.repository.alert;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long>, AlertRepositoryCustom {
	Page<Alert> findByPostWriter(User user, Pageable Pageable);

	Optional<Alert> findBySaveTypeAndPostIdAndRegisterUser(SaveType saveType, Long postId, User registerUser);
	
//	Alert findByGood(Good good);
//
//	Alert findByDiaryAnswer(DiaryAnswer diaryAnswer);
//
//	Alert findByFreeBoardAnswer(FreeBoardAnswer freeBoardAnswer);
}
