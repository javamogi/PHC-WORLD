package com.phcworld.repository.board;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryRequest;
import com.phcworld.domain.user.User;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class DiaryRepositoryTest {
	
	@Autowired
	private DiaryRepository diaryRepository;

	@Test
	public void create() {
		User writer = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		Diary diary = Diary.builder()
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		Diary newDiary = diaryRepository.save(diary);
		assertNotNull(newDiary);
	}
	
	@Test
	public void read() {
		User writer = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		Diary diary = Diary.builder()
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		diaryRepository.save(diary);
//		PageRequest pageRequest = PageRequest.of(0, 6, new Sort(Direction.DESC, "id"));
		PageRequest pageRequest = PageRequest.of(0, 6, Sort.by("id").descending());
		Page<Diary> diaryPage = diaryRepository.findByWriter(writer, pageRequest);
		List<Diary> diaryList = diaryPage.getContent();
		assertThat(diaryList, hasItems(diary));
	}
	
	@Test
	public void update() {
		User writer = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		Diary diary = Diary.builder()
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		Diary newDiary = Diary.builder()
				.writer(writer)
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
		assertThat("update content", is(updatedDiary.getContents()));
		assertThat("test.jpg", is(updatedDiary.getThumbnail()));
	}
	
	@Test
	public void delete() {
		User writer = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		Diary diary = Diary.builder()
				.writer(writer)
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
		User writer = User.builder()
				.id(1L)
				.email("user@test.test")
				.password("user")
				.name("user")
				.build();
		Diary diary = Diary.builder()
				.writer(writer)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
		diaryRepository.save(diary);
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createDate").descending());
		Page<Diary> diaryPage = diaryRepository.findAllPage(pageRequest);
		List<Diary> diaryList = diaryPage.getContent();
		assertThat(diaryList, hasItems(diary));
	}

}
