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
import java.util.stream.Collectors;

import com.phcworld.domain.board.DiaryResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryRequest;
import com.phcworld.domain.board.DiaryResponse;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.service.board.DiaryServiceImpl;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@Transactional
@Slf4j
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
	public void getDiaryResponseDto() throws Exception {
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
		List<DiaryResponse> diaryResponseList = page.getContent().stream()
				.map(DiaryResponse::of)
				.collect(Collectors.toList());
		DiaryResponseDto diaryResponseDto = DiaryResponseDto.builder()
				.totalPages(page.getTotalPages())
				.diaries(diaryResponseList)
				.build();
		when(diaryService.getDiaryResponseListTemp(user, user.getEmail(), 1)).thenReturn(diaryResponseDto);
		DiaryResponseDto dto = diaryService.getDiaryResponseListTemp(user, user.getEmail(), 1);
		assertThat(dto.getDiaries(), hasItems(DiaryResponse.of(diary), DiaryResponse.of(diary2)));
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
		DiaryRequest request = DiaryRequest.builder()
				.title("title")
				.contents("contents")
				.thumbnail("no-image-icon.gif")
				.build();
		Diary diary = Diary.builder()
				.writer(user)
				.title(request.getTitle())
				.contents(request.getContents())
				.thumbnail(request.getThumbnail())
				.build();
				
		DiaryResponse diaryResponse = DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.build();
		when(diaryService.createDiary(user, request))
		.thenReturn(diaryResponse);
		DiaryResponse createdDiary = diaryService.createDiary(user, request);
		assertThat("title", is(createdDiary.getTitle()));
		assertThat("contents", is(createdDiary.getContents()));
	}
	
	@Test
	public void getOneDiaryResponse() {
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
		DiaryResponse diaryResponse = DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.countOfAnswers(diary.getCountOfAnswer())
				.build();
		when(diaryService.getOneDiary(1L)).thenReturn(diaryResponse);
		DiaryResponse oneDiaryResponse = diaryService.getOneDiary(1L);
		assertThat(diaryResponse, is(oneDiaryResponse));
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
		DiaryRequest request = DiaryRequest.builder()
				.id(newDiary.getId())
				.title(newDiary.getTitle())
				.contents(newDiary.getContents())
				.thumbnail(newDiary.getThumbnail())
				.build();
		DiaryResponse diaryResponse = DiaryResponse.builder()
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
		DiaryResponse updatedDiary = diaryService.updateDiary(request);
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
		diaryService.deleteDiary(diary.getId());
		verify(diaryService, times(1)).deleteDiary(diary.getId());
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
		diaryService.deleteDiary(diary.getId());
		verify(diaryService, times(1)).deleteDiary(diary.getId());
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
