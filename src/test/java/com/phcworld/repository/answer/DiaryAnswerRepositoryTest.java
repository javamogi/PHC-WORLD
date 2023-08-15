package com.phcworld.repository.answer;


import java.time.LocalDateTime;
import java.util.Optional;

import com.phcworld.exception.model.CustomException;
import com.phcworld.util.DiaryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class DiaryAnswerRepositoryTest {
	
	@Autowired
	private DiaryRepository diaryRepository;
	
	@Autowired
	private DiaryAnswerRepository diaryAnswerRepository;

	private User user;
	private Diary diary;

	@Before
	public void setup(){
		user = User.builder()
				.id(1L)
				.build();
		diary = diaryRepository.save(DiaryFactory.getDiaryEntity(user));
	}

	@Test
	public void create() {
		DiaryAnswer answer = DiaryAnswer.builder()
				.writer(user)
				.diary(diary)
				.contents("diary answer content")
				.build();
		DiaryAnswer createdAnswer = diaryAnswerRepository.save(answer);
		assertNotNull(createdAnswer);
	}
	
	@Test
	public void read() {
		DiaryAnswer answer = DiaryAnswer.builder()
				.writer(user)
				.diary(diary)
				.contents("diary answer content")
				.build();
		DiaryAnswer createdAnswer = diaryAnswerRepository.save(answer);
		DiaryAnswer actual = diaryAnswerRepository.findById(createdAnswer.getId())
				.orElseThrow(() -> new CustomException("400", "게시물이 존재하지 않습니다."));
		assertThat(actual).isEqualTo(createdAnswer);
	}

	@Test(expected = CustomException.class)
	public void read_throw_exception() {
		diaryAnswerRepository.findById(1L)
				.orElseThrow(() -> new CustomException("400", "게시물이 존재하지 않습니다."));
	}
	
	@Test
	public void update() {
		DiaryAnswer answer = DiaryAnswer.builder()
				.writer(user)
				.diary(diary)
				.contents("diary answer content")
				.build();
		DiaryAnswer createdAnswer = diaryAnswerRepository.save(answer);
		createdAnswer.setContents("update content");
		DiaryAnswer updatedAnswer = diaryAnswerRepository.save(createdAnswer);
		assertThat("update content").isEqualTo(updatedAnswer.getContents());
	}
	
	@Test
	public void delete() {
		DiaryAnswer answer = DiaryAnswer.builder()
				.writer(user)
				.diary(diary)
				.contents("diary answer content")
				.build();
		DiaryAnswer createdAnswer = diaryAnswerRepository.save(answer);
		assertNotNull(createdAnswer);
		diaryAnswerRepository.delete(createdAnswer);
		Optional<DiaryAnswer> deleteAnswer = diaryAnswerRepository.findById(createdAnswer.getId());
		assertFalse(deleteAnswer.isPresent());
	}

}
