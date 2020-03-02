package com.phcworld.repository.good;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.repository.good.GoodRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class GoodRepositoryTest {
	
	@Autowired
	private GoodRepository goodRepository;
	
	@Autowired
	private DiaryRepository diaryRepository;

	@Test
	public void create() {
		User writer = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		Diary diary = Diary.builder()
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Diary newDiary = diaryRepository.save(diary);
		Good good = Good.builder()
				.diary(newDiary)
				.user(writer)
				.build();
		Good newGood = goodRepository.save(good);
		assertNotNull(newGood);
	}
	
	@Test
	public void readByDiaryAndUser() {
		User writer = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		Diary diary = Diary.builder()
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Diary newDiary = diaryRepository.save(diary);
		Good good = Good.builder()
				.diary(newDiary)
				.user(writer)
				.build();
		Good newGood = goodRepository.save(good);
		Good findGood = goodRepository.findByDiaryAndUser(newDiary, writer);
		assertThat(newGood, is(findGood));
	}
	
	@Test
	public void readByUser() {
		User writer = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		Diary diary = Diary.builder()
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Diary newDiary = diaryRepository.save(diary);
		Good good = Good.builder()
				.diary(newDiary)
				.user(writer)
				.build();
		Good newGood = goodRepository.save(good);
		List<Good> goodList = goodRepository.findByUser(writer);
		assertThat(goodList, hasItem(newGood));
	}
	
	@Test
	public void delete() {
		User writer = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		Diary diary = Diary.builder()
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Diary newDiary = diaryRepository.save(diary);
		Good good = Good.builder()
				.diary(newDiary)
				.user(writer)
				.build();
		Good newGood = goodRepository.save(good);
		goodRepository.delete(newGood);
		Optional<Good> findGood = goodRepository.findById(newGood.getId());
		assertFalse(findGood.isPresent());
	}

}
