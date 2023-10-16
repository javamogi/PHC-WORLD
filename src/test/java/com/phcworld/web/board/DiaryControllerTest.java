package com.phcworld.web.board;

import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.dto.DiaryRequest;
import com.phcworld.domain.board.dto.DiaryResponse;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.Authority;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.dto.DiarySelectDto;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.service.user.UserService;
import com.phcworld.utils.HttpSessionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(DiaryController.class)
public class DiaryControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private DiaryServiceImpl diaryService;

	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void requestEmailDiaryList() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User requestUser = User.builder()
				.id(2L)
				.email("test@test.test")
				.password("test")
				.name("테스트")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		given(this.userService.findUserByEmail("test@test.test"))
		.willReturn(requestUser);
		Diary diary = Diary.builder()
				.id(1L)
				.writer(requestUser)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();
		Diary diary2 = Diary.builder()
				.id(2L)
				.writer(requestUser)
				.title("test2")
				.contents("test2")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();
		List<DiarySelectDto> diaryList = new ArrayList<DiarySelectDto>();
		diaryList.add(DiarySelectDto.of(diary));
		diaryList.add(DiarySelectDto.of(diary2));
		
		Page<DiarySelectDto> page = new PageImpl<DiarySelectDto>(diaryList);
		List<Integer> pageNations = new ArrayList<Integer>();
		pageNations.add(1);
		
		
		when(diaryService.findPageDiary(user, 1, requestUser))
		.thenReturn(page);
		Page<DiarySelectDto> diaryPage = diaryService.findPageDiary(user, 1, requestUser);
		
		List<DiaryResponse> diaryResponseList = diaryPage.getContent().stream()
				.map(DiaryResponse::of)
				.collect(Collectors.toList());
		

		this.mvc.perform(get("/diaries/list/{email}", "test@test.test")
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("board/diary/diary")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("diaries", diaryResponseList))
		.andExpect(model().attribute("pageNations", pageNations))
		.andExpect(model().attribute("requestUser", requestUser))
		.andExpect(model().size(3));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void requestDiaryFormEmptyLoginUser() throws Exception {
		this.mvc.perform(get("/diaries/form")
						.with(csrf()))
		.andExpect(view().name(containsString("user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void successDiaryForm() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		this.mvc.perform(get("/diaries/form")
						.with(csrf())
				.session(mockSession))
		.andExpect(view().name(containsString("board/diary/diary_form")))
		.andExpect(model().attribute("user", user))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void createDiary() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		
		DiaryRequest request = DiaryRequest.builder()
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.build();
		DiaryResponse response = DiaryResponse.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.build();
		
		when(this.diaryService.createDiary(user, request))
		.thenReturn(response);

		this.mvc.perform(post("/diaries")
						.with(csrf())
				.param("title", "test")
				.param("contents", "test")
				.param("thumbnail", "no-image-icon.gif")
				.session(mockSession))
//		.andExpect(redirectedUrl("/diaries/list/" + user.getEmail()));
		.andExpect(redirectedUrl("/diaries/" + 1L));
	}
	
	@Test
	public void createWhenEmptyLoginUserDiary() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(post("/diaries")
						.with(csrf())
				.param("title", "test")
				.param("contents", "test")
				.param("thumbnail", "no-image-icon.gif")
				.session(mockSession))
				.andExpect(status().isUnauthorized());
//		.andExpect(view().name(containsString("/user/login")))
//		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void readDairyWhenDiaryWriterEqualLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.updateDate(LocalDateTime.now())
				.build();
		DiaryResponse diaryResponse = DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.countOfAnswers(diary.getCountOfAnswer())
				.countOfGood(diary.getCountOfGood())
				.updateDate(diary.getFormattedUpdateDate())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diaryResponse);
		this.mvc.perform(get("/diaries/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("board/diary/detail_diary")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("diary", diaryResponse))
		.andExpect(model().attribute("user", true))
		.andExpect(model().attribute("matchUser", true))
		.andExpect(model().attribute("matchAuthority", false))
		.andExpect(model().size(4));
	}
	
	@Test
	public void readWhenEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.build();
		DiaryResponse diaryResponse = DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.countOfAnswers(diary.getCountOfAnswer())
				.countOfGood(diary.getCountOfGood())
				.updateDate(diary.getFormattedUpdateDate())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diaryResponse);
		this.mvc.perform(get("/diaries/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
				.andExpect(status().isUnauthorized());
//		.andExpect(view().name(containsString("/board/diary/detail_diary")))
//		.andExpect(status().isOk())
//		.andExpect(model().attribute("diary", diaryResponse))
//		.andExpect(model().attribute("user", false))
//		.andExpect(model().attribute("matchUser", false))
//		.andExpect(model().attribute("matchAuthority", false))
//		.andExpect(model().size(4));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void readWhenWriterNotEqualLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user2)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.build();
		DiaryResponse diaryResponse = DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.countOfAnswers(diary.getCountOfAnswer())
				.countOfGood(diary.getCountOfGood())
				.updateDate(diary.getFormattedUpdateDate())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diaryResponse);
		this.mvc.perform(get("/diaries/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("board/diary/detail_diary")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("diary", diaryResponse))
		.andExpect(model().attribute("user", true))
		.andExpect(model().attribute("matchUser", false))
		.andExpect(model().attribute("matchAuthority", false))
		.andExpect(model().size(4));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void readDairyWhenHasAuthority() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		user.setAuthority(Authority.ROLE_ADMIN);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user2)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.build();
		DiaryResponse diaryResponse = DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.countOfAnswers(diary.getCountOfAnswer())
				.countOfGood(diary.getCountOfGood())
				.updateDate(diary.getFormattedUpdateDate())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diaryResponse);
		this.mvc.perform(get("/diaries/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("board/diary/detail_diary")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("diary", diaryResponse))
		.andExpect(model().attribute("user", true))
		.andExpect(model().attribute("matchUser", false))
		.andExpect(model().attribute("matchAuthority", true))
		.andExpect(model().size(4));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void requestUpdateDiaryForm() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.build();
		DiaryResponse diaryResponse = DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.countOfAnswers(diary.getCountOfAnswer())
				.countOfGood(diary.getCountOfGood())
				.updateDate(diary.getFormattedUpdateDate())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diaryResponse);
		this.mvc.perform(get("/diaries/{id}/form", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("board/diary/diary_updateForm")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("diary", diaryResponse))
		.andExpect(model().size(1));
	}
	
	@Test
	public void requestUpdateFormWhenEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.build();
		DiaryResponse diaryResponse = DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.countOfAnswers(diary.getCountOfAnswer())
				.countOfGood(diary.getCountOfGood())
				.updateDate(diary.getFormattedUpdateDate())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diaryResponse);
		this.mvc.perform(get("/diaries/{id}/form", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
				.andExpect(status().isUnauthorized());
//		.andExpect(view().name(containsString("/user/login")))
//		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void requestUpdateFormWhenMatchNotWriter() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user2)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.build();
		DiaryResponse diaryResponse = DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.title(diary.getTitle())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.countOfAnswers(diary.getCountOfAnswer())
				.countOfGood(diary.getCountOfGood())
				.updateDate(diary.getFormattedUpdateDate())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diaryResponse);
		this.mvc.perform(get("/diaries/{id}/form", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "본인의 작성한 글만 수정 가능합니다."))
		.andExpect(model().size(1));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void successUpdateDiary() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("contents")
				.thumbnail("no-image-icon.gif")
				.build();
		DiaryResponse response = DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.build();
		Diary updateDiary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("updateTest")
				.thumbnail("no-image-icon.gif")
				.build();
		DiaryRequest request = DiaryRequest.builder()
				.id(updateDiary.getId())
				.title(updateDiary.getTitle())
				.contents(updateDiary.getContents())
				.thumbnail(updateDiary.getThumbnail())
				.build();
		DiaryResponse updatedDiaryResponse = DiaryResponse.builder()
				.id(updateDiary.getId())
				.writer(updateDiary.getWriter())
				.contents(updateDiary.getContents())
				.thumbnail(updateDiary.getThumbnail())
				.countOfAnswers(updateDiary.getCountOfAnswer())
				.countOfGood(updateDiary.getCountOfGood())
				.updateDate(updateDiary.getFormattedUpdateDate())
				.build();
		diary.update(request);
		when(this.diaryService.getOneDiary(diary.getId()))
		.thenReturn(response);
		when(this.diaryService.updateDiary(request))
		.thenReturn(updatedDiaryResponse);
		this.mvc.perform(patch("/diaries")
						.with(csrf())
				.param("id", "1")
				.param("contents", "updateTest")
				.param("thumbnail", "no-image-icon.gif")
				.session(mockSession))
		.andExpect(redirectedUrl("/diaries/" + 1L));
	}
	
	@Test
	public void updateEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(patch("/diaries")
						.with(csrf())
				.param("id", "1")
				.param("contents", "updateTest")
				.param("thumbnail", "no-image-icon.gif")
				.session(mockSession))
		.andDo(print())
		.andExpect(status().isUnauthorized());
//		.andExpect(view().name(containsString("/user/login")))
//		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void updateWhenNotMatchWriter() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user2)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.build();
		DiaryResponse response = DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(response);
		this.mvc.perform(patch("/diaries")
						.with(csrf())
				.param("id", "1")
				.param("contents", "updateTest")
				.param("thumbnail", "no-image-icon.gif")
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "본인의 작성한 글만 수정 가능합니다."))
		.andExpect(model().size(2));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void successDeleteDiary() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.build();
		DiaryResponse response = DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(response);
		diaryService.deleteDiary(1L);
		this.mvc.perform(delete("/diaries/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andExpect(redirectedUrl("/diaries/list/" + user.getEmail()));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void deleteEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(delete("/diaries/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void deleteWhenNotMatchAuthority() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = User.builder()
				.id(2L)
				.email("test4@test.test")
				.password("test4")
				.name("테스트4")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user2)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.build();
		DiaryResponse response = DiaryResponse.builder()
				.id(diary.getId())
				.writer(diary.getWriter())
				.contents(diary.getContents())
				.thumbnail(diary.getThumbnail())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(response);
		this.mvc.perform(delete("/diaries/{id}", 1L)
						.with(csrf())
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "삭제 권한이 없습니다."))
		.andExpect(model().size(1));
	}
	
	@Test
	public void pushUpbuttonWhenEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(put("/diaries/{diaryId}/good", 1L)
						.with(csrf())
				.session(mockSession))
				.andExpect(status().isUnauthorized());
//		.andExpect(jsonPath("$.error").value("로그인을 해야합니다."));
	}
	
	@Test
	@WithMockUser(username = "test3@test.test", authorities = "ROLE_USER")
	public void pushUpbutton() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority(Authority.ROLE_USER)
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("title")
				.contents("content")
				.thumbnail("no-image-icon.gif")
				.build();
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
		this.mvc.perform(put("/diaries/{diaryId}/good", 1L)
						.with(csrf())
				.session(mockSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.success").value(successResponse.getSuccess()));
	}
	
}
