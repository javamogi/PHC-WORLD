package com.phcworld.medium.repository.answer;


import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.medium.util.FreeBoardFactory;
import com.phcworld.repository.answer.FreeBoardAnswerRepository;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.freeboard.infrastructure.FreeBoardJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class FreeBoardAnswerRepositoryTest {

	@Autowired
	private FreeBoardJpaRepository freeBoardRepository;

	@Autowired
	private FreeBoardAnswerRepository freeBoardAnswerRepository;

	private UserEntity user;
	private FreeBoardEntity freeBoard;

	@Before
	public void setup(){
		user = UserEntity.builder()
				.id(1L)
				.build();
		freeBoard = freeBoardRepository.save(FreeBoardFactory.getFreeBoardEntity(user));
	}

	@Test
	public void create() {
		FreeBoardAnswer answer = FreeBoardAnswer.builder()
				.writer(user)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		
		FreeBoardAnswer newAnswer = freeBoardAnswerRepository.save(answer);
		assertNotNull(newAnswer);
	}
	
	@Test
	public void read() {
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.writer(user)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		FreeBoardAnswer saveFreeBoardAnswer = freeBoardAnswerRepository.save(freeBoardAnswer);
		List<FreeBoardAnswer> freeBoardAnswerList = freeBoardAnswerRepository.findByWriter(user);
		assertThat(freeBoardAnswerList).contains(saveFreeBoardAnswer);
	}
	
	@Test
	public void update() {
		FreeBoardAnswer answer = FreeBoardAnswer.builder()
				.writer(user)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		FreeBoardAnswer newAnswer = freeBoardAnswerRepository.save(answer);
		FreeBoardAnswer registAnswer = freeBoardAnswerRepository.findById(newAnswer.getId())
				.orElseThrow(NotFoundException::new);
		registAnswer.update("update content");
		FreeBoardAnswer updatedAnswer = freeBoardAnswerRepository.save(registAnswer);
		assertThat("update content").isEqualTo(updatedAnswer.getContents());
	}
	
	@Test
	public void delete() {
		FreeBoardAnswer answer = FreeBoardAnswer.builder()
				.writer(user)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		FreeBoardAnswer newAnswer = freeBoardAnswerRepository.save(answer);
		assertNotNull(newAnswer);
		freeBoardAnswerRepository.delete(newAnswer);
		Optional<FreeBoardAnswer>findAnswer = freeBoardAnswerRepository.findById(newAnswer.getId());
		assertFalse(findAnswer.isPresent());
	}

}
