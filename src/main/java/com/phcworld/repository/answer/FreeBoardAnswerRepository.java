package com.phcworld.repository.answer;

import java.util.List;

import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.answer.FreeBoardAnswer;

public interface FreeBoardAnswerRepository extends JpaRepository<FreeBoardAnswer, Long> {
	List<FreeBoardAnswer> findByWriter(UserEntity user);
}
