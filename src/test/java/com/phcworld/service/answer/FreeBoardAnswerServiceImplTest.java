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
		FreeBoardAnswer answer = FreeBoardAnswer.builder()
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("content")
				.createDate(LocalDateTime.now())
				.build();
		List<FreeBoardAnswer> list = new ArrayList<FreeBoardAnswer>();
		list.add(answer);
		freeBoard.setFreeBoardAnswers(list);
		
		when(freeBoardAnswerService.createFreeBoardAnswer(writer, freeBoard.getId(), "content")).thenReturn(answer);
		FreeBoardAnswer freeBoardAnswer = 
				freeBoardAnswerService.createFreeBoardAnswer(writer, freeBoard.getId(), "content");
		assertThat("content", is(freeBoardAnswer.getContents()));
		assertThat("[1]", is(freeBoardAnswer.getFreeBoard().getCountOfAnswer()));
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
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("content")
				.createDate(LocalDateTime.now())
				.build();
		List<FreeBoardAnswer> list = new ArrayList<FreeBoardAnswer>();
		list.add(answer);
		freeBoard.setFreeBoardAnswers(list);
		
		when(freeBoardAnswerService.createFreeBoardAnswer(writer, freeBoard.getId(), "content")).thenReturn(answer);
		FreeBoardAnswer freeBoardAnswer = 
				freeBoardAnswerService.createFreeBoardAnswer(writer, freeBoard.getId(), "content");
		
		freeBoardAnswer.getFreeBoard().getFreeBoardAnswers().remove(answer);
		when(freeBoardAnswerService.deleteFreeBoardAnswer(freeBoardAnswer.getId(), writer))
		.thenReturn("{\"success\":\"" + freeBoardAnswer.getFreeBoard().getCountOfAnswer() +"\"}");
		String successStr = freeBoardAnswerService.deleteFreeBoardAnswer(freeBoardAnswer.getId(), writer);
		assertThat("{\"success\":\"\"}", is(successStr));
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
				.createDate(LocalDateTime.now())
				.build();
		when(freeBoardAnswerService.createFreeBoardAnswer(writer, freeBoard.getId(), "content")).thenReturn(answer);
		FreeBoardAnswer freeBoardAnswer = 
				freeBoardAnswerService.createFreeBoardAnswer(writer, freeBoard.getId(), "content");
		doThrow(MatchNotUserExceptioin.class)
		.when(freeBoardAnswerService).deleteFreeBoardAnswer(freeBoardAnswer.getId(), user);
		freeBoardAnswerService.deleteFreeBoardAnswer(freeBoardAnswer.getId(), user);
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
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardAnswer answer2 = FreeBoardAnswer.builder()
				.id(2L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("content2")
				.createDate(LocalDateTime.now())
				.build();
		List<FreeBoardAnswer> answerList = new ArrayList<FreeBoardAnswer>();
		answerList.add(answer);
		answerList.add(answer2);
		
		when(freeBoardAnswerService.findFreeBoardAnswerListByWriter(writer)).thenReturn(answerList);
		List<FreeBoardAnswer> findFreeBoardAnswerList = freeBoardAnswerService.findFreeBoardAnswerListByWriter(writer);
		assertThat(findFreeBoardAnswerList, hasItems(answer, answer2));
	}

}
