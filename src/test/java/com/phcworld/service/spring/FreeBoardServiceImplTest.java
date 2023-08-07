package com.phcworld.service.spring;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.board.FreeBoardRequest;
import com.phcworld.domain.board.FreeBoardResponse;
import com.phcworld.domain.user.User;
import com.phcworld.exception.model.CustomException;
import com.phcworld.service.board.FreeBoardService;
import com.phcworld.service.board.FreeBoardServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class FreeBoardServiceImplTest {

	@Autowired
	private FreeBoardService service;

	@Test
	public void findFreeBoardAllList() throws Exception {
		StopWatch queryStopWatch = new StopWatch();
		queryStopWatch.start();
		service.findFreeBoardAllListAndSetNewBadge();
		queryStopWatch.stop();
		log.info("DB querymethod SELECT 시간 : {}", queryStopWatch.getTotalTimeSeconds());
	}


	@Test
	public void findFreeBoardAllListByQuerydsl() throws Exception {
		StopWatch queryStopWatch = new StopWatch();
		queryStopWatch.start();
		List<FreeBoardResponse> list = service.getByQuerydsl(1, 20, "x");
		queryStopWatch.stop();
		log.info("DB querydsl SELECT 시간 : {}", queryStopWatch.getTotalTimeSeconds());
		for (FreeBoardResponse f : list) {
			log.info("freeboard : {}", f);
		}
	}
	
}
