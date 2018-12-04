package com.phcworld.domain.board;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DiaryServiceImplTest {
	
	private static final Logger log = LoggerFactory.getLogger(DiaryServiceImplTest.class);
	
	@Autowired
	private DiaryServiceImpl diaryService;

	@Test
	@Transactional
	public void createDiary() {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		Diary diary = diaryService.createDiary(user, "test3", "test3", "no-image-icon.gif");
		Diary actual = diaryService.getOneDiary(diary.getId());
		assertThat(actual, is(diary));
	}
	
	@Test
	@Transactional
	public void updateDiary() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		Diary diary = diaryService.createDiary(user, "test3", "test3", "no-image-icon.gif");
		Diary diaryUpdated = diaryService.updateDiary(diary, "updateDiary", "test.jpg");
		Diary actual = diaryService.getOneDiary(diary.getId());
		assertThat(actual, is(diaryUpdated));
	}
	
	@Test(expected = JpaObjectRetrievalFailureException.class)
	@Transactional
	public void deleteDiaryById() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		Diary diary = diaryService.createDiary(user, "test3", "test3", "no-image-icon.gif");
		log.debug("diary : {}", diary);
		Long id = diary.getId();
		diaryService.deleteDiaryById(id);
		diaryService.getOneDiary(id);
	}
	
	@Test
	@Transactional
	public void addAndDeleteDiaryAnswerCount() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		Diary diary = diaryService.createDiary(user, "test3", "test3", "no-image-icon.gif");
		Diary diaryAnswerAdded = diaryService.addDiaryAnswer(diary.getId());
		assertThat("[1]", is(diaryAnswerAdded.getCountOfAnswer()));
		Diary diaryAnswerDeleted = diaryService.deleteDiaryAnswer(diary.getId());
		assertThat("", is(diaryAnswerDeleted.getCountOfAnswer()));
	}
	
	@Test
	@Transactional
	public void findPageDiaryByWriter() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		Diary diary = diaryService.createDiary(user, "test3", "test3", "no-image-icon.gif");
		Diary diary2 = diaryService.createDiary(user, "test4", "test4", "no-image-icon.gif");
		PageRequest pageRequest = PageRequest.of(0, 6, new Sort(Direction.DESC, "id"));
		Page<Diary> pageDiary = diaryService.findPageDiaryByWriter(user, pageRequest, user);
		List<Diary> diaryList = pageDiary.getContent();
		assertThat(diaryList, hasItems(diary, diary2));
	}

}
