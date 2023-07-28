package com.phcworld.service.board;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.board.FreeBoardRequest;
import com.phcworld.domain.board.FreeBoardResponse;
import com.phcworld.domain.user.User;
import com.phcworld.exception.model.CustomException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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
		FreeBoardRequest request = FreeBoardRequest.builder()
				.title("title")
				.contents("contents")
				.icon("")
				.build();
		FreeBoard board = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title(request.getTitle())
				.contents(request.getContents())
				.icon(request.getIcon())
				.badge("new")
				.count(0)
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title(board.getTitle())
				.contents(board.getContents())
				.icon(board.getIcon())
				.badge(board.getBadge())
				.count(board.getCount())
				.build();
		when(freeBoardService.createFreeBoard(user, request)).thenReturn(response);
		FreeBoardResponse freeBoardResponse = freeBoardService.createFreeBoard(user, request);
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
		FreeBoardRequest request = FreeBoardRequest.builder()
				.title("title")
				.contents("contents")
				.icon("")
				.build();
		FreeBoard board = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title(request.getTitle())
				.contents(request.getContents())
				.icon(request.getIcon())
				.badge("new")
				.count(0)
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title(board.getTitle())
				.contents(board.getContents())
				.icon(board.getIcon())
				.badge(board.getBadge())
				.count(board.getCount())
				.build();
		when(freeBoardService.createFreeBoard(user, request)).thenReturn(response);
		FreeBoardResponse freeBoardResponse = freeBoardService.createFreeBoard(user, request);
		
		when(freeBoardService.getOneFreeBoard(response.getId())).thenReturn(response);
		FreeBoardResponse actual = freeBoardService.getOneFreeBoard(response.getId());
		
		assertThat(actual, is(freeBoardResponse));
	}

	@Test(expected = CustomException.class)
	public void getEmptyFreeBoard() {
		when(freeBoardService.getOneFreeBoard(1L)).thenThrow(new CustomException("400", "게시글이 없습니다."));
		freeBoardService.getOneFreeBoard(1L);
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
				.createDate(LocalDateTime.now())
				.build();
		
		FreeBoardRequest request = FreeBoardRequest.builder()
				.id(1L)
				.contents("new contents")
				.icon("")
				.build();
		board.update(request);
		
		FreeBoardResponse response = FreeBoardResponse.builder()
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
		FreeBoardResponse updatedfreeBoard = freeBoardService.updateFreeBoard(request);
		
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
		FreeBoard board = FreeBoard.builder()
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
		FreeBoard board = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.createDate(LocalDateTime.now())
				.build();
		board.addCount();
		FreeBoardResponse response = FreeBoardResponse.builder()
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
		FreeBoardResponse addedFreeBoardAnswer = freeBoardService.addFreeBoardCount(board.getId());
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
		User user2 = User.builder()
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardResponse response = FreeBoardResponse.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		FreeBoardResponse response2 = FreeBoardResponse.builder()
				.id(2L)
				.writer(user2)
				.title("title2")
				.contents("content2")
				.icon("")
				.badge("new")
				.count(0)
				.build();
		List<FreeBoardResponse> list = new ArrayList<FreeBoardResponse>();
		list.add(response);
		list.add(response2);
		when(freeBoardService.findFreeBoardAllListAndSetNewBadge()).thenReturn(list);
		List<FreeBoardResponse> findAllfreeBoardList =  freeBoardService.findFreeBoardAllListAndSetNewBadge();
		assertThat(findAllfreeBoardList, hasItems(response, response2));
	}
	
}
