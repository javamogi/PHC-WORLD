package com.phcworld.medium.repository.board;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.medium.domain.board.FreeBoardInsertDto;
import com.phcworld.exception.model.CustomException;
import com.phcworld.medium.util.FreeBoardFactory;
import com.phcworld.freeboard.infrastructure.FreeBoardJpaRepository;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardSelectDto;
import com.phcworld.user.infrastructure.UserEntity;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.freeboard.domain.dto.FreeBoardRequest;

import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@Transactional
@Slf4j
public class FreeBoardJpaRepositoryTest {
	
	@Autowired
	private FreeBoardJpaRepository freeBoardRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private UserEntity user;

	public void setup(){
		user = UserEntity.builder()
				.id(1L)
				.build();
	}
	
	@Test
	@Transactional
	public void create() {
		FreeBoardEntity freeBoard = FreeBoardEntity.builder()
				.writer(user)
				.title("title")
				.contents("content")
				.count(0)
				.build();
		FreeBoardEntity newFreeBoard =  freeBoardRepository.save(freeBoard);
		assertNotNull(newFreeBoard);
	}

	@Test
	@Ignore
	public void bulkInsertByJDBC(){

		EasyRandom generator = FreeBoardFactory.getFreeBoardRandomEntity();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<FreeBoardInsertDto> list = IntStream.range(0, 10000 * 100)
				.parallel()
				.mapToObj(i -> generator.nextObject(FreeBoardEntity.class))
				.map(FreeBoardInsertDto::of)
				.collect(Collectors.toList());
		stopWatch.stop();
		log.info("객체 생성 시간 : {}", stopWatch.getTotalTimeSeconds());

		StopWatch queryStopWatch = new StopWatch();
		queryStopWatch.start();

		String sql = String.format("INSERT INTO %s (writer_id, title, contents, icon, badge, count, create_date, update_date) VALUES (:writerId, :title, :contents, :icon, :badge, :count, :createDate, :updateDate)", "free_board");
//		String sql = String.format("INSERT INTO %s (id, writer_id, title, contents, icon, badge, count) VALUES (nextval('BOARD_SEQ'), :writerId, :title, :contents, :icon, :badge, :count)", "free_board");

		SqlParameterSource[] params = list
				.stream()
				.map(BeanPropertySqlParameterSource::new)
				.toArray(SqlParameterSource[]::new);
		namedParameterJdbcTemplate.batchUpdate(sql, params);
		queryStopWatch.stop();
		log.info("DB 인서트 시간 : {}", queryStopWatch.getTotalTimeSeconds());
	}

	@Test
	@DisplayName("ID생성 전략이 IDENTITY에서는 사용하면 성능저하")
	@Ignore
	public void bulkInsertByJPA(){

		EasyRandom generator = FreeBoardFactory.getFreeBoardRandomEntity();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<FreeBoardEntity> list = IntStream.range(0, 10000 * 100)
				.parallel()
				.mapToObj(i -> generator.nextObject(FreeBoardEntity.class))
				.collect(Collectors.toList());
		stopWatch.stop();
		log.info("객체 생성 시간 : {}", stopWatch.getTotalTimeSeconds());

		StopWatch queryStopWatch = new StopWatch();
		queryStopWatch.start();
		freeBoardRepository.saveAll(list);
		queryStopWatch.stop();
		log.info("DB 인서트 시간 : {}", queryStopWatch.getTotalTimeSeconds());
	}
	
	@Test
	@Transactional
	public void read() {
		FreeBoardEntity freeBoard = FreeBoardEntity.builder()
				.writer(user)
				.title("title")
				.contents("content")
				.count(0)
				.build();
		freeBoardRepository.save(freeBoard);
		List<FreeBoardEntity> freeBoardList = freeBoardRepository.findByWriter(user);
		assertThat(freeBoardList).contains(freeBoard);
	}

	@Test
	public void readByQuerydsl() {
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createDate").descending());
		StopWatch queryStopWatch = new StopWatch();
		queryStopWatch.start();
		List<FreeBoardSelectDto> freeBoardList = freeBoardRepository.findByKeywordOrderById("x", pageRequest);
		queryStopWatch.stop();
		log.info("DB querydsl SELECT 시간 : {}", queryStopWatch.getTotalTimeSeconds());
		freeBoardList.stream().forEach(board -> {
			log.info("freeBoard title : {}", board.getTitle());
		});
	}

	@Test(expected = CustomException.class)
	public void read_throw_exception(){
		freeBoardRepository.findById(5L)
				.orElseThrow(() -> new CustomException("400", "게시물이 존재하지 않습니다."));
	}
	
	@Test
	@Transactional
	public void update() {
		FreeBoardEntity freeBoard = FreeBoardEntity.builder()
				.writer(user)
				.title("title")
				.contents("content")
				.count(0)
				.build();
		FreeBoardRequest request = FreeBoardRequest.builder()
				.id(1L)
				.contents("new contents")
				.icon("")
				.build();
		FreeBoardEntity newBoard = freeBoardRepository.save(freeBoard);
		FreeBoardEntity register = freeBoardRepository.findById(newBoard.getId())
				.orElseThrow(() -> new CustomException("400", "게시물이 존재하지 않습니다."));
		register.update(request);
		FreeBoardEntity updatedBoard = freeBoardRepository.save(register);
		assertThat(request.getContents()).isEqualTo(updatedBoard.getContents());
	}
	
	@Test
	@Transactional
	public void delete() {
		FreeBoardEntity freeBoard = FreeBoardEntity.builder()
				.writer(user)
				.title("title")
				.contents("content")
				.count(0)
				.build();
		FreeBoardEntity newBoard = freeBoardRepository.save(freeBoard);
		assertNotNull(newBoard);
		freeBoardRepository.delete(newBoard);
		Optional<FreeBoardEntity> findBoard = freeBoardRepository.findById(newBoard.getId());
		assertFalse(findBoard.isPresent());
	}

}
