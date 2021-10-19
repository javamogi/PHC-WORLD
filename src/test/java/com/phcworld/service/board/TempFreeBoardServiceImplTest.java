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

import com.phcworld.domain.answer.TempFreeBoardAnswer;
import com.phcworld.domain.board.TempFreeBoard;
import com.phcworld.domain.board.TempFreeBoardRequest;
import com.phcworld.domain.board.TempFreeBoardResponse;
import com.phcworld.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class TempFreeBoardServiceImplTest {

	@Mock
	private TempFreeBoardServiceImpl freeBoardService;
		
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
		TempFreeBoardRequest request = TempFreeBoardRequest.builder()
				.title("title")
				.contents("contents")
				.icon("")
				.build();
		TempFreeBoard board = TempFreeBoard.builder()
				.id(1L)
				.writer(user)
				.title(request.getTitle())
				.contents(request.getContents())
				.icon(request.getIcon())
				.badge("new")
				.count(0)
				.build();
		TempFreeBoardResponse response = TempFreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title(board.getTitle())
				.contents(board.getContents())
				.icon(board.getIcon())
				.badge(board.getBadge())
				.count(board.getCount())
				.build();
		when(freeBoardService.createFreeBoard(user, request)).thenReturn(response);
		TempFreeBoardResponse freeBoardResponse = freeBoardService.createFreeBoard(user, request);
		assertThat(response, is(freeBoardResponse));
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
		TempFreeBoardRequest request = TempFreeBoardRequest.builder()
				.title("title")
				.contents("contents")
				.icon("")
				.build();
		TempFreeBoard board = TempFreeBoard.builder()
				.id(1L)
				.writer(user)
				.title(request.getTitle())
				.contents(request.getContents())
				.icon(request.getIcon())
				.badge("new")
				.count(0)
				.build();
		TempFreeBoardResponse response = TempFreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title(board.getTitle())
				.contents(board.getContents())
				.icon(board.getIcon())
				.badge(board.getBadge())
				.count(board.getCount())
				.build();
		when(freeBoardService.createFreeBoard(user, request)).thenReturn(response);
		TempFreeBoardResponse freeBoardResponse = freeBoardService.createFreeBoard(user, request);
		
		when(freeBoardService.getOneFreeBoard(response.getId())).thenReturn(response);
		TempFreeBoardResponse actual = freeBoardService.getOneFreeBoard(response.getId());
		
		assertThat(actual, is(freeBoardResponse));
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
		TempFreeBoard board = TempFreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		
		TempFreeBoardRequest request = TempFreeBoardRequest.builder()
				.id(1L)
				.contents("new contents")
				.icon("")
				.build();
		board.update(request);
		
		TempFreeBoardResponse response = TempFreeBoardResponse.builder()
				.id(1L)
				.writer(board.getWriter())
				.title(board.getTitle())
				.contents(board.getContents())
				.icon(board.getIcon())
				.badge(board.getBadge())
				.count(board.getCount())
				.countOfAnswer(board.getCountOfAnswer())
				.createDate(board.getFormattedCreateDate())
				.build();
		
		when(freeBoardService.updateFreeBoard(request)).thenReturn(response);
		TempFreeBoardResponse updatedfreeBoard = freeBoardService.updateFreeBoard(request);
		
		assertThat(request.getContents(), is(updatedfreeBoard.getContents()));		
	}
	
	@Test
	public void deleteFreeBoardWhenEmptyFreeBoardAnswer() {
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		TempFreeBoard board = TempFreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		freeBoardService.deleteFreeBoard(board.getId());
		verify(freeBoardService, times(1)).deleteFreeBoard(board.getId());
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
		TempFreeBoard board = TempFreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		TempFreeBoardAnswer answer = TempFreeBoardAnswer.builder()
				.writer(user)
				.tempFreeBoard(board)
				.contents("content")
				.build();
		List<TempFreeBoardAnswer> list = new ArrayList<TempFreeBoardAnswer>();
		list.add(answer);
		board.setTempFreeBoardAnswers(list);
		freeBoardService.deleteFreeBoard(board.getId());
		verify(freeBoardService, times(1)).deleteFreeBoard(board.getId());
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
		TempFreeBoard board = TempFreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		board.addCount();
		TempFreeBoardResponse response = TempFreeBoardResponse.builder()
				.id(1L)
				.writer(board.getWriter())
				.title(board.getTitle())
				.contents(board.getContents())
				.icon(board.getIcon())
				.badge(board.getBadge())
				.count(board.getCount())
				.countOfAnswer(board.getCountOfAnswer())
				.createDate(board.getFormattedCreateDate())
				.build();
		when(freeBoardService.addFreeBoardCount(board.getId())).thenReturn(response);
		TempFreeBoardResponse addedFreeBoardAnswer = freeBoardService.addFreeBoardCount(board.getId());
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
		TempFreeBoard board = TempFreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		TempFreeBoard board2 = TempFreeBoard.builder()
				.id(2L)
				.writer(user)
				.title("title2")
				.contents("content2")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		List<TempFreeBoard> freeBoardList = new ArrayList<TempFreeBoard>();
		freeBoardList.add(board);
		freeBoardList.add(board2);
		
		when(freeBoardService.findFreeBoardListByWriter(user)).thenReturn(freeBoardList);
		List<TempFreeBoard> findFreeBoardList = freeBoardService.findFreeBoardListByWriter(user);
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
		User user2 = User.builder()
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		TempFreeBoardResponse response = TempFreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		TempFreeBoardResponse response2 = TempFreeBoardResponse.builder()
				.id(2L)
				.writer(user2)
				.title("title2")
				.contents("content2")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		List<TempFreeBoardResponse> list = new ArrayList<TempFreeBoardResponse>();
		list.add(response);
		list.add(response2);
		when(freeBoardService.findFreeBoardAllListAndSetNewBadge()).thenReturn(list);
		List<TempFreeBoardResponse> findAllfreeBoardList =  freeBoardService.findFreeBoardAllListAndSetNewBadge();
		assertThat(findAllfreeBoardList, hasItems(response, response2));
	}
	
}
