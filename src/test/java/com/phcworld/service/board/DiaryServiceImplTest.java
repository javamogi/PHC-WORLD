package com.phcworld.service.board;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;
import com.phcworld.service.board.DiaryServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class DiaryServiceImplTest {
	
	private static final Logger log = LoggerFactory.getLogger(DiaryServiceImplTest.class);
	
	@Mock
	private DiaryServiceImpl diaryService;
	
	@Test
	public void findPageDiary() throws Exception {
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
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		Diary diary2 = Diary.builder()
				.writer(user)
				.title("title2")
				.contents("content2")
				.thumbnail("no-image-icon.gif")
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		List<Diary> list = new ArrayList<Diary>();
		list.add(diary);
		list.add(diary2);
		Page<Diary> page = new PageImpl<Diary>(list);
		when(diaryService.findPageDiary(user, 0, user)).thenReturn(page);
		Page<Diary> pageDiary = diaryService.findPageDiary(user, 0, user);
		List<Diary> diaryList = pageDiary.getContent();
		assertThat(diaryList, hasItems(diary, diary2));
	}
	
	@Test
	public void createDiary() {
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
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		when(diaryService.createDiary(user, "title", "content", "no-image-icon.gif"))
		.thenReturn(diary);
		Diary createdDiary = diaryService.createDiary(user, "title", "content", "no-image-icon.gif");
		assertThat("title", is(createdDiary.getTitle()));
		assertThat("content", is(createdDiary.getContents()));
	}
	
	@Test
	public void getOneDiary() {
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
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		when(diaryService.getOneDiary(1L)).thenReturn(diary);
		Diary oneDiary = diaryService.getOneDiary(1L);
		assertThat(diary, is(oneDiary));
	}
	
	@Test
	public void updateDiary() throws Exception {
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
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		diary.update("updateDiary", "test.jpg");
		when(diaryService.updateDiary(diary, "updateDiary", "test.jpg"))
		.thenReturn(diary);
		Diary updatedDiary = diaryService.updateDiary(diary, "updateDiary", "test.jpg");
		assertThat("updateDiary", is(updatedDiary.getContents()));
		assertThat("test.jpg", is(updatedDiary.getThumbnail()));
	}
	
	@Test
	public void deleteDiaryById() throws Exception {
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
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		diaryService.deleteDiaryById(diary.getId());
		verify(diaryService, times(1)).deleteDiaryById(diary.getId());
	}
	
	@Test
	public void addDiaryAnswerCount() throws Exception {
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
//				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer answer = DiaryAnswer.builder()
				.writer(user)
				.diary(diary)
				.contents("content")
				.createDate(LocalDateTime.now())
				.build();
		List<DiaryAnswer> list = new ArrayList<DiaryAnswer>();
		list.add(answer);
		diary.setDiaryAnswers(list);
		
		when(diaryService.addDiaryAnswer(diary.getId()))
		.thenReturn(diary);
		Diary addedAnswerDiary = diaryService.addDiaryAnswer(diary.getId());
		assertThat("[1]", is(addedAnswerDiary.getCountOfAnswer()));
	}
	
	@Test
	public void deleteDiaryAnswerCount() {
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
				.countOfAnswer(1)
				.createDate(LocalDateTime.now())
				.build();
		diary.deleteAnswer();
		when(diaryService.deleteDiaryAnswer(diary.getId()))
		.thenReturn(diary);
		Diary deletedAnswerDiary = diaryService.deleteDiaryAnswer(diary.getId());
		assertThat("", is(deletedAnswerDiary.getCountOfAnswer()));
	}
	
	@Test
	public void addGoodWhenPushGoodButton() throws Exception {
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
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		Set<User> set = new HashSet<User>();
		set.add(user);
		diary.setGoodPushedUser(set);
		when(diaryService.updateGood(diary.getId(), user))
		.thenReturn("{\"success\":\"" + Integer.toString(diary.getGoodPushedUser().size()) +"\"}");
		String str = diaryService.updateGood(diary.getId(), user);
		assertThat("{\"success\":\"1\"}", is(str));
	}
	
	@Test
	public void declineGoodWhenPushGoodButton() throws Exception {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Set<User> set = new HashSet<User>();
		set.add(user);
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.goodPushedUser(set)
				.build();
		diary.getGoodPushedUser().remove(user);
		when(diaryService.updateGood(diary.getId(), user))
		.thenReturn("{\"success\":\"" + Integer.toString(diary.getGoodPushedUser().size()) +"\"}");
		String str = diaryService.updateGood(diary.getId(), user);
		assertThat("{\"success\":\"0\"}", is(str));
	}
	
	@Test
	public void findDiaryListByWriter() {
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
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		List<Diary> list = new ArrayList<Diary>();
		list.add(diary);
		when(diaryService.findDiaryListByWriter(user))
		.thenReturn(list);
		List<Diary> diaryList = diaryService.findDiaryListByWriter(user);
		assertThat(diaryList, hasItems(diary));
	}
	
}