package com.phcworld.repository.board;


import java.util.List;

import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.board.FreeBoard;
import org.springframework.data.jpa.repository.Query;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>, FreeBoardRepositoryCustom {
	List<FreeBoard> findByWriter(UserEntity user);

	@Query("select f from FreeBoard f join fetch f.writer")
	List<FreeBoard> findAllByFetch();
}
