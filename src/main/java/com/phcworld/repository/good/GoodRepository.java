package com.phcworld.repository.good;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.good.Good;
import com.phcworld.user.infrastructure.UserEntity;

public interface GoodRepository extends JpaRepository<Good, Long>{
	Good findByDiaryAndUser(Diary diary, UserEntity user);
	
	List<Good> findByUser(UserEntity user);
}
