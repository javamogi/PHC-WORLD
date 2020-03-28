package com.phcworld.service.board;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class FreeBoardServiceImplTest {

	@Mock
	private FreeBoardServiceImpl freeBoardService;
		
	@Test
	public void createFreeBoard() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard board = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		when(freeBoardService.createFreeBoard(user, board)).thenReturn(board);
		FreeBoard freeBoard = freeBoardService.createFreeBoard(user, board);
		assertThat(board, is(freeBoard));
	}
		
	
	@Test
	public void getOneFreeBoard() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard board = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		when(freeBoardService.createFreeBoard(user, board)).thenReturn(board);
		FreeBoard freeBoard = freeBoardService.createFreeBoard(user, board);
		
		when(freeBoardService.getOneFreeBoard(freeBoard.getId())).thenReturn(board);
		FreeBoard actual = freeBoardService.getOneFreeBoard(freeBoard.getId());
		
		assertThat(actual, is(freeBoard));
	}
	
	@Test
	public void updateFreeBoard() throws Exception {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard board = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		when(freeBoardService.createFreeBoard(user, board)).thenReturn(board);
		FreeBoard freeBoard = freeBoardService.createFreeBoard(user, board);
		FreeBoard newBoard = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("new title")
				.contents("new content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		board.update(newBoard);
		
		when(freeBoardService.updateFreeBoard(newBoard)).thenReturn(board);
		FreeBoard updatedfreeBoard = freeBoardService.updateFreeBoard(newBoard);
		
		when(freeBoardService.getOneFreeBoard(freeBoard.getId())).thenReturn(updatedfreeBoard);
		FreeBoard actual = freeBoardService.getOneFreeBoard(freeBoard.getId());
		assertThat(actual, is(updatedfreeBoard));
	}
	
	@Test
	public void deleteFreeBoardEmptyFreeBoardAnswer() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard board = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		freeBoardService.deleteFreeBoard(board);
		verify(freeBoardService, times(1)).deleteFreeBoard(board);
	}
	
	@Test
	public void deleteFreeBoard() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard board = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		FreeBoardAnswer answer = FreeBoardAnswer.builder()
				.writer(user)
				.freeBoard(board)
				.contents("content")
				.build();
		List<FreeBoardAnswer> list = new ArrayList<FreeBoardAnswer>();
		list.add(answer);
		board.setFreeBoardAnswers(list);
		freeBoardService.deleteFreeBoard(board);
		verify(freeBoardService, times(1)).deleteFreeBoard(board);
	}
	
	@Test
	public void addFreeBoardCount() throws Exception {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard board = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		board.addCount();
		when(freeBoardService.addFreeBoardCount(board.getId())).thenReturn(board);
		FreeBoard addedFreeBoardAnswer = freeBoardService.addFreeBoardCount(board.getId());
		assertThat(1, is(addedFreeBoardAnswer.getCount()));
	}
	
	@Test
	public void findFreeBoardListByWriter() throws Exception {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard board = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		FreeBoard board2 = FreeBoard.builder()
				.id(2L)
				.writer(user)
				.title("title2")
				.contents("content2")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		List<FreeBoard> freeBoardList = new ArrayList<FreeBoard>();
		freeBoardList.add(board);
		freeBoardList.add(board2);
		when(freeBoardService.findFreeBoardListByWriter(user)).thenReturn(freeBoardList);
		List<FreeBoard> findFreeBoardList = freeBoardService.findFreeBoardListByWriter(user);
		assertThat(findFreeBoardList, hasItems(board, board2));
	}
	
	@Test
	public void findFreeBoardAllList() throws Exception {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard board = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		User user2 = User.builder()
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard board2 = FreeBoard.builder()
				.id(2L)
				.writer(user2)
				.title("title2")
				.contents("content2")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		List<FreeBoard> freeBoardList = new ArrayList<FreeBoard>();
		freeBoardList.add(board);
		freeBoardList.add(board2);
		when(freeBoardService.findFreeBoardAllList()).thenReturn(freeBoardList);
		List<FreeBoard> findAllfreeBoardList =  freeBoardService.findFreeBoardAllList();
		assertThat(findAllfreeBoardList, hasItems(board, board2));
	}
	
}
