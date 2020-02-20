package com.phcworld.repository.answer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.user.User;

public interface FreeBoardAnswerRepository extends JpaRepository<FreeBoardAnswer, Long> {
	List<FreeBoardAnswer> findByWriter(User user);
}
