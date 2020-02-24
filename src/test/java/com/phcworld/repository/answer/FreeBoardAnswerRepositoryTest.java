package com.phcworld.repository.answer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class FreeBoardAnswerRepositoryTest {
	
	@Autowired
	private FreeBoardAnswerRepository freeBoardAnswerRepository;

	@Test
	public void create() {
		User user = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		FreeBoardAnswer answer = FreeBoardAnswer.builder()
				.writer(user)
				.freeBoard(freeBoard)
				.contents("content")
				.createDate(LocalDateTime.now())
				.build();
		
		FreeBoardAnswer newAnswer = freeBoardAnswerRepository.save(answer);
		assertNotNull(newAnswer);
	}
	
	@Test
	public void read() {
		User user = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.writer(user)
				.freeBoard(freeBoard)
				.contents("content")
				.createDate(LocalDateTime.now())
				.build();
		freeBoardAnswerRepository.save(freeBoardAnswer);
		List<FreeBoardAnswer> freeBoardAnswerList = freeBoardAnswerRepository.findByWriter(user);
		freeBoardAnswerList.stream().forEach(answer -> {
			log.info("freeBoardAnswer contents : {}", answer.getContents());
		});
		assertNotNull(freeBoardAnswerList);
	}
	
	@Test
	public void update() {
		User user = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		FreeBoardAnswer answer = FreeBoardAnswer.builder()
				.writer(user)
				.freeBoard(freeBoard)
				.contents("content")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardAnswer newAnswer = freeBoardAnswerRepository.save(answer);
		FreeBoardAnswer registAnswer = freeBoardAnswerRepository.getOne(newAnswer.getId());
		registAnswer.update("update content");
		FreeBoardAnswer updatedAnswer = freeBoardAnswerRepository.save(registAnswer);
		assertThat("update content", is(updatedAnswer.getContents()));
	}
	
	@Test
	public void delete() {
		User user = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		FreeBoardAnswer answer = FreeBoardAnswer.builder()
				.writer(user)
				.freeBoard(freeBoard)
				.contents("content")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardAnswer newAnswer = freeBoardAnswerRepository.save(answer);
		assertNotNull(newAnswer);
		freeBoardAnswerRepository.delete(newAnswer);
		Optional<FreeBoardAnswer>findAnswer = freeBoardAnswerRepository.findById(newAnswer.getId());
		assertFalse(findAnswer.isPresent());
	}

}
