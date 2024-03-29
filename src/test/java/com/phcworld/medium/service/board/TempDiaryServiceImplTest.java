package com.phcworld.medium.service.board;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.phcworld.service.board.TempDiaryServiceImpl;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.TempDiaryAnswer;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.TempDiary;
import com.phcworld.domain.board.dto.TempDiaryRequest;
import com.phcworld.domain.board.dto.TempDiaryResponse;
import com.phcworld.domain.good.TempGood;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class TempDiaryServiceImplTest {
	
	@Mock
	private TempDiaryServiceImpl diaryService;
	
	@Test
	public void findPageDiary() throws Exception {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		TempDiary diary = TempDiary.builder()
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		TempDiary diary2 = TempDiary.builder()
				.writer(user)
				.title("title2")
				.contents("content2")
				.thumbnail("no-image-icon.gif")
				.build();
		List<TempDiary> list = new ArrayList<TempDiary>();
		list.add(diary);
		list.add(diary2);
		Page<TempDiary> page = new PageImpl<TempDiary>(list);
		when(diaryService.findPageDiary(user, 0, user)).thenReturn(page);
		Page<TempDiary> pageDiary = diaryService.findPageDiary(user, 0, user);
		List<TempDiary> diaryList = pageDiary.getContent();
		assertThat(diaryList, hasItems(diary, diary2));
	}
	
	@Test
	public void createDiary() {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		TempDiaryRequest request = TempDiaryRequest.builder()
				.title("title")
				.contents("contents")
				.thumbnail("no-image-icon.gif")
				.build();
		TempDiary diary = TempDiary.builder()
				.writer(user)
				.title(request.getTitle())
				.contents(request.getContents())
				.thumbnail(request.getThumbnail())
				.build();
				
		TempDiaryResponse diaryResponse = TempDiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.build();
		when(diaryService.createDiary(user, request))
		.thenReturn(diaryResponse);
		TempDiaryResponse createdDiary = diaryService.createDiary(user, request);
		assertThat("title", is(createdDiary.getTitle()));
		assertThat("contents", is(createdDiary.getContents()));
	}
	
	@Test
	public void getOneDiaryResponse() {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		TempDiary diary = TempDiary.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		TempDiaryResponse diaryResponse = TempDiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.countOfAnswers(diary.getCountOfAnswer())
				.build();
		when(diaryService.getOneDiary(1L)).thenReturn(diaryResponse);
		TempDiaryResponse oneDiaryResponse = diaryService.getOneDiary(1L);
		assertThat(diaryResponse, is(oneDiaryResponse));
	}
	
	@Test
	public void updateDiary() throws Exception {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		TempDiary diary = TempDiary.builder()
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		TempDiary newDiary = TempDiary.builder()
				.writer(user)
				.title("title")
				.contents("updateDiary")
				.thumbnail("test.jpg")
				.build();
		TempDiaryRequest request = TempDiaryRequest.builder()
				.id(newDiary.getId())
				.title(newDiary.getTitle())
				.contents(newDiary.getContents())
				.thumbnail(newDiary.getThumbnail())
				.build();
		TempDiaryResponse diaryResponse = TempDiaryResponse.builder()
				.id(newDiary.getId())
				.writer(newDiary.getWriter())
				.title(newDiary.getTitle())
				.contents(newDiary.getContents())
				.thumbnail(newDiary.getThumbnail())
				.countOfAnswers(newDiary.getCountOfAnswer())
				.build();
		diary.update(request);
		when(diaryService.updateDiary(request))
		.thenReturn(diaryResponse);
		TempDiaryResponse updatedDiary = diaryService.updateDiary(request);
		assertThat("updateDiary", is(updatedDiary.getContents()));
		assertThat("test.jpg", is(updatedDiary.getThumbnail()));
	}
	
	@Test
	public void deleteDiaryEmptyDiaryAnswer() throws Exception {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		TempDiary diary = TempDiary.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		diaryService.deleteDiary(diary.getId());
		verify(diaryService, times(1)).deleteDiary(diary.getId());
	}
	
	@Test
	public void deleteDiary() throws Exception {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		TempDiary diary = TempDiary.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		TempDiaryAnswer answer = TempDiaryAnswer.builder()
				.id(1L)
				.writer(user)
				.tempDiary(diary)
				.contents("diary answer content")
				.build();
		List<TempDiaryAnswer> list = new ArrayList<TempDiaryAnswer>();
		list.add(answer);
		diary.setTempDiaryAnswers(list);
		diaryService.deleteDiary(diary.getId());
		verify(diaryService, times(1)).deleteDiary(diary.getId());
	}
	
	@Test
	public void addGoodWhenPushGoodButton() throws Exception {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		TempDiary diary = TempDiary.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		TempGood good = TempGood.builder()
				.id(1L)
				.tempDiary(diary)
				.user(user)
				.build();
		Set<TempGood> goodUsers = new HashSet<TempGood>();
		goodUsers.add(good);
		diary.setTempGood(goodUsers);
		
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
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		TempDiary diary = TempDiary.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		TempGood good = TempGood.builder()
				.tempDiary(diary)
				.user(user)
				.createDate(LocalDateTime.now())
				.build();
		Set<TempGood> list = new HashSet<TempGood>();
		list.add(good);
		diary.setTempGood(list);
		diary.updateGood(good);
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
		UserEntity user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		TempDiary diary = TempDiary.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		List<TempDiary> list = new ArrayList<TempDiary>();
		list.add(diary);
		when(diaryService.findDiaryListByWriter(user))
		.thenReturn(list);
		List<TempDiary> diaryList = diaryService.findDiaryListByWriter(user);
		assertThat(diaryList, hasItems(diary));
	}
	
}
