package com.phcworld.repository.good;

import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.phcworld.domain.board.TempDiary;
import com.phcworld.domain.good.TempGood;

public interface TempGoodRepository extends JpaRepository<TempGood, Long>{
	TempGood findByTempDiaryAndUser(TempDiary diary, UserEntity user);
}
