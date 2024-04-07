package com.phcworld.repository.board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.board.TempDiary;
import com.phcworld.user.infrastructure.UserEntity;

public interface TempDiaryRepository extends JpaRepository<TempDiary, Long> {
	Page<TempDiary> findByWriter(UserEntity user, Pageable Pageable);
	List<TempDiary> findByWriter(UserEntity user);
}
