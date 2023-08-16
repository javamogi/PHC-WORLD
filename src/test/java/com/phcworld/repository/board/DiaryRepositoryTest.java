package com.phcworld.repository.board;


import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryInsertDto;
import com.phcworld.domain.board.DiaryRequest;
import com.phcworld.domain.board.DiaryResponse;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.dto.DiarySelectDto;
import com.phcworld.util.DiaryFactory;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

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
public class DiaryRepositoryTest {
	
	@Autowired
	private DiaryRepository diaryRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private User user;

	public void setup(){
		user = User.builder()
				.id(1L)
				.build();
	}

	@Test
	@Transactional
	public void create() {
		Diary diary = Diary.builder()
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		Diary newDiary = diaryRepository.save(diary);
		assertNotNull(newDiary);
	}

	@Test
	@Ignore
	public void bulkInsert(){

		EasyRandom generator = DiaryFactory.getDiaryRandomEntity();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<DiaryInsertDto> list = IntStream.range(0, 10000 * 100)
				.parallel()
				.mapToObj(i -> generator.nextObject(Diary.class))
				.map(DiaryInsertDto::of)
				.collect(Collectors.toList());
		stopWatch.stop();
		log.info("객체 생성 시간 : {}", stopWatch.getTotalTimeSeconds());

		StopWatch queryStopWatch = new StopWatch();
		queryStopWatch.start();

		String sql = String.format("INSERT INTO %s (writer_id, title, contents, thumbnail, create_date, update_date) VALUES (:writerId, :title, :contents, :thumbnail, :createDate, :updateDate)", "diary");

		SqlParameterSource[] params = list
				.stream()
				.map(BeanPropertySqlParameterSource::new)
				.toArray(SqlParameterSource[]::new);
		namedParameterJdbcTemplate.batchUpdate(sql, params);
		queryStopWatch.stop();
		log.info("DB 인서트 시간 : {}", queryStopWatch.getTotalTimeSeconds());
	}
	
	@Test
	@Transactional
	public void read() {
		Diary diary = Diary.builder()
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		diaryRepository.save(diary);
//		PageRequest pageRequest = PageRequest.of(0, 6, new Sort(Direction.DESC, "id"));
		PageRequest pageRequest = PageRequest.of(0, 6, Sort.by("id").descending());
		Page<Diary> diaryPage = diaryRepository.findByWriter(user, pageRequest);
		List<Diary> diaryList = diaryPage.getContent();
		assertThat(diaryList).contains(diary);
	}
	
	@Test
	@Transactional
	public void update() {
		Diary diary = Diary.builder()
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		Diary newDiary = Diary.builder()
				.writer(user)
				.title("title")
				.contents("update content")
				.thumbnail("test.jpg")
				.build();
		DiaryRequest request = DiaryRequest.builder()
				.id(newDiary.getId())
				.title(newDiary.getTitle())
				.contents(newDiary.getContents())
				.thumbnail(newDiary.getThumbnail())
				.build();
		
		diary.update(request);
		Diary updatedDiary = diaryRepository.save(diary);
		assertThat("update content").isEqualTo(updatedDiary.getContents());
		assertThat("test.jpg").isEqualTo(updatedDiary.getThumbnail());
	}
	
	@Test
	@Transactional
	public void delete() {
		Diary diary = Diary.builder()
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		Diary newDiary = diaryRepository.save(diary);
		diaryRepository.delete(newDiary);
		Optional<Diary> findDiary = diaryRepository.findById(newDiary.getId());
		assertFalse(findDiary.isPresent());
	}

	@Test
	public void findAllByQuerydsl(){
		Diary diary = Diary.builder()
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		diaryRepository.save(diary);
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createDate").descending());
		StopWatch queryStopWatch = new StopWatch();
		queryStopWatch.start();
		Page<DiarySelectDto> diaryPage = diaryRepository.findAllPage(user, pageRequest);
		queryStopWatch.stop();
		log.info("DB querydsl SELECT 시간 : {}", queryStopWatch.getTotalTimeSeconds());

		List<DiarySelectDto> list = diaryPage.getContent();
		List<DiaryResponse> responseList = list.stream()
						.map(DiaryResponse::of)
						.collect(Collectors.toList());
		assertThat(responseList.get(0).getTitle())
				.isEqualTo(diary.getTitle());
	}

}
