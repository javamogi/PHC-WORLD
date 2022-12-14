package com.phcworld.repository.board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.TempDiary;
import com.phcworld.domain.user.User;

public interface TempDiaryRepository extends JpaRepository<TempDiary, Long> {
	Page<TempDiary> findByWriter(User user, Pageable Pageable);
	List<TempDiary> findByWriter(User user);
}
