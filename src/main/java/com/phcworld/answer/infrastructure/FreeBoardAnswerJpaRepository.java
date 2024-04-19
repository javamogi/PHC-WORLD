package com.phcworld.answer.infrastructure;

import java.util.List;

import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeBoardAnswerJpaRepository extends JpaRepository<FreeBoardAnswerEntity, Long>, FreeBoardAnswerJpaRepositoryCustom {
	List<FreeBoardAnswerEntity> findByWriter(UserEntity user);
	Page<FreeBoardAnswerEntity> findByFreeBoard(FreeBoardEntity freeBoard, Pageable pageable);
}
