package com.phcworld.service.good;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.service.good.GoodService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class GoodServiceTest {
	
	@Mock
	private GoodService goodService;

	@Test
	public void pushGoodButton() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
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
				.id(1L)
				.diary(diary)
				.user(user)
				.build();
		List<Good> list = new ArrayList<Good>();
		list.add(good);
		diary.setGoodPushedUser(list);
		when(goodService.pushGood(diary, user))
		.thenReturn(diary);
		Diary updatedGoodDiary = goodService.pushGood(diary, user);
		assertThat(1, is(updatedGoodDiary.getGoodPushedUser().size()));
	}
	
	@Test
	public void getList() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
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
