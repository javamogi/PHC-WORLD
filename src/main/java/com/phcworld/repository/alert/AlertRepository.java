package com.phcworld.repository.alert;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.embedded.PostInfo;
import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long>, AlertRepositoryCustom {
	Page<Alert> findByPostWriter(UserEntity user, Pageable Pageable);

//	Optional<Alert> findBySaveTypeAndPostIdAndRegisterUser(SaveType saveType, Long postId, User registerUser);
	Optional<Alert> findByPostInfo(PostInfo postInfo);
	
//	Alert findByGood(Good good);
//
//	Alert findByDiaryAnswer(DiaryAnswer diaryAnswer);
//
//	Alert findByFreeBoardAnswer(FreeBoardAnswer freeBoardAnswer);
}
