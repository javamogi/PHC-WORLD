package com.phcworld.medium.service.answer;

import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.answer.controller.port.FreeBoardAnswerResponse;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.answer.service.FreeBoardAnswerServiceImpl;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
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

	private UserEntity writer;
	private FreeBoardEntity freeBoard;

	@Before
	public void setup(){
		writer = UserEntity.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();

		freeBoard = FreeBoardEntity.builder()
				.id(1L)
				.writer(writer)
				.title("title")
				.contents("content")
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
		FreeBoardAnswerEntity answer = FreeBoardAnswerEntity.builder()
				.id(1L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents(request.getContents())
				.build();
		List<FreeBoardAnswerEntity> list = new ArrayList<FreeBoardAnswerEntity>();
		list.add(answer);
		freeBoard.setFreeBoardAnswers(list);
		
		FreeBoardAnswerResponse freeBoardAnswerApiResponse = FreeBoardAnswerResponse.of(answer);
		
		when(freeBoardAnswerService.create(writer, freeBoard.getId(), request))
		.thenReturn(freeBoardAnswerApiResponse);
		FreeBoardAnswerResponse createdFreeBoardAnswerApiResponse =
				freeBoardAnswerService.create(writer, freeBoard.getId(), request);
		assertThat(request.getContents()).isEqualTo(createdFreeBoardAnswerApiResponse.getContents());
		assertThat("[1]").isEqualTo(createdFreeBoardAnswerApiResponse.getCountOfAnswers());
		assertThat(freeBoard.getId()).isEqualTo(createdFreeBoardAnswerApiResponse.getFreeBoardId());
	}
	
	@Test
	public void successDelete() {
		FreeBoardAnswerEntity answer = FreeBoardAnswerEntity.builder()
				.id(1L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		List<FreeBoardAnswerEntity> list = new ArrayList<FreeBoardAnswerEntity>();
		list.add(answer);
		freeBoard.setFreeBoardAnswers(list);

		answer.getFreeBoard().getFreeBoardAnswers().remove(answer);
		SuccessResponse response = SuccessResponse.builder()
				.success("삭제성공")
				.build();
		when(freeBoardAnswerService.delete(answer.getId(), writer))
		.thenReturn(response);
		SuccessResponse success = freeBoardAnswerService.delete(answer.getId(), writer);
		assertThat(response).isEqualTo(success);
	}
	
	@Test(expected = NotMatchUserException.class)
	@Transactional
	public void deleteFreeBoardAnswerWhenWriterNotMatchUser() throws Exception {
		UserEntity user= UserEntity.builder()
				.id(2L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		FreeBoardAnswerEntity answer = FreeBoardAnswerEntity.builder()
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
		FreeBoardAnswerEntity answer = FreeBoardAnswerEntity.builder()
				.id(1L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		FreeBoardAnswerEntity answer2 = FreeBoardAnswerEntity.builder()
				.id(2L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("content2")
				.build();
		List<FreeBoardAnswerEntity> answerList = new ArrayList<FreeBoardAnswerEntity>();
		answerList.add(answer);
		answerList.add(answer2);
		
		when(freeBoardAnswerService.findFreeBoardAnswerListByWriter(writer)).thenReturn(answerList);
		List<FreeBoardAnswerEntity> findFreeBoardAnswerList =
				freeBoardAnswerService.findFreeBoardAnswerListByWriter(writer);
		assertThat(findFreeBoardAnswerList)
				.contains(answer)
				.contains(answer2);
	}
	
	@Test
	public void readFreeBoardAnswer() {
		FreeBoardAnswerEntity answer = FreeBoardAnswerEntity.builder()
				.id(1L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		List<FreeBoardAnswerEntity> list = new ArrayList<FreeBoardAnswerEntity>();
		list.add(answer);
		freeBoard.setFreeBoardAnswers(list);

		FreeBoardAnswerResponse freeBoardAnswerApiResponse = FreeBoardAnswerResponse.of(answer);
		
		when(freeBoardAnswerService.read(answer.getId(), writer))
		.thenReturn(freeBoardAnswerApiResponse);
		FreeBoardAnswerResponse createdFreeBoardAnswerApiResponse =
				freeBoardAnswerService.read(answer.getId(), writer);
		assertThat(freeBoardAnswerApiResponse).isEqualTo(createdFreeBoardAnswerApiResponse);
	}
	
	@Test
	public void update() {
		FreeBoardAnswerEntity answer = FreeBoardAnswerEntity.builder()
				.id(1L)
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("content")
				.build();
		List<FreeBoardAnswerEntity> list = new ArrayList<FreeBoardAnswerEntity>();
		list.add(answer);
		freeBoard.setFreeBoardAnswers(list);
		
		FreeBoardAnswerRequest request = FreeBoardAnswerRequest.builder()
				.id(answer.getId())
				.contents("update contents")
				.build();
		
		answer.update(request.getContents());
		
		FreeBoardAnswerResponse freeBoardAnswerApiResponse = FreeBoardAnswerResponse.of(answer);
		
		when(freeBoardAnswerService.update(request, writer))
		.thenReturn(freeBoardAnswerApiResponse);
		FreeBoardAnswerResponse updatedFreeBoardAnswerApiResponse =
				freeBoardAnswerService.update(request, writer);
		assertThat(freeBoardAnswerApiResponse).isEqualTo(updatedFreeBoardAnswerApiResponse);
	}

}
