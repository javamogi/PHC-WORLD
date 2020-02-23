package com.phcworld.repository.answer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.DiaryRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class DiaryAnswerRepositoryTest {
	
	@Autowired
	private DiaryRepository diaryRepository;
	
	@Autowired
	private DiaryAnswerRepository diaryAnswerRepository;

	@Test
	public void create() {
		User writer = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		Diary diary = Diary.builder()
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		Diary createdDiary = diaryRepository.save(diary);
		DiaryAnswer answer = DiaryAnswer.builder()
				.writer(writer)
				.diary(createdDiary)
				.contents("diary answer content")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer createdAnswer = diaryAnswerRepository.save(answer);
		assertNotNull(createdAnswer);
	}
	
	@Test
	public void read() {
		User writer = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		Diary diary = Diary.builder()
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		Diary createdDiary = diaryRepository.save(diary);
		DiaryAnswer answer = DiaryAnswer.builder()
				.writer(writer)
				.diary(createdDiary)
				.contents("diary answer content")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer createdAnswer = diaryAnswerRepository.save(answer);
		DiaryAnswer actual = diaryAnswerRepository.getOne(createdAnswer.getId());
		assertThat(actual, is(createdAnswer));
	}
	
	@Test
	public void update() {
		User writer = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		Diary diary = Diary.builder()
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		Diary createdDiary = diaryRepository.save(diary);
		DiaryAnswer answer = DiaryAnswer.builder()
				.writer(writer)
				.diary(createdDiary)
				.contents("diary answer content")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer createdAnswer = diaryAnswerRepository.save(answer);
		createdAnswer.setContents("update content");
		DiaryAnswer updatedAnswer = diaryAnswerRepository.save(createdAnswer);
		assertThat("update content", is(updatedAnswer.getContents()));
	}
	
	@Test
	public void delete() {
		User writer = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		Diary diary = Diary.builder()
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		Diary createdDiary = diaryRepository.save(diary);
		DiaryAnswer answer = DiaryAnswer.builder()
				.writer(writer)
				.diary(createdDiary)
				.contents("diary answer content")
				.createDate(LocalDateTime.now())
				.build();
		DiaryAnswer createdAnswer = diaryAnswerRepository.save(answer);
		assertNotNull(createdAnswer);
		diaryAnswerRepository.delete(createdAnswer);
		Optional<DiaryAnswer> deleteAnswer = diaryAnswerRepository.findById(createdAnswer.getId());
		assertFalse(deleteAnswer.isPresent());
	}

}
