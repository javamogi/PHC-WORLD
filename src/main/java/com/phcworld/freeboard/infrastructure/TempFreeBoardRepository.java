package com.phcworld.freeboard.infrastructure;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.user.infrastructure.UserEntity;

public interface TempFreeBoardRepository extends JpaRepository<TempFreeBoard, Long> {
	List<TempFreeBoard> findByWriter(UserEntity user);
}
