package com.phcworld.medium.repository.board;


import com.phcworld.freeboard.infrastructure.FreeBoardJpaRepository;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardAndAnswersSelectDto;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardSelectDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
@SqlGroup({
		@Sql(value = "/sql/free_board-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
public class FreeBoardRepositoryTest {
	
	@Autowired
	private FreeBoardJpaRepository freeBoardJpaRepository;

	@Test
	@Transactional
	public void findByQuerydsl(){
		PageRequest pageRequest = PageRequest.of(0, 10);
		StopWatch queryStopWatch = new StopWatch();
		queryStopWatch.start();
		Optional<FreeBoardAndAnswersSelectDto> result = freeBoardJpaRepository.findByIdAndAnswers(1, pageRequest);
		queryStopWatch.stop();
		log.info("DB querydsl SELECT 시간 : {}", queryStopWatch.getTotalTimeSeconds());
		log.info("result : {}", result.get());
	}

	@Test
	@Transactional
	public void findAllByQuerydsl(){
		StopWatch queryStopWatch = new StopWatch();
		queryStopWatch.start();
		List<FreeBoardSelectDto> result = freeBoardJpaRepository.findAllWithAnswersCount(null);
		queryStopWatch.stop();
		log.info("DB querydsl SELECT 시간 : {}", queryStopWatch.getTotalTimeSeconds());
		log.info("result : {}", result);
	}

}
