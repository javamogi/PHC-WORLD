package com.phcworld.service.answer;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
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
import com.phcworld.domain.api.model.request.FreeBoardAnswerRequest;
import com.phcworld.domain.api.model.response.FreeBoardAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.exception.MatchNotUserExceptioin;
import com.phcworld.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FreeBoardAnswerServiceImplTest {
	
	@Mock
	private FreeBoardAnswerServiceImpl freeBoardAnswerService;

	@Test
	@Transactional
	public void createFreeBoardAnswer() {
		User writer = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(writer)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		FreeBoardAnswerRequest request = FreeBoardAnswerRequest.builder()
				.contents("contents")
				.build();
		FreeBoardAnswer answer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents(request.getContents())
				.build();
		List<FreeBoardAnswer> list = new ArrayList<FreeBoardAnswer>();
		list.add(answer);
		freeBoard.setFreeBoardAnswers(list);
		
		FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = FreeBoardAnswerApiResponse.builder()
				.id(answer.getId())
				.writer(answer.getWriter())
				.contents(answer.getContents())
				.freeBoardId(answer.getFreeBoard().getId())
				.countOfAnswers(answer.getFreeBoard().getCountOfAnswer())
				.updateDate(answer.getFormattedUpdateDate())
				.build();
		
		when(freeBoardAnswerService.create(writer, freeBoard.getId(), request))
		.thenReturn(freeBoardAnswerApiResponse);
		FreeBoardAnswerApiResponse createdFreeBoardAnswerApiResponse = 
				freeBoardAnswerService.create(writer, freeBoard.getId(), request);
		assertThat(request.getContents(), is(createdFreeBoardAnswerApiResponse.getContents()));
		assertThat("[1]", is(createdFreeBoardAnswerApiResponse.getCountOfAnswers()));
		assertThat(freeBoard.getId(), is(createdFreeBoardAnswerApiResponse.getFreeBoardId()));
	}
	
	@Test
	public void successDelete() {
		User writer = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(writer)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		FreeBoardAnswer answer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		List<FreeBoardAnswer> list = new ArrayList<FreeBoardAnswer>();
		list.add(answer);
		freeBoard.setFreeBoardAnswers(list);

		answer.getFreeBoard().getFreeBoardAnswers().remove(answer);
		SuccessResponse response = SuccessResponse.builder()
				.success(answer.getFreeBoard().getCountOfAnswer())
				.build();
		when(freeBoardAnswerService.delete(answer.getId(), writer))
		.thenReturn(response);
		SuccessResponse success = freeBoardAnswerService.delete(answer.getId(), writer);
		assertThat(response, is(success));
	}
	
	@Test(expected = MatchNotUserExceptioin.class)
	@Transactional
	public void deleteFreeBoardAnswerWhenWriterNotMatchUser() throws Exception {
		User writer = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		User user= User.builder()
				.id(2L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(writer)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		FreeBoardAnswer answer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		
		doThrow(MatchNotUserExceptioin.class)
		.when(freeBoardAnswerService).delete(answer.getId(), user);
		freeBoardAnswerService.delete(answer.getId(), user);
	}
	
	@Test
	@Transactional
	public void findFreeBoardAnswerListByWriter() throws Exception {
		User writer = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(writer)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		FreeBoardAnswer answer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		FreeBoardAnswer answer2 = FreeBoardAnswer.builder()
				.id(2L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("content2")
				.build();
		List<FreeBoardAnswer> answerList = new ArrayList<FreeBoardAnswer>();
		answerList.add(answer);
		answerList.add(answer2);
		
		when(freeBoardAnswerService.findFreeBoardAnswerListByWriter(writer)).thenReturn(answerList);
		List<FreeBoardAnswer> findFreeBoardAnswerList = 
				freeBoardAnswerService.findFreeBoardAnswerListByWriter(writer);
		assertThat(findFreeBoardAnswerList, hasItems(answer, answer2));
	}
	
	@Test
	public void readFreeBoardAnswer() {
		User writer = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(writer)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		FreeBoardAnswer answer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		List<FreeBoardAnswer> list = new ArrayList<FreeBoardAnswer>();
		list.add(answer);
		freeBoard.setFreeBoardAnswers(list);

		FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = FreeBoardAnswerApiResponse.builder()
				.id(answer.getId())
				.writer(answer.getWriter())
				.contents(answer.getContents())
				.freeBoardId(answer.getFreeBoard().getId())
				.countOfAnswers(answer.getFreeBoard().getCountOfAnswer())
				.updateDate(answer.getFormattedUpdateDate())
				.build();
		
		when(freeBoardAnswerService.read(answer.getId(), writer))
		.thenReturn(freeBoardAnswerApiResponse);
		FreeBoardAnswerApiResponse createdFreeBoardAnswerApiResponse = 
				freeBoardAnswerService.read(answer.getId(), writer);
		assertThat(freeBoardAnswerApiResponse, is(createdFreeBoardAnswerApiResponse));
	}
	
	@Test
	public void update() {
		User writer = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(writer)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
		FreeBoardAnswer answer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		List<FreeBoardAnswer> list = new ArrayList<FreeBoardAnswer>();
		list.add(answer);
		freeBoard.setFreeBoardAnswers(list);
		
		FreeBoardAnswerRequest request = FreeBoardAnswerRequest.builder()
				.id(answer.getId())
				.contents("update contents")
				.build();
		
		answer.update(request.getContents());
		
		FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = FreeBoardAnswerApiResponse.builder()
				.id(answer.getId())
				.writer(answer.getWriter())
				.contents(answer.getContents())
				.freeBoardId(answer.getFreeBoard().getId())
				.countOfAnswers(answer.getFreeBoard().getCountOfAnswer())
				.updateDate(answer.getFormattedUpdateDate())
				.build();
		
		when(freeBoardAnswerService.update(request, writer))
		.thenReturn(freeBoardAnswerApiResponse);
		FreeBoardAnswerApiResponse updatedFreeBoardAnswerApiResponse = 
				freeBoardAnswerService.update(request, writer);
		assertThat(freeBoardAnswerApiResponse, is(updatedFreeBoardAnswerApiResponse));
	}

}
