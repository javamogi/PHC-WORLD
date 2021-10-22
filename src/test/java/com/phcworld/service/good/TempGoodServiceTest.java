package com.phcworld.service.good;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.board.TempDiary;
import com.phcworld.domain.good.TempGood;
import com.phcworld.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class TempGoodServiceTest {
	
	@Mock
	private TempGoodService goodService;

	@Test
	public void pushUpGoodButton() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		TempDiary diary = TempDiary.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		TempGood good = TempGood.builder()
				.id(1L)
				.tempDiary(diary)
				.user(user)
				.build();
		Set<TempGood> set = new HashSet<TempGood>();
		set.add(good);
		diary.setTempGood(set);
		when(goodService.pushGood(diary, user))
		.thenReturn(diary);
		TempDiary updatedGoodDiary = goodService.pushGood(diary, user);
		assertThat(1, is(updatedGoodDiary.getCountOfGood()));
		
		diary.updateGood(good);
		when(goodService.pushGood(diary, user))
		.thenReturn(diary);
		updatedGoodDiary = goodService.pushGood(diary, user);
		assertThat(0, is(updatedGoodDiary.getCountOfGood()));
	}
	
}
