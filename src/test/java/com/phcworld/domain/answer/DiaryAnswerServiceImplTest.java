package com.phcworld.domain.answer;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryServiceImpl;
import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.user.User;
import com.phcworld.service.user.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DiaryAnswerServiceImplTest {
	
	@Autowired
	private DiaryAnswerServiceImpl diaryAnswerService;
	
	@Autowired
	private DiaryServiceImpl diaryService;
	
	@Autowired
	private UserService userService;
	
	@Test
	@Transactional
	public void createDiaryAnswer() {
		User writer = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		writer.setId(1L);;
		Diary diary = diaryService.createDiary(writer, "test", "test", "no-image-icon.gif");
		DiaryAnswer diaryAnswer = diaryAnswerService.createDiaryAnswer(writer, diary, "test");
		DiaryAnswer actual = diaryAnswerService.getOneDiaryAnswer(diaryAnswer.getId());
		assertThat(actual, is(diaryAnswer));
	}

	@Test(expected = MatchNotUserExceptioin.class)
	public void deleteDiaryAnswerWriterNotMatchUser() throws Exception {
		User writer = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		writer.setId(1L);;
		User user= User.builder()
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		user.setId(2L);
		Diary diary = diaryService.createDiary(writer, "test", "test", "no-image-icon.gif");
		DiaryAnswer diaryAnswer = diaryAnswerService.createDiaryAnswer(writer, diary, "test");
		diaryAnswerService.deleteDiaryAnswer(diaryAnswer.getId(), user, diary.getId());
	}

	@Test(expected = JpaObjectRetrievalFailureException.class)
	@Transactional
	public void deleteDiaryAnswer() throws Exception {
		User user = User.builder()
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		User writer = userService.createUser(user);
		Diary diary = diaryService.createDiary(writer, "test", "test", "no-image-icon.gif");
		DiaryAnswer diaryAnswer = diaryAnswerService.createDiaryAnswer(writer, diary, "test");
		Long id = diaryAnswer.getId();
		diaryAnswerService.deleteDiaryAnswer(id, writer, diary.getId());
		assertNull(diaryAnswerService.getOneDiaryAnswer(id));
	}
	
	@Test
	@Transactional
	public void findDiaryAnswerListByWriter() throws Exception {
		User writer = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		writer.setId(1L);;
		Diary diary = diaryService.createDiary(writer, "test", "test", "no-image-icon.gif");
		DiaryAnswer diaryAnswer = diaryAnswerService.createDiaryAnswer(writer, diary, "test");
		DiaryAnswer diaryAnswer2 = diaryAnswerService.createDiaryAnswer(writer, diary, "test2");
		List<DiaryAnswer> diaryAnswerList = diaryAnswerService.findDiaryAnswerListByWriter(writer);
		assertThat(diaryAnswerList, hasItems(diaryAnswer, diaryAnswer2));
	}
}
