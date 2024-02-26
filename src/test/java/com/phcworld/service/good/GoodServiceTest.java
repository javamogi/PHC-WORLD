package com.phcworld.service.good;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.phcworld.domain.user.Authority;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.repository.good.GoodRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.service.good.GoodService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@Transactional
@Slf4j
public class GoodServiceTest {
	
	@SpyBean
	private GoodService goodService;

	@Autowired
	private GoodRepository goodRepository;

	@Autowired
	private DiaryRepository diaryRepository;

	@Test
	public void pushGoodButton() throws InterruptedException {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();

		Diary d = diaryRepository.findById(1L).orElseThrow(() -> new RuntimeException());

		int numberOfThreads = 2;
		ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);

		log.info("동시성 테스트 진행");
		executorService.execute(() -> {
			goodService.pushGood(d, user);
			latch.countDown();
		});
		executorService.execute(() -> {
			goodService.pushGood(d, user);
			latch.countDown();
		});
		latch.await();

		List<Good> goods = goodRepository.findAll();

		assertThat(0L, is(d.getCountGood()));
		assertThat(0, is(goods.size()));
	}
	
	@Test
	public void getList() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.build();
		List<Good> list = new ArrayList<Good>();
		list.add(good);
		when(goodService.getGoodList(user))
		.thenReturn(list);
		List<Good> goods = goodService.getGoodList(user);
		assertThat(goods, hasItems(good));
	}
	
}
