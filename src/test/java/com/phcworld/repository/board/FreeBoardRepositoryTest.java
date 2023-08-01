package com.phcworld.repository.board;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.jeasy.random.FieldPredicates.*;
import static org.jeasy.random.FieldPredicates.inClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.phcworld.domain.board.FreeBoardInsertDto;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.board.FreeBoardRequest;
import com.phcworld.domain.user.User;

import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.springframework.util.StopWatch;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class FreeBoardRepositoryTest {
	
	@Autowired
	private FreeBoardRepository freeBoardRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	
	@Test
	public void create() {
		User user = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.count(0)
				.build();
		log.info("freeBoard : {}", freeBoard);
		FreeBoard newFreeBoard =  freeBoardRepository.save(freeBoard);
		assertNotNull(newFreeBoard);
	}

	@Test
	public void bulkInsert(){
		User user = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		Predicate<Field> idPredicate = named("id")
				.and(ofType(Long.class))
				.and(inClass(FreeBoard.class));

		Predicate<Field> memberIdPredicate = named("writer")
				.and(ofType(User.class))
				.and(inClass(FreeBoard.class));
		LocalDate firstDate = LocalDate.of(2010, 1, 1);
		LocalDate lastDate = LocalDate.now();
		EasyRandomParameters param = new EasyRandomParameters()
				.excludeField(idPredicate)
				.dateRange(firstDate, lastDate)
				.randomize(memberIdPredicate, () -> user);

		EasyRandom generator = new EasyRandom(param);
//		User user = generator.nextObject(User.class);
//		FreeBoard freeBoard = generator.nextObject(FreeBoard.class);
//		freeBoardRepository.save(freeBoard);

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<FreeBoardInsertDto> list = IntStream.range(0, 10000 * 100)
				.parallel()
				.mapToObj(i -> generator.nextObject(FreeBoard.class))
				.map(FreeBoardInsertDto::of)
				.collect(Collectors.toList());
//		List<FreeBoard> list = new ArrayList<>();
//		for (int i = 0; i < 10000 * 10; i++){
//			FreeBoard freeBoard = generator.nextObject(FreeBoard.class);
//			list.add(freeBoard);
//		}
		stopWatch.stop();
		log.info("객체 생성 시간 : {}", stopWatch.getTotalTimeSeconds());
		StopWatch queryStopWatch = new StopWatch();
		queryStopWatch.start();
//		freeBoardRepository.saveAll(list);
		String sql = String.format("INSERT INTO %s (writer_id, title, contents, icon, badge, count) VALUES (:writerId, :title, :contents, :icon, :badge, :count)", "FREE_BOARD");

		SqlParameterSource[] params = list
				.stream()
				.map(BeanPropertySqlParameterSource::new)
				.toArray(SqlParameterSource[]::new);
		namedParameterJdbcTemplate.batchUpdate(sql, params);
		queryStopWatch.stop();
		log.info("DB 인서트 시간 : {}", queryStopWatch.getTotalTimeSeconds());
	}
	
	@Test
	public void read() {
		User user = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.count(0)
				.build();
		freeBoardRepository.save(freeBoard);
		List<FreeBoard> freeBoardList = freeBoardRepository.findByWriter(user);
		freeBoardList.stream().forEach(board -> {
			log.info("freeBoard title : {}", board.getTitle());
		});
		assertThat(freeBoardList, hasItems(freeBoard));
	}
	
	@Test
	public void update() {
		User user = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.count(0)
				.build();
		FreeBoardRequest request = FreeBoardRequest.builder()
				.id(1L)
				.contents("new contents")
				.icon("")
				.build();
		FreeBoard newBoard = freeBoardRepository.save(freeBoard);
		FreeBoard regitBoard = freeBoardRepository.getOne(newBoard.getId());
		regitBoard.update(request);
		FreeBoard updatedBoard = freeBoardRepository.save(regitBoard);
		assertThat(request.getContents(), is(updatedBoard.getContents()));
	}
	
	@Test
	public void delete() {
		User user = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		FreeBoard freeBoard = FreeBoard.builder()
				.writer(user)
				.title("title")
				.contents("content")
				.icon("")
				.count(0)
				.build();
		FreeBoard newBoard = freeBoardRepository.save(freeBoard);
		assertNotNull(newBoard);
		freeBoardRepository.delete(newBoard);
		Optional<FreeBoard> findBoard = freeBoardRepository.findById(newBoard.getId());
		assertFalse(findBoard.isPresent());
	}

}
