package com.phcworld.repository.good;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.board.TempDiary;
import com.phcworld.domain.good.TempGood;
import com.phcworld.domain.user.User;

public interface TempGoodRepository extends JpaRepository<TempGood, Long>{
	TempGood findByTempDiaryAndUser(TempDiary diary, User user);
}
