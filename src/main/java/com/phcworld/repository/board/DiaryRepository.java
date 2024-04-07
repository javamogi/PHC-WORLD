package com.phcworld.repository.board;

import java.util.List;

import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.board.Diary;

public interface DiaryRepository extends JpaRepository<Diary, Long>, DiaryRepositoryCustom {
	Page<Diary> findByWriter(UserEntity user, Pageable Pageable);
	List<Diary> findByWriter(UserEntity user);

}
