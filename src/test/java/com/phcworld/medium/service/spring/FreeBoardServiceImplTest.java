package com.phcworld.medium.service.spring;

import com.phcworld.freeboard.controller.port.FreeBoardResponse;
import com.phcworld.freeboard.controller.port.FreeBoardSearchDto;
import com.phcworld.freeboard.controller.port.FreeBoardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
		FreeBoardSearchDto search = new FreeBoardSearchDto();
		search.setPageNum(1);
		search.setPageSize(30);
		search.setKeyword("");
		StopWatch queryStopWatch = new StopWatch();
		queryStopWatch.start();
		List<FreeBoardResponse> list = service.getSearchResult(search);
		queryStopWatch.stop();
		log.info("DB querydsl SELECT 시간 : {}", queryStopWatch.getTotalTimeSeconds());
		for (FreeBoardResponse f : list) {
			log.info("freeboard : {}", f);
		}
	}
	
}
