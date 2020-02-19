package com.phcworld.domain.good;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.user.User;
import com.phcworld.service.board.DiaryServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class GoodServiceImplTest {
	
	@Autowired
	private GoodServiceImpl goodService;
	
	@Autowired
	private DiaryServiceImpl diaryService;

	@Test
	@Transactional
	public void upGood() {
		User user = User.builder()
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		user.setId(1L);
		Diary diary = diaryService.createDiary(user, "test3", "test3", "no-image-icon.gif");
		goodService.upGood(diary.getId(), user);
		Diary actual = diaryService.getOneDiary(diary.getId());
		assertThat(actual.getCountOfGood(), is(diary.getCountOfGood()));
	}

}
