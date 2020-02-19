package com.phcworld.repository.board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.user.User;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
	Page<Diary> findByWriter(User user, Pageable Pageable);
	List<Diary> findByWriter(User user);
}
