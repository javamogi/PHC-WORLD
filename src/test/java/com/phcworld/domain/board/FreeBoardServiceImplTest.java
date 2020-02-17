package com.phcworld.domain.board;

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

import com.phcworld.domain.user.User;
import com.phcworld.service.user.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FreeBoardServiceImplTest {

	@Autowired
	private FreeBoardServiceImpl freeBoardService;
		
	@Autowired
	private UserService userService;
	
	@Test
	@Transactional
	public void createFreeBoard() {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		FreeBoard freeBoard = freeBoardService.createFreeBoard(user, "test", "test", "");
		FreeBoard actual = freeBoardService.getOneFreeBoard(freeBoard.getId());
		assertThat(actual, is(freeBoard));
	}
	
	@Test
	@Transactional
	public void updateFreeBoard() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		FreeBoard freeBoard = freeBoardService.createFreeBoard(user, "test", "test", "");
		FreeBoard freeBoardUpdated = freeBoardService.updateFreeBoard(freeBoard, "testUpdate", "test.jpg");
		FreeBoard actual = freeBoardService.getOneFreeBoard(freeBoard.getId());
		assertThat(actual, is(freeBoardUpdated));
	}
	
	@Test(expected = JpaObjectRetrievalFailureException.class)
	@Transactional
	public void deleteFreeBoardById() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		FreeBoard freeBoard = freeBoardService.createFreeBoard(user, "test", "test", "");
		Long id = freeBoard.getId();
		freeBoardService.deleteFreeBoardById(id);
		freeBoardService.getOneFreeBoard(id);
	}
	
	@Test
	@Transactional
	public void addFreeBoardCount() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		FreeBoard freeBoard = freeBoardService.createFreeBoard(user, "test", "test", "");
		FreeBoard addedFreeBoardAnswer = freeBoardService.addFreeBoardCount(freeBoard.getId());
		assertThat(1, is(addedFreeBoardAnswer.getCount()));
	}
	
	@Test
	@Transactional
	public void addFreeBoardAnswerCount() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		FreeBoard freeBoard = freeBoardService.createFreeBoard(user, "test", "test", "");
		FreeBoard addedFreeBoardAnswer = freeBoardService.addFreeBoardAnswer(freeBoard.getId());
		assertThat("[1]", is(addedFreeBoardAnswer.getCountOfAnswer()));
	}
	
	@Test
	@Transactional
	public void findFreeBoardListByWriter() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		user.setId(1L);
		FreeBoard freeBoard = freeBoardService.createFreeBoard(user, "test", "test", "");
		FreeBoard freeBoard2 = freeBoardService.createFreeBoard(user, "test2", "test2", "");
		List<FreeBoard> freeBoardList = freeBoardService.findFreeBoardListByWriter(user);
		assertThat(freeBoardList, hasItems(freeBoard, freeBoard2));
	}
	
	@Test
	@Transactional
	public void findFreeBoardAllList() throws Exception {
		User user = new User("test3@test.test", "test3", "테스트3");
		userService.createUser(user);
		FreeBoard freeBoard = freeBoardService.createFreeBoard(user, "test", "test", "");
		FreeBoard freeBoard2 = freeBoardService.createFreeBoard(user, "test2", "test2", "");
		User user2 = new User("test4@test.test", "test4", "테스트4");
		userService.createUser(user2);
		FreeBoard freeBoard3 = freeBoardService.createFreeBoard(user2, "test3", "test3", "");
		FreeBoard freeBoard4 = freeBoardService.createFreeBoard(user2, "test4", "test4", "");
		List<FreeBoard> freeBoardList =  freeBoardService.findFreeBoardAllList();
		assertThat(freeBoardList, hasItems(freeBoard, freeBoard2, freeBoard3, freeBoard4));
	}

}
