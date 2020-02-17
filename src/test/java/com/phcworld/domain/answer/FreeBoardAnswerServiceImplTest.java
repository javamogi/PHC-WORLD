package com.phcworld.domain.answer;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.board.FreeBoardServiceImpl;
import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.user.User;
import com.phcworld.service.user.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FreeBoardAnswerServiceImplTest {
	
	@Autowired
	private FreeBoardServiceImpl freeBoardService;
	
	@Autowired
	private FreeBoardAnswerServiceImpl freeBoardAnswerService;
	
	@Autowired
	private UserService userService;

	@Test
	@Transactional
	public void createFreeBoardAnswer() {
		User writer = new User("test@test.test", "test", "테스트");
		writer.setId(1L);
		FreeBoard freeBoard = freeBoardService.createFreeBoard(writer, "test", "test", "");
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerService.createFreeBoardAnswer(writer, freeBoard, "test");
		FreeBoardAnswer actual = freeBoardAnswerService.getOneFreeBoardAnswer(freeBoardAnswer.getId());
		assertThat(actual, is(freeBoardAnswer));
	}
	
	@Test(expected = MatchNotUserExceptioin.class)
	@Transactional
	public void deleteFreeBoardAnswerWriterNotMatchUser() throws Exception {
		User writer = new User("test@test.test", "test", "테스트");
		writer.setId(1L);;
		User user= new User("test3@test,tset", "test3", "테스트3");
		user.setId(2L);
		FreeBoard freeBoard = freeBoardService.createFreeBoard(writer, "test", "test", "");
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerService.createFreeBoardAnswer(writer, freeBoard, "test");
		freeBoardAnswerService.deleteFreeBoardAnswer(freeBoardAnswer.getId(), user, freeBoard.getId());
	}
	
	@Test(expected = JpaObjectRetrievalFailureException.class)
	@Transactional
	public void deleteFreeBoardAnswer() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		User writer = userService.createUser(user);
		FreeBoard freeBoard = freeBoardService.createFreeBoard(writer, "test", "test", "");
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerService.createFreeBoardAnswer(writer, freeBoard, "test");
		Long id = freeBoardAnswer.getId();
		freeBoardAnswerService.deleteFreeBoardAnswer(freeBoardAnswer.getId(), user, freeBoard.getId());
		assertNull(freeBoardAnswerService.getOneFreeBoardAnswer(id));
	}
	
	@Test
	@Transactional
	public void findFreeBoardAnswerListByWriter() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		User writer = userService.createUser(user);
		FreeBoard freeBoard = freeBoardService.createFreeBoard(writer, "test", "test", "");
		FreeBoardAnswer freeBoardAnswer = freeBoardAnswerService.createFreeBoardAnswer(writer, freeBoard, "test");
		FreeBoardAnswer freeBoardAnswer2 = freeBoardAnswerService.createFreeBoardAnswer(writer, freeBoard, "test2");
		List<FreeBoardAnswer> freeBoardAnswerList = freeBoardAnswerService.findFreeBoardAnswerListByWriter(writer);
		assertThat(freeBoardAnswerList, hasItems(freeBoardAnswer, freeBoardAnswer2));
	}

}
