package com.phcworld.domain.alert;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;
import com.phcworld.repository.answer.DiaryAnswerRepository;
import com.phcworld.repository.answer.FreeBoardAnswerRepository;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.repository.board.FreeBoardRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AlertServiceImplTest {
	
	private static final Logger log = LoggerFactory.getLogger(AlertServiceImplTest.class);
	
	@Autowired
	private AlertServiceImpl alertService;
	
	@Autowired
	private DiaryRepository diaryRepository;
	
	@Autowired
	private DiaryAnswerRepository diaryAnswerRepository;
	
	@Autowired
	private FreeBoardRepository freeBoardRepository;
	
	@Autowired
	private FreeBoardAnswerRepository freeBoardAnswerRepository;
	
	@Test
	@Transactional
	public void createAlertAndGetOneAlert() {
		User writer = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		writer.setId(1L);
		Diary diary = Diary.builder()
				.writer(writer)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.countOfGood(0)
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		diaryRepository.save(diary);
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.writer(writer)
				.diary(diary)
				.contents("diaryAnswer")
				.build();
		diaryAnswerRepository.save(diaryAnswer);
		Alert alert = new Alert("Diary", diaryAnswer, diary.getWriter(), diaryAnswer.getCreateDate());
		log.debug("alert : {}", alert);
		Alert alertCreated = alertService.createAlert(alert);
		Alert actual = alertService.getOneAlert(alertCreated.getId());
		log.debug("actual : {}", actual);
		assertThat(actual, is(alertCreated));
	}
	
	@Test
	@Transactional
	public void findPageRequestAlertByUser() throws Exception {
		User writer = User.builder()
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		writer.setId(1L);
		
		Diary diary = Diary.builder()
				.writer(writer)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.countOfGood(0)
				.countOfAnswer(0)
				.createDate(LocalDateTime.now())
				.build();
		diaryRepository.save(diary);
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.writer(writer)
				.diary(diary)
				.contents("diaryAnswer")
				.build();
		diaryAnswerRepository.save(diaryAnswer);
		
		Alert diaryAnswerAlert = new Alert("Diary", diaryAnswer, diary.getWriter(), diaryAnswer.getCreateDate());
		alertService.createAlert(diaryAnswerAlert);
		
		FreeBoard freeBoard = FreeBoard.builder()
				.title("title")
				.contents("content")
				.icon("")
				.createDate(LocalDateTime.now())
				.count(0)
				.countOfAnswer(0)
				.build();
		freeBoardRepository.save(freeBoard);
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.writer(writer)
				.freeBoard(freeBoard)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		freeBoardAnswerRepository.save(freeBoardAnswer);
		
		Alert freeBoardAnswerAlert = new Alert("Free Board", freeBoardAnswer, writer, freeBoardAnswer.getCreateDate());
		alertService.createAlert(freeBoardAnswerAlert);
		
		List<Alert> alertList = alertService.findPageRequestAlertByUser(writer);
		assertThat(alertList, hasItems(diaryAnswerAlert, freeBoardAnswerAlert));
	}

}
