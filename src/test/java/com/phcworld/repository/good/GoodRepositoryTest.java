package com.phcworld.repository.good;


import com.phcworld.domain.board.Diary;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.util.DiaryFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class GoodRepositoryTest {
	
	@Autowired
	private GoodRepository goodRepository;
	
	@Autowired
	private DiaryRepository diaryRepository;

	private User user;
	private Diary diary;

	@Before
	public void setup(){
		user = User.builder()
				.id(1L)
				.build();
		diary = diaryRepository.save(DiaryFactory.getDiaryEntity(user));
	}

	@Test
	public void create() {
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.build();
		Good newGood = goodRepository.save(good);
		assertNotNull(newGood);
	}
	
	@Test
	public void readByDiaryAndUser() {
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.build();
		Good newGood = goodRepository.save(good);
		Good findGood = goodRepository.findByDiaryAndUser(diary, user);
		assertThat(newGood).isEqualTo(findGood);
	}

	@Test
	public void readByUser() {
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.build();
		Good newGood = goodRepository.save(good);
		List<Good> goodList = goodRepository.findByUser(user);
		assertThat(goodList).contains(newGood);
	}
	
	@Test
	public void delete() {
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.build();
		Good newGood = goodRepository.save(good);
		goodRepository.delete(newGood);
		Optional<Good> findGood = goodRepository.findById(newGood.getId());
		assertFalse(findGood.isPresent());
	}

}
