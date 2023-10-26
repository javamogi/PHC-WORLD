package com.phcworld.service.board;

import com.phcworld.domain.board.dto.DiaryRequest;
import com.phcworld.domain.board.dto.DiaryResponse;
import com.phcworld.domain.user.Authority;
import com.phcworld.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class DiaryServiceImplIntegrationTest {
	
	@SpyBean
	private DiaryServiceImpl diaryService;

	private User user;

	@Before
	public void setup(){
		user = User.builder()
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

}
