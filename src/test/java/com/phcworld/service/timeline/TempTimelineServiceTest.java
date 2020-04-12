package com.phcworld.service.timeline;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.timeline.TempTimeline;
import com.phcworld.domain.user.User;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class TempTimelineServiceTest {
	
	@Mock
	private TimelineServiceImpl timelineService;

	@Test
	public void getTilmelineList() {
		List<TempTimeline> timelineList = new ArrayList<TempTimeline>();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test3")
				.contents("test3")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		TempTimeline diaryTimeline = TempTimeline.builder()
				.type("diary")
				.icon("edit")
				.url("/diaries/"+diary.getId())
				.diary(diary)
				.saveDate(diary.getCreateDate())
				.build();
		timelineList.add(diaryTimeline);
		DiaryAnswer diaryAnswer = DiaryAnswer.builder()
				.writer(user)
				.diary(diary)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		TempTimeline diaryAnswerTimeline = TempTimeline.builder()
				.type("diary answer")
				.icon("comment")
				.url("/diaries/"+diaryAnswer.getDiary().getId())
				.diaryAnswer(diaryAnswer)
				.saveDate(diaryAnswer.getCreateDate())
				.build();
		timelineList.add(diaryAnswerTimeline);
		
		FreeBoard freeBoard = FreeBoard.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.count(0)
				.badge("")
				.createDate(LocalDateTime.now())
				.build();
		TempTimeline freeBoardTimeline = TempTimeline.builder()
				.type("free board")
				.url("/freeboards/"+freeBoard.getId())
				.icon("list-alt")
				.freeBoard(freeBoard)
				.saveDate(freeBoard.getCreateDate())
				.build();
		timelineList.add(freeBoardTimeline);
		
		FreeBoardAnswer freeBoardAnswer = FreeBoardAnswer.builder()
				.writer(user)
				.freeBoard(freeBoard)
				.contents("test")
				.createDate(LocalDateTime.now())
				.build();
		TempTimeline freeBoardAnswerTimeline = TempTimeline.builder()
				.type("free board answer")
				.icon("comment")
				.url("/freeboards/"+freeBoardAnswer.getFreeBoard().getId())
				.freeBoardAnswer(freeBoardAnswer)
				.saveDate(freeBoardAnswer.getCreateDate())
				.build();
		timelineList.add(freeBoardAnswerTimeline);
		
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.createDate(LocalDateTime.now())
				.build();
		TempTimeline goodTimeline = TempTimeline.builder()
				.type("good")
				.icon("thumbs-up")
				.url("/diaries/"+good.getDiary().getId())
				.good(good)
				.saveDate(good.getCreateDate())
				.build();
		timelineList.add(goodTimeline);
		
		Comparator<TempTimeline> compTimeline = new Comparator<TempTimeline>() {
			@Override
			public int compare(TempTimeline a, TempTimeline b) {
				return b.getSaveDate().compareTo(a.getSaveDate());
			}
		};
		timelineList.sort(compTimeline);
		assertThat(timelineList, hasItems(diaryTimeline, diaryAnswerTimeline, 
				freeBoardTimeline, freeBoardAnswerTimeline, goodTimeline));
		timelineList.stream().forEach(timeline -> {
			log.info("Timeline : {}", timeline);
		});
	}

}
