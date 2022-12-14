package com.phcworld.repository.board;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.board.TempFreeBoard;
import com.phcworld.domain.user.User;

public interface TempFreeBoardRepository extends JpaRepository<TempFreeBoard, Long> {
	List<TempFreeBoard> findByWriter(User user);
}
