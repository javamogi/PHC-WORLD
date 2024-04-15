package com.phcworld.medium.repository.answer;


import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.medium.util.FreeBoardFactory;
import com.phcworld.answer.infrastructure.FreeBoardAnswerJpaRepository;
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
	private FreeBoardAnswerJpaRepository freeBoardAnswerRepository;

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
		FreeBoardAnswerEntity answer = FreeBoardAnswerEntity.builder()
				.writer(user)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		
		FreeBoardAnswerEntity newAnswer = freeBoardAnswerRepository.save(answer);
		assertNotNull(newAnswer);
	}
	
	@Test
	public void read() {
		FreeBoardAnswerEntity freeBoardAnswer = FreeBoardAnswerEntity.builder()
				.writer(user)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		FreeBoardAnswerEntity saveFreeBoardAnswer = freeBoardAnswerRepository.save(freeBoardAnswer);
		List<FreeBoardAnswerEntity> freeBoardAnswerList = freeBoardAnswerRepository.findByWriter(user);
		assertThat(freeBoardAnswerList).contains(saveFreeBoardAnswer);
	}
	
	@Test
	public void update() {
		FreeBoardAnswerEntity answer = FreeBoardAnswerEntity.builder()
				.writer(user)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		FreeBoardAnswerEntity newAnswer = freeBoardAnswerRepository.save(answer);
		FreeBoardAnswerEntity registAnswer = freeBoardAnswerRepository.findById(newAnswer.getId())
				.orElseThrow(NotFoundException::new);
		registAnswer.update("update content");
		FreeBoardAnswerEntity updatedAnswer = freeBoardAnswerRepository.save(registAnswer);
		assertThat("update content").isEqualTo(updatedAnswer.getContents());
	}
	
	@Test
	public void delete() {
		FreeBoardAnswerEntity answer = FreeBoardAnswerEntity.builder()
				.writer(user)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		FreeBoardAnswerEntity newAnswer = freeBoardAnswerRepository.save(answer);
		assertNotNull(newAnswer);
		freeBoardAnswerRepository.delete(newAnswer);
		Optional<FreeBoardAnswerEntity>findAnswer = freeBoardAnswerRepository.findById(newAnswer.getId());
		assertFalse(findAnswer.isPresent());
	}

}
