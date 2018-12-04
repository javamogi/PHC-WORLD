package com.phcworld.domain.good;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryServiceImpl;
import com.phcworld.domain.user.User;

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
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		Diary diary = diaryService.createDiary(user, "test3", "test3", "no-image-icon.gif");
		goodService.upGood(diary.getId(), user);
		Diary actual = diaryService.getOneDiary(diary.getId());
		assertThat(actual.getCountOfGood(), is(diary.getCountOfGood()));
	}

}
