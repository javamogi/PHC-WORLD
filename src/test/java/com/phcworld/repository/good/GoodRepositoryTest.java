package com.phcworld.repository.good;


import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryInsertDto;
import com.phcworld.domain.board.GoodInsertDto;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.Authority;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.DiaryRepository;
import com.phcworld.repository.user.UserRepository;
import com.phcworld.util.DiaryFactory;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@Transactional
@Slf4j
public class GoodRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GoodRepository goodRepository;
	
	@Autowired
	private DiaryRepository diaryRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private User user;
	private Diary diary;

	@Test
	@Ignore
	public void bulkInsert(){

		EasyRandom generator = DiaryFactory.getGoodEntity();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<GoodInsertDto> list = IntStream.range(0, 10000 * 100)
				.parallel()
				.mapToObj(i -> generator.nextObject(Good.class))
				.map(GoodInsertDto::of)
				.collect(Collectors.toList());
		stopWatch.stop();
		log.info("객체 생성 시간 : {}", stopWatch.getTotalTimeSeconds());

		StopWatch queryStopWatch = new StopWatch();
		queryStopWatch.start();

		String sql = String.format("INSERT INTO %s (diary_id, user_id, create_date) VALUES (:diaryId, :userId, :createDate)", "good");

		SqlParameterSource[] params = list
				.stream()
				.map(BeanPropertySqlParameterSource::new)
				.toArray(SqlParameterSource[]::new);
		namedParameterJdbcTemplate.batchUpdate(sql, params);
		queryStopWatch.stop();
		log.info("DB 인서트 시간 : {}", queryStopWatch.getTotalTimeSeconds());
	}

	@Before
	public void setup(){
		user = User.builder()
				.id(1L)
				.email("test@test.test")
				.name("테스트")
				.authority(Authority.ROLE_ADMIN)
				.profileImage("blank-profile-picture.png")
				.build();
		diary = diaryRepository.save(DiaryFactory.getDiaryEntity(user));
	}

	@Test
	@Transactional
	public void create() {
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.build();
		Good newGood = goodRepository.save(good);
		assertNotNull(newGood);
	}
	
	@Test
	public void readByDiaryAndUser() {
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.build();
		Good newGood = goodRepository.save(good);
		Good findGood = goodRepository.findByDiaryAndUser(diary, user);
		assertThat(newGood).isEqualTo(findGood);
	}

	@Test
	public void readByUser() {
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.build();
		Good newGood = goodRepository.save(good);
		List<Good> goodList = goodRepository.findByUser(user);
		assertThat(goodList).contains(newGood);
	}
	
	@Test
	@Transactional
	public void delete() {
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.build();
		Good newGood = goodRepository.save(good);
		goodRepository.delete(newGood);
		Optional<Good> findGood = goodRepository.findById(newGood.getId());
		assertFalse(findGood.isPresent());
	}

}
