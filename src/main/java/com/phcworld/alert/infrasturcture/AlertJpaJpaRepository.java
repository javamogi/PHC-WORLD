package com.phcworld.alert.infrasturcture;

import com.phcworld.domain.embedded.PostInfo;
import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlertJpaJpaRepository extends JpaRepository<AlertEntity, Long>, AlertJpaRepositoryCustom {
	Page<AlertEntity> findByPostWriter(UserEntity user, Pageable Pageable);

//	Optional<Alert> findBySaveTypeAndPostIdAndRegisterUser(SaveType saveType, Long postId, User registerUser);
	Optional<AlertEntity> findByPostInfo(PostInfo postInfo);
	
//	Alert findByGood(Good good);
//
//	Alert findByDiaryAnswer(DiaryAnswer diaryAnswer);
//
//	Alert findByFreeBoardAnswer(FreeBoardAnswer freeBoardAnswer);
}
