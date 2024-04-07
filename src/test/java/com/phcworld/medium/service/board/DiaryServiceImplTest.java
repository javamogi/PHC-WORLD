package com.phcworld.medium.service.board;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.dto.DiaryRequest;
import com.phcworld.domain.board.dto.DiaryResponse;
import com.phcworld.domain.board.dto.DiaryResponseDto;
import com.phcworld.domain.good.Good;
import com.phcworld.medium.util.DiaryFactory;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.repository.board.dto.DiarySelectDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//@Transactional
@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class DiaryServiceImplTest {
	
	@Mock
	private DiaryServiceImpl diaryService;

	private UserEntity user;
	private Diary diary;

	@Before
	public void setup(){
		user = UserEntity.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		diary = DiaryFactory.getDiaryEntity(user);
	}
	
	@Test
	public void findPageDiary() throws Exception {
		Diary diary2 = Diary.builder()
				.writer(user)
				.title("title2")
				.contents("content2")
				.thumbnail("no-image-icon.gif")
				.build();
		List<DiarySelectDto> list = new ArrayList<DiarySelectDto>();
		list.add(DiarySelectDto.of(diary));
		list.add(DiarySelectDto.of(diary2));

		String searchKeyword = null;
		Page<DiarySelectDto> page = new PageImpl<DiarySelectDto>(list);
		when(diaryService.findPageDiary(user, 0, user, searchKeyword)).thenReturn(page);
		Page<DiarySelectDto> pageDiary = diaryService.findPageDiary(user, 0, user, searchKeyword);
		List<DiarySelectDto> diaryList = pageDiary.getContent();
		assertThat(diaryList)
				.contains(DiarySelectDto.of(diary))
				.contains(DiarySelectDto.of(diary2));
	}

	@Test
	public void getDiaryResponseDto() throws Exception {
		Diary diary2 = Diary.builder()
				.writer(user)
				.title("title2")
				.contents("content2")
				.thumbnail("no-image-icon.gif")
				.build();
		List<Diary> list = new ArrayList<Diary>();
		list.add(diary);
		list.add(diary2);
		Page<Diary> page = new PageImpl<Diary>(list);
		List<DiaryResponse> diaryResponseList = page.getContent().stream()
				.map(DiaryResponse::of)
				.collect(Collectors.toList());
		DiaryResponseDto diaryResponseDto = DiaryResponseDto.builder()
				.totalPages(page.getTotalPages())
				.diaries(diaryResponseList)
				.build();
		String searchKeyword = null;
		when(diaryService.getDiaryResponseListTemp(user, user.getEmail(), 1, null)).thenReturn(diaryResponseDto);
		DiaryResponseDto dto = diaryService.getDiaryResponseListTemp(user, user.getEmail(), 1, null);
		assertThat(dto.getDiaries())
				.contains(DiaryResponse.of(diary))
				.contains(DiaryResponse.of(diary2));
	}
	
	@Test
	public void createDiary() {
		List<String> hashtags = new ArrayList<>();
		hashtags.add("#아침");
		hashtags.add("#과일");
		DiaryRequest request = DiaryRequest.builder()
				.title("title")
				.contents("contents")
				.thumbnail("no-image-icon.gif")
				.hashtags(hashtags)
				.build();
		DiaryResponse diaryResponse = DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(request.getTitle())
				.contents(request.getContents())
				.thumbnail(request.getThumbnail())
				.hashtags(hashtags)
				.build();
		when(diaryService.createDiary(user, request))
		.thenReturn(diaryResponse);
		DiaryResponse createdDiary = diaryService.createDiary(user, request);
		assertThat("title").isEqualTo(createdDiary.getTitle());
		assertThat("contents").isEqualTo(createdDiary.getContents());
		assertThat(hashtags).contains("#아침").contains("#과일");
	}
	
	@Test
	public void getOneDiaryResponse() {
		DiaryResponse diaryResponse = DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.countOfAnswers(diary.getCountOfAnswer())
				.build();
		when(diaryService.getOneDiary(1L)).thenReturn(diaryResponse);
		DiaryResponse oneDiaryResponse = diaryService.getOneDiary(1L);
		assertThat(diaryResponse).isEqualTo(oneDiaryResponse);
	}
	
	@Test
	public void updateDiary() throws Exception {
		Diary newDiary = Diary.builder()
				.writer(user)
				.title("title")
				.contents("updateDiary")
				.thumbnail("test.jpg")
				.build();
		DiaryRequest request = DiaryRequest.builder()
				.id(newDiary.getId())
				.title(newDiary.getTitle())
				.contents(newDiary.getContents())
				.thumbnail(newDiary.getThumbnail())
				.build();
		DiaryResponse diaryResponse = DiaryResponse.builder()
				.id(newDiary.getId())
				.writer(newDiary.getWriter())
				.title(newDiary.getTitle())
				.contents(newDiary.getContents())
				.thumbnail(newDiary.getThumbnail())
				.countOfAnswers(newDiary.getCountOfAnswer())
				.build();
		diary.update(request);
		when(diaryService.updateDiary(request))
		.thenReturn(diaryResponse);
		DiaryResponse updatedDiary = diaryService.updateDiary(request);
		assertThat("updateDiary").isEqualTo(updatedDiary.getContents());
		assertThat("test.jpg").isEqualTo(updatedDiary.getThumbnail());
	}
	
	@Test
	public void deleteDiaryEmptyDiaryAnswer() throws Exception {
		diaryService.deleteDiary(diary.getId());
		verify(diaryService, times(1)).deleteDiary(diary.getId());
	}
	
	@Test
	public void deleteDiary() throws Exception {
		DiaryAnswer answer = DiaryAnswer.builder()
				.id(1L)
				.writer(user)
				.diary(diary)
				.contents("diary answer content")
				.build();
		List<DiaryAnswer> list = new ArrayList<DiaryAnswer>();
		list.add(answer);
		diary.setDiaryAnswers(list);
		diaryService.deleteDiary(diary.getId());
		verify(diaryService, times(1)).deleteDiary(diary.getId());
	}
	
	@Test
	public void addGoodWhenPushGoodButton() throws Exception {
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.createDate(LocalDateTime.now())
				.build();
		List<Good> list = new ArrayList<Good>();
		list.add(good);
		diary.setGoodPushedUser(list);
		
		SuccessResponse successResponse = SuccessResponse.builder()
				.success(Integer.toString(diary.getCountOfGood()))
				.build();
		
		when(diaryService.updateGood(diary.getId(), user))
		.thenReturn(successResponse);
		SuccessResponse newSuccessResponse = diaryService.updateGood(diary.getId(), user);
		assertThat(successResponse).isEqualTo(newSuccessResponse);
	}
	
	@Test
	public void declineGoodWhenPushGoodButton() throws Exception {
		Good good = Good.builder()
				.diary(diary)
				.user(user)
				.createDate(LocalDateTime.now())
				.build();
		List<Good> list = new ArrayList<Good>();
		list.add(good);
		diary.setGoodPushedUser(list);
		diary.getGoodPushedUser().remove(good);
		SuccessResponse successResponse = SuccessResponse.builder()
				.success(Integer.toString(diary.getCountOfGood()))
				.build();
		when(diaryService.updateGood(diary.getId(), user))
		.thenReturn(successResponse);
		SuccessResponse newSuccessResponse = diaryService.updateGood(diary.getId(), user);
		assertThat(successResponse).isEqualTo(newSuccessResponse);
	}
	
	@Test
	public void findDiaryListByWriter() {
		List<Diary> list = new ArrayList<Diary>();
		list.add(diary);
		when(diaryService.findDiaryListByWriter(user))
		.thenReturn(list);
		List<Diary> diaryList = diaryService.findDiaryListByWriter(user);
		assertThat(diaryList).contains(diary);
	}
	
}
