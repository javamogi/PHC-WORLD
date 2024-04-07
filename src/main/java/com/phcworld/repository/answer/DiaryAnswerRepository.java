package com.phcworld.repository.answer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.user.infrastructure.UserEntity;


public interface DiaryAnswerRepository extends JpaRepository<DiaryAnswer, Long> {
	List<DiaryAnswer> findByWriter(UserEntity user);
}
