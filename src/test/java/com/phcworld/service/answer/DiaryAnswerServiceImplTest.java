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
				.writer(writer)
				.diary(diary)
				.contents("diary answer content")
				.createDate(LocalDateTime.now())
				.build();
		List<DiaryAnswer> list = new ArrayList<DiaryAnswer>();
		list.add(answer);
		diary.setDiaryAnswers(list);
		when(diaryAnswerService.createDiaryAnswer(writer, diary, "diary answer content"))
		.thenReturn(answer);
		DiaryAnswer diaryAnswer = diaryAnswerService.createDiaryAnswer(writer, diary, "diary answer content");
		assertThat("diary answer content", is(diaryAnswer.getContents()));
		assertThat("[1]", is(diaryAnswer.getDiary().getCountOfAnswer()));
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
		when(diaryAnswerService.createDiaryAnswer(writer, diary, "diary answer content"))
		.thenReturn(answer);
		DiaryAnswer diaryAnswer = diaryAnswerService.createDiaryAnswer(writer, diary, "diary answer content");
		doThrow(MatchNotUserExceptioin.class)
		.when(diaryAnswerService).deleteDiaryAnswer(diaryAnswer.getId(), user, diary.getId());
		diaryAnswerService.deleteDiaryAnswer(diaryAnswer.getId(), user, diary.getId());
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
		
		when(diaryAnswerService.createDiaryAnswer(writer, diary, "diary answer content"))
		.thenReturn(answer);
		DiaryAnswer diaryAnswer = diaryAnswerService.createDiaryAnswer(writer, diary, "diary answer content");
		diaryAnswer.getDiary().getDiaryAnswers().remove(diaryAnswer);
		when(diaryAnswerService.deleteDiaryAnswer(diaryAnswer.getId(), writer, diary.getId()))
		.thenReturn("{\"success\":\"" + diaryAnswer.getDiary().getCountOfAnswer() +"\"}");
		String successStr = diaryAnswerService.deleteDiaryAnswer(diaryAnswer.getId(), writer, diary.getId());
		assertThat("{\"success\":\"\"}", is(successStr));
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
	public void getOneDiaryAnswer() {
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
		when(diaryAnswerService.getOneDiaryAnswer(answer.getId()))
		.thenReturn(answer);
		DiaryAnswer diaryAnswer = diaryAnswerService.getOneDiaryAnswer(answer.getId());
		assertThat(answer, is(diaryAnswer));
	}
}
