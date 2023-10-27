package com.phcworld.service.board;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.Hashtag;
import com.phcworld.domain.board.dto.DiaryRequest;
import com.phcworld.domain.board.dto.DiaryResponse;
import com.phcworld.domain.user.Authority;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.HashtagRepository;
import com.phcworld.repository.board.dto.DiarySelectDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class DiaryServiceImplIntegrationTest {
	
	@SpyBean
	private DiaryServiceImpl diaryService;

	@Autowired
	private HashtagRepository hashtagRepository;

	private User user;

	@Before
	public void setup(){
		user = User.builder()
				.id(1L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_ADMIN)
				.createDate(LocalDateTime.now())
				.build();
	}

	@Test
	public void 일기_게시물_생성() {
		List<String> hashtags = new ArrayList<>();
		hashtags.add("#아침");
		hashtags.add("#과일");
		DiaryRequest request = DiaryRequest.builder()
				.title("title")
				.contents("contents")
				.thumbnail("no-image-icon.gif")
				.hashtags(hashtags)
				.build();
		StopWatch watch = new StopWatch();
		watch.start();
		DiaryResponse createdDiary = diaryService.createDiary(user, request);
		watch.stop();
		log.info("insert 시간 : {}", watch.getTotalTimeMillis());
		assertThat("title").isEqualTo(createdDiary.getTitle());
		assertThat("contents").isEqualTo(createdDiary.getContents());
		assertThat(hashtags).contains("#아침").contains("#과일");
	}

	@Test
	public void 해시태그_입력_일기_게시물_조회() throws Exception {
		List<String> hashtags = new ArrayList<>();
		hashtags.add("#아침");
		hashtags.add("#과일");
		DiaryRequest request = DiaryRequest.builder()
				.title("title")
				.contents("contents")
				.thumbnail("no-image-icon.gif")
				.hashtags(hashtags)
				.build();
		diaryService.createDiary(user, request);
		StopWatch watch = new StopWatch();
		watch.start();
		Page<DiarySelectDto> pageDiary = diaryService.findPageDiary(user, 1, user);
		List<DiarySelectDto> diaryList = pageDiary.getContent();
		watch.stop();
		log.info("select 시간 : {}", watch.getTotalTimeMillis());
		log.info("diary list : {}", diaryList);
		assertThat(hashtags).contains("#아침").contains("#과일");
	}

}
