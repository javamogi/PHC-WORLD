package com.phcworld.service.answer;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.api.model.request.FreeBoardAnswerRequest;
import com.phcworld.domain.api.model.response.FreeBoardAnswerApiResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.exception.MatchNotUserException;
import com.phcworld.domain.user.User;
import com.phcworld.exception.model.NotMatchUserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class FreeBoardAnswerServiceImplTest {
	
	@Mock
	private FreeBoardAnswerServiceImpl freeBoardAnswerService;

	private User writer;
	private FreeBoard freeBoard;

	@Before
	public void setup(){
		writer = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();

		freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(writer)
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.build();
	}

	@Test
	@Transactional
	public void createFreeBoardAnswer() {
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
		
		FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = FreeBoardAnswerApiResponse.of(answer);
		
		when(freeBoardAnswerService.create(writer, freeBoard.getId(), request))
		.thenReturn(freeBoardAnswerApiResponse);
		FreeBoardAnswerApiResponse createdFreeBoardAnswerApiResponse = 
				freeBoardAnswerService.create(writer, freeBoard.getId(), request);
		assertThat(request.getContents()).isEqualTo(createdFreeBoardAnswerApiResponse.getContents());
		assertThat("[1]").isEqualTo(createdFreeBoardAnswerApiResponse.getCountOfAnswers());
		assertThat(freeBoard.getId()).isEqualTo(createdFreeBoardAnswerApiResponse.getFreeBoardId());
	}
	
	@Test
	public void successDelete() {
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
		assertThat(response).isEqualTo(success);
	}
	
	@Test(expected = NotMatchUserException.class)
	@Transactional
	public void deleteFreeBoardAnswerWhenWriterNotMatchUser() throws Exception {
		User user= User.builder()
				.id(2L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardAnswer answer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		
		doThrow(NotMatchUserException.class)
		.when(freeBoardAnswerService).delete(answer.getId(), user);
		freeBoardAnswerService.delete(answer.getId(), user);
	}
	
	@Test
	@Transactional
	public void findFreeBoardAnswerListByWriter() throws Exception {
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
		assertThat(findFreeBoardAnswerList)
				.contains(answer)
				.contains(answer2);
	}
	
	@Test
	public void readFreeBoardAnswer() {
		FreeBoardAnswer answer = FreeBoardAnswer.builder()
				.id(1L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		List<FreeBoardAnswer> list = new ArrayList<FreeBoardAnswer>();
		list.add(answer);
		freeBoard.setFreeBoardAnswers(list);

		FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = FreeBoardAnswerApiResponse.of(answer);
		
		when(freeBoardAnswerService.read(answer.getId(), writer))
		.thenReturn(freeBoardAnswerApiResponse);
		FreeBoardAnswerApiResponse createdFreeBoardAnswerApiResponse = 
				freeBoardAnswerService.read(answer.getId(), writer);
		assertThat(freeBoardAnswerApiResponse).isEqualTo(createdFreeBoardAnswerApiResponse);
	}
	
	@Test
	public void update() {
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
		
		FreeBoardAnswerApiResponse freeBoardAnswerApiResponse = FreeBoardAnswerApiResponse.of(answer);
		
		when(freeBoardAnswerService.update(request, writer))
		.thenReturn(freeBoardAnswerApiResponse);
		FreeBoardAnswerApiResponse updatedFreeBoardAnswerApiResponse = 
				freeBoardAnswerService.update(request, writer);
		assertThat(freeBoardAnswerApiResponse).isEqualTo(updatedFreeBoardAnswerApiResponse);
	}

}
