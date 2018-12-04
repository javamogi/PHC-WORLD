package com.phcworld.domain.answer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.user.User;


public interface DiaryAnswerRepository extends JpaRepository<DiaryAnswer, Long> {
	List<DiaryAnswer> findByWriter(User user);
}
