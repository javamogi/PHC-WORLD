package com.phcworld.medium.service.board;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.board.dto.FreeBoardRequest;
import com.phcworld.domain.board.dto.FreeBoardResponse;
import com.phcworld.service.board.FreeBoardServiceImpl;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.exception.model.CustomException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FreeBoardServiceImplTest {

	@Mock
	private FreeBoardServiceImpl freeBoardService;

	private UserEntity user;

	@Before
	public void setup(){
		user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
	}
		
	@Test
	public void createFreeBoard() {
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
		assertThat(response).isEqualTo(freeBoardResponse);
	}
		
	
	@Test
	public void getOneFreeBoard() {
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
		
		assertThat(actual).isEqualTo(freeBoardResponse);
	}

	@Test(expected = CustomException.class)
	public void getEmptyFreeBoard() {
		when(freeBoardService.getOneFreeBoard(1L)).thenThrow(new CustomException("400", "게시글이 없습니다."));
		freeBoardService.getOneFreeBoard(1L);
	}
	
	@Test
	public void updateFreeBoard() throws Exception {
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
		
		assertThat(request.getContents()).isEqualTo(updatedfreeBoard.getContents());
	}
	
	@Test
	public void deleteFreeBoardWhenEmptyFreeBoardAnswer() {
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
		assertThat(1).isEqualTo(addedFreeBoardAnswer.getCount());
	}
	
	@Test
	public void findFreeBoardListByWriter() throws Exception {
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
		assertThat(findFreeBoardList)
				.contains(board)
				.contains(board2);
	}
	
	@Test
	public void findFreeBoardAllList() throws Exception {
		UserEntity user2 = UserEntity.builder()
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
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
		assertThat(findAllfreeBoardList)
				.contains(response)
				.contains(response2);
	}
	
}
