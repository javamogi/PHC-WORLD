package com.phcworld.repository.good;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;

public interface GoodRepository extends JpaRepository<Good, Long>{
	Good findByDiaryAndUser(Diary diary, User user);
}
