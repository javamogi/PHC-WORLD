package com.phcworld.service.answer;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
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

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.api.model.response.DiaryAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.user.User;
import com.phcworld.service.answer.DiaryAnswerServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class DiaryAnswerServiceImplTest {
	
	@Mock
	private DiaryAnswerServiceImpl diaryAnswerService;
	
	@Test
	public void createDiaryAnswer() {
		User writer = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer answer = DiaryAnswer.builder()
				.id(1L)
				.writer(writer)
				.diary(diary)
				.contents("diary answer content")
				.createDate(LocalDateTime.now())
				.build();
		List<DiaryAnswer> list = new ArrayList<DiaryAnswer>();
		list.add(answer);
		diary.setDiaryAnswers(list);
		
		DiaryAnswerApiResponse diaryAnswerApiResponse = DiaryAnswerApiResponse.builder()
				.id(answer.getId())
				.writer(answer.getWriter())
				.contents(answer.getContents())
				.diaryId(answer.getDiary().getId())
				.countOfAnswers(answer.getDiary().getCountOfAnswer())
				.createDate(answer.getFormattedCreateDate())
				.build();
				
		
		when(diaryAnswerService.create(writer, diary.getId(), "diary answer content"))
		.thenReturn(diaryAnswerApiResponse);
		DiaryAnswerApiResponse createdDiaryAnswerApiResponse = 
				diaryAnswerService.create(writer, diary.getId(), "diary answer content");
		assertThat(diaryAnswerApiResponse, is(createdDiaryAnswerApiResponse));
	}

	@Test(expected = MatchNotUserExceptioin.class)
	public void deleteDiaryAnswerWhenWriterNotMatchUser() throws Exception {
		User writer = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		User user= User.builder()
				.id(2L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer answer = DiaryAnswer.builder()
				.id(1L)
				.writer(writer)
				.diary(diary)
				.contents("diary answer content")
				.createDate(LocalDateTime.now())
				.build();
		
		doThrow(MatchNotUserExceptioin.class)
		.when(diaryAnswerService).delete(answer.getId(), user);
		diaryAnswerService.delete(answer.getId(), user);
	}
	
	@Test
	public void deleteDiaryAnswer() {
		User writer = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer answer = DiaryAnswer.builder()
				.writer(writer)
				.diary(diary)
				.contents("diary answer content")
				.createDate(LocalDateTime.now())
				.build();
		List<DiaryAnswer> list = new ArrayList<DiaryAnswer>();
		list.add(answer);
		diary.setDiaryAnswers(list);
		
		answer.getDiary().getDiaryAnswers().remove(answer);
		SuccessResponse response = SuccessResponse.builder()
				.success(answer.getDiary().getCountOfAnswer())
				.build();
		when(diaryAnswerService.delete(answer.getId(), writer))
		.thenReturn(response);
		SuccessResponse success = diaryAnswerService.delete(answer.getId(), writer);
		assertThat(response, is(success));
	}

	@Test
	public void findDiaryAnswerListByWriter() throws Exception {
		User writer = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer answer = DiaryAnswer.builder()
				.writer(writer)
				.diary(diary)
				.contents("diary answer content")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer answer2 = DiaryAnswer.builder()
				.writer(writer)
				.diary(diary)
				.contents("diary answer content2")
				.createDate(LocalDateTime.now())
				.build();
		List<DiaryAnswer> list = new ArrayList<DiaryAnswer>();
		list.add(answer);
		list.add(answer2);
		when(diaryAnswerService.findDiaryAnswerListByWriter(writer))
		.thenReturn(list);
		List<DiaryAnswer> diaryAnswerList = diaryAnswerService.findDiaryAnswerListByWriter(writer);
		assertThat(diaryAnswerList, hasItems(answer, answer2));
	}
	
	@Test
	public void readDiaryAnswer() {
		User writer = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer answer = DiaryAnswer.builder()
				.writer(writer)
				.diary(diary)
				.contents("diary answer content")
				.createDate(LocalDateTime.now())
				.build();
		List<DiaryAnswer> list = new ArrayList<DiaryAnswer>();
		list.add(answer);
		diary.setDiaryAnswers(list);
		
		DiaryAnswerApiResponse diaryAnswerApiResponse = DiaryAnswerApiResponse.builder()
				.id(answer.getId())
				.writer(answer.getWriter())
				.contents(answer.getContents())
				.diaryId(answer.getDiary().getId())
				.countOfAnswers(answer.getDiary().getCountOfAnswer())
				.createDate(answer.getFormattedCreateDate())
				.build();
		
		when(diaryAnswerService.read(answer.getId(), writer))
		.thenReturn(diaryAnswerApiResponse);
		DiaryAnswerApiResponse createdDiaryAnswerApiResponse = 
				diaryAnswerService.read(answer.getId(), writer);
		assertThat(diaryAnswerApiResponse, is(createdDiaryAnswerApiResponse));
	}
	
	@Test
	public void update() {
		User writer = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer answer = DiaryAnswer.builder()
				.writer(writer)
				.diary(diary)
				.contents("diary answer content")
				.createDate(LocalDateTime.now())
				.build();
		List<DiaryAnswer> list = new ArrayList<DiaryAnswer>();
		list.add(answer);
		diary.setDiaryAnswers(list);
		
		answer.update("diary answer update content");
		
		DiaryAnswerApiResponse diaryAnswerApiResponse = DiaryAnswerApiResponse.builder()
				.id(answer.getId())
				.writer(answer.getWriter())
				.contents(answer.getContents())
				.diaryId(answer.getDiary().getId())
				.countOfAnswers(answer.getDiary().getCountOfAnswer())
				.createDate(answer.getFormattedCreateDate())
				.build();
		
		when(diaryAnswerService.update(answer.getId(), answer.getContents(), writer))
		.thenReturn(diaryAnswerApiResponse);
		DiaryAnswerApiResponse updatedDiaryAnswerApiResponse = 
				diaryAnswerService.update(answer.getId(), answer.getContents(), writer);
		assertThat(diaryAnswerApiResponse, is(updatedDiaryAnswerApiResponse));
	}
	
}
