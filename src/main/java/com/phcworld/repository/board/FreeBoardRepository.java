package com.phcworld.repository.board;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;
import org.springframework.data.jpa.repository.Query;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>, FreeBoardRepositoryCustom {
	List<FreeBoard> findByWriter(User user);

	@Query("select f from FreeBoard f join fetch f.writer")
	List<FreeBoard> findAllByFetch();
}
