package com.phcworld.service.board;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.service.board.DiaryServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class DiaryServiceImplTest {
	
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
				.build();
		Diary diary2 = Diary.builder()
				.writer(user)
				.title("title2")
				.contents("content2")
				.thumbnail("no-image-icon.gif")
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
				.build();
		when(diaryService.createDiary(user, diary))
		.thenReturn(diary);
		Diary createdDiary = diaryService.createDiary(user, diary);
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
				.build();
		Diary newDiary = Diary.builder()
				.writer(user)
				.title("title")
				.contents("updateDiary")
				.thumbnail("test.jpg")
				.build();
		diary.update(newDiary);
		when(diaryService.updateDiary(diary, newDiary))
		.thenReturn(diary);
		Diary updatedDiary = diaryService.updateDiary(diary, newDiary);
		assertThat("updateDiary", is(updatedDiary.getContents()));
		assertThat("test.jpg", is(updatedDiary.getThumbnail()));
	}
	
	@Test
	public void deleteDiaryEmptyDiaryAnswer() throws Exception {
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
				.build();
		diaryService.deleteDiary(diary);
		verify(diaryService, times(1)).deleteDiary(diary);
	}
	
	@Test
	public void deleteDiary() throws Exception {
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
				.build();
		DiaryAnswer answer = DiaryAnswer.builder()
				.id(1L)
				.writer(user)
				.diary(diary)
				.contents("diary answer content")
				.build();
		List<DiaryAnswer> list = new ArrayList<DiaryAnswer>();
		list.add(answer);
		diary.setDiaryAnswers(list);
		diaryService.deleteDiary(diary);
		verify(diaryService, times(1)).deleteDiary(diary);
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
				.build();
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.createDate(LocalDateTime.now())
				.build();
		List<Good> list = new ArrayList<Good>();
		list.add(good);
		diary.setGoodPushedUser(list);
		
		SuccessResponse successResponse = SuccessResponse.builder()
				.success(Integer.toString(diary.getCountOfGood()))
				.build();
		
		when(diaryService.updateGood(diary.getId(), user))
		.thenReturn(successResponse);
		SuccessResponse newSuccessResponse = diaryService.updateGood(diary.getId(), user);
		assertThat(successResponse, is(newSuccessResponse));
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
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.createDate(LocalDateTime.now())
				.build();
		List<Good> list = new ArrayList<Good>();
		list.add(good);
		diary.setGoodPushedUser(list);
		diary.getGoodPushedUser().remove(good);
		SuccessResponse successResponse = SuccessResponse.builder()
				.success(Integer.toString(diary.getCountOfGood()))
				.build();
		when(diaryService.updateGood(diary.getId(), user))
		.thenReturn(successResponse);
		SuccessResponse newSuccessResponse = diaryService.updateGood(diary.getId(), user);
		assertThat(successResponse, is(newSuccessResponse));
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
				.build();
		List<Diary> list = new ArrayList<Diary>();
		list.add(diary);
		when(diaryService.findDiaryListByWriter(user))
		.thenReturn(list);
		List<Diary> diaryList = diaryService.findDiaryListByWriter(user);
		assertThat(diaryList, hasItems(diary));
	}
	
}
