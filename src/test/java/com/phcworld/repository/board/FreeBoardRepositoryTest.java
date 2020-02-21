package com.phcworld.repository.board;

import static org.hamcrest.CoreMatchers.hasItems;
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

import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;
import com.phcworld.repository.user.UserRepository;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class FreeBoardRepositoryTest {
	
	@Autowired
	private FreeBoardRepository freeBoardRepository;
	
	@Test
	public void create() {
		User user = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.countOfAnswer(0)
				.build();
		log.info("freeBoard : {}", freeBoard);
		FreeBoard newFreeBoard =  freeBoardRepository.save(freeBoard);
		assertNotNull(newFreeBoard);
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
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.countOfAnswer(0)
				.build();
		freeBoardRepository.save(freeBoard);
		List<FreeBoard> freeBoardList = freeBoardRepository.findByWriter(user);
		freeBoardList.stream().forEach(board -> {
			log.info("freeBoard title : {}", board.getTitle());
		});
		assertThat(freeBoardList, hasItems(freeBoard));
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
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.countOfAnswer(0)
				.build();
		FreeBoard newBoard = freeBoardRepository.save(freeBoard);
		FreeBoard regitBoard = freeBoardRepository.getOne(newBoard.getId());
		regitBoard.update("modify content", "");
		FreeBoard updatedBoard = freeBoardRepository.save(regitBoard);
		assertThat("modify content", is(updatedBoard.getContents()));
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
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.countOfAnswer(0)
				.build();
		FreeBoard newBoard = freeBoardRepository.save(freeBoard);
		assertNotNull(newBoard);
		freeBoardRepository.delete(newBoard);
		Optional<FreeBoard> findBoard = freeBoardRepository.findById(newBoard.getId());
		assertFalse(findBoard.isPresent());
	}

}
