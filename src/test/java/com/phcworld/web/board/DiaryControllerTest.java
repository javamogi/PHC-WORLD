package com.phcworld.web.board;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.user.User;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.service.user.UserService;
import com.phcworld.web.HttpSessionUtils;


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
	public void requestEmailDiaryList() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
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
				.authority("ROLE_USER")
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
				.build();
		Diary diary2 = Diary.builder()
				.id(2L)
				.writer(requestUser)
				.title("test2")
				.contents("test2")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		List<Diary> diaryList = new ArrayList<Diary>();
		diaryList.add(diary);
		diaryList.add(diary2);
		
		Page<Diary> page = new PageImpl<Diary>(diaryList);
		List<Integer> pageNations = new ArrayList<Integer>();
		pageNations.add(1);
		
		when(diaryService.findPageDiary(user, 1, requestUser))
		.thenReturn(page);
		Page<Diary> diaryPage = diaryService.findPageDiary(user, 1, requestUser);
		
		this.mvc.perform(get("/diary/list/{email}", "test@test.test")
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/diary/diary")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("diaries", diaryPage.getContent()))
		.andExpect(model().attribute("pageNations", pageNations))
		.andExpect(model().attribute("requestUser", requestUser))
		.andExpect(model().size(3));
	}
	
	@Test
	public void requestDiaryFormEmptyLoginUser() throws Exception {
		this.mvc.perform(get("/diary/form"))
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void successDiaryForm() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		this.mvc.perform(get("/diary/form")
				.session(mockSession))
		.andExpect(view().name(containsString("/board/diary/diary_form")))
		.andExpect(model().attribute("user", user))
		.andExpect(status().isOk());
	}
	
	@Test
	public void createDiary() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		this.mvc.perform(post("/diary")
				.param("title", "test")
				.param("contents", "test")
				.param("thumbnail", "no-image-icon.gif")
				.session(mockSession))
		.andExpect(redirectedUrl("/diary/list/" + user.getEmail()));
	}
	
	@Test
	public void createWhenEmptyLoginUserDiary() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(post("/diary")
				.param("title", "test")
				.param("contents", "test")
				.param("thumbnail", "no-image-icon.gif")
				.session(mockSession))
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void readDairyWhenDiaryWriterEqualLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
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
				.createDate(LocalDateTime.now())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diary);
		this.mvc.perform(get("/diary/{id}/detail", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/diary/detail_diary")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("diary", diary))
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
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diary);
		this.mvc.perform(get("/diary/{id}/detail", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/diary/detail_diary")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("diary", diary))
		.andExpect(model().attribute("user", false))
		.andExpect(model().attribute("matchUser", false))
		.andExpect(model().attribute("matchAuthority", false))
		.andExpect(model().size(4));
	}
	
	@Test
	public void readWhenWriterNotEqualLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
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
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user2)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diary);
		this.mvc.perform(get("/diary/{id}/detail", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/diary/detail_diary")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("diary", diary))
		.andExpect(model().attribute("user", true))
		.andExpect(model().attribute("matchUser", false))
		.andExpect(model().attribute("matchAuthority", false))
		.andExpect(model().size(4));
	}
	
	@Test
	public void readDairyWhenHasAuthority() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		user.setAuthority("ROLE_ADMIN");
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
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user2)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diary);
		this.mvc.perform(get("/diary/{id}/detail", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/diary/detail_diary")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("diary", diary))
		.andExpect(model().attribute("user", true))
		.andExpect(model().attribute("matchUser", false))
		.andExpect(model().attribute("matchAuthority", true))
		.andExpect(model().size(4));
	}
	
	@Test
	public void requestUpdateDiaryForm() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
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
				.createDate(LocalDateTime.now())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diary);
		this.mvc.perform(get("/diary/{id}/form", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/diary/diary_updateForm")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("diary", diary))
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
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diary);
		this.mvc.perform(get("/diary/{id}/form", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void requestUpdateFormWhenMatchNotWriter() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
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
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user2)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diary);
		this.mvc.perform(get("/diary/{id}/form", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "본인의 작성한 글만 수정 가능합니다."))
		.andExpect(model().size(1));
	}
	
	@Test
	public void successUpdateDiary() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		user.setId(1L);
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
				.createDate(LocalDateTime.now())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diary);
		Diary updatedDiary = Diary.builder()
				.id(1L)
				.writer(user)
				.title("test")
				.contents("updateTest")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		given(this.diaryService.updateDiary(diary, "updateTest", "no-image-icon.gif"))
		.willReturn(updatedDiary);
		this.mvc.perform(put("/diary/{id}", 1L)
				.param("contents", "updateTest")
				.param("thumbnail", "no-image-icon.gif")
				.session(mockSession))
		.andExpect(redirectedUrl("/diary/" + 1L + "/detail"));
	}
	
	@Test
	public void updateEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(put("/diary/{id}", 1L)
				.param("contents", "updateTest")
				.param("thumbnail", "no-image-icon.gif")
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void updateWhenNotMatchWriter() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
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
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user2)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diary);
		this.mvc.perform(put("/diary/{id}", 1L)
				.param("contents", "updateTest")
				.param("thumbnail", "no-image-icon.gif")
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "본인의 작성한 글만 수정 가능합니다."))
		.andExpect(model().size(1));
	}
	
	@Test
	public void successDeleteDiary() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
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
				.createDate(LocalDateTime.now())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diary);
		this.mvc.perform(delete("/diary/{id}/delete", 1L)
				.session(mockSession))
		.andExpect(redirectedUrl("/diary/list/" + user.getEmail()));
	}
	
	@Test
	public void deleteEmptyLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(delete("/diary/{id}/delete", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void deleteWhenNotMatchAuthority() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = User.builder()
				.id(1L)
				.email("test3@test.test")
				.password("test3")
				.name("테스트3")
				.profileImage("blank-profile-picture.png")
				.authority("ROLE_USER")
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
				.authority("ROLE_USER")
				.createDate(LocalDateTime.now())
				.build();
		Diary diary = Diary.builder()
				.id(1L)
				.writer(user2)
				.title("test")
				.contents("test")
				.thumbnail("no-image-icon.gif")
				.createDate(LocalDateTime.now())
				.build();
		when(this.diaryService.getOneDiary(1L))
		.thenReturn(diary);
		this.mvc.perform(delete("/diary/{id}/delete", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "삭제 권한이 없습니다."))
		.andExpect(model().size(1));
	}
	
}
