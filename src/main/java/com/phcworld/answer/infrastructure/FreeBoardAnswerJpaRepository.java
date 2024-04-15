package com.phcworld.answer.infrastructure;

import java.util.List;

import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeBoardAnswerJpaRepository extends JpaRepository<FreeBoardAnswerEntity, Long> {
	List<FreeBoardAnswerEntity> findByWriter(UserEntity user);
}
