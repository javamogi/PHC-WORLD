package com.phcworld.repository.board;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {
	List<FreeBoard> findByWriter(User user);
}
