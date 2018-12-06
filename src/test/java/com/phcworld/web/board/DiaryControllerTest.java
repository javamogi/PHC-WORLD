package com.phcworld.web.board;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryServiceImpl;
import com.phcworld.domain.user.User;
import com.phcworld.domain.user.UserService;
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
	public void whenNotMatchLoginUserDiaryList() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User requestUser = new User("test@test.test", "test", "테스트");
		requestUser.setId(2L);
		given(this.userService.findUserByEmail("test@test.test"))
		.willReturn(requestUser);
		PageRequest pageRequest = PageRequest.of(0, 6, new Sort(Direction.DESC, "id"));
		Diary diary = new Diary(requestUser, "test", "test", "no-image-icon.gif");
		diary.setId(1L);
		Diary diary2 = new Diary(requestUser, "test2", "test2", "no-image-icon.gif");
		diary2.setId(2L);
		List<Diary> diaryList = new ArrayList<Diary>();
		diaryList.add(diary);
		diaryList.add(diary2);
		Page<Diary> diaryPage = new PageImpl<Diary>(diaryList);
		given(this.diaryService.findPageDiaryByWriter(user, pageRequest, requestUser))
		.willReturn(diaryPage);
		List<Integer> pageNations = new ArrayList<Integer>();
		pageNations.add(1);
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
	public void whenEmptyLoginUserDiaryList() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User requestUser = new User("test@test.test", "test", "테스트");
		requestUser.setId(2L);
		given(this.userService.findUserByEmail("test@test.test"))
		.willReturn(requestUser);
		PageRequest pageRequest = PageRequest.of(0, 6, new Sort(Direction.DESC, "id"));
		Diary diary = new Diary(requestUser, "test", "test", "no-image-icon.gif");
		diary.setId(1L);
		Diary diary2 = new Diary(requestUser, "test2", "test2", "no-image-icon.gif");
		diary2.setId(2L);
		List<Diary> diaryList = new ArrayList<Diary>();
		diaryList.add(diary);
		diaryList.add(diary2);
		Page<Diary> diaryPage = new PageImpl<Diary>(diaryList);
		given(this.diaryService.findPageDiaryByWriter(user, pageRequest, requestUser))
		.willReturn(diaryPage);
		List<Integer> pageNations = new ArrayList<Integer>();
		pageNations.add(1);
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
	public void whenNotMatchLoginUserAndRequestUserDiaryList() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User requestUser = new User("test@test.test", "test", "테스트");
		requestUser.setId(2L);
		given(this.userService.findUserByEmail("test@test.test"))
		.willReturn(requestUser);
		PageRequest pageRequest = PageRequest.of(0, 6, new Sort(Direction.DESC, "id"));
		Diary diary = new Diary(requestUser, "test", "test", "no-image-icon.gif");
		diary.setId(1L);
		Diary diary2 = new Diary(requestUser, "test2", "test2", "no-image-icon.gif");
		diary2.setId(2L);
		List<Diary> diaryList = new ArrayList<Diary>();
		diaryList.add(diary);
		diaryList.add(diary2);
		Page<Diary> diaryPage = new PageImpl<Diary>(diaryList);
		given(this.diaryService.findPageDiaryByWriter(null, pageRequest, requestUser))
		.willReturn(diaryPage);
		List<Integer> pageNations = new ArrayList<Integer>();
		pageNations.add(1);
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
	public void whenNotLoginUserDiaryForm() throws Exception {
		this.mvc.perform(get("/diary/form"))
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void successDiaryForm() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
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
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
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
	public void createFailedNotLoginUserDiary() throws Exception {
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
	public void detailDairyWhenDiaryWriterEqualLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		Diary diary = new Diary(user, "test", "test", "no-image-icon.gif");
		diary.setId(1L);
		given(this.diaryService.getOneDiary(1L))
		.willReturn(diary);
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
	public void whenNotLoginUserDetailDairy() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		Diary diary = new Diary(user, "test", "test", "no-image-icon.gif");
		diary.setId(1L);
		given(this.diaryService.getOneDiary(1L))
		.willReturn(diary);
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
	public void detailDairyWhenDiaryWriterNotEqualLoginUser() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = new User("test4@test.test", "test4", "테스트4");
		user.setId(2L);
		Diary diary = new Diary(user2, "test", "test", "no-image-icon.gif");
		diary.setId(1L);
		given(this.diaryService.getOneDiary(1L))
		.willReturn(diary);
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
	public void detailDairyWhenHasAuthority() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		user.setAuthority("ROLE_ADMIN");
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = new User("test4@test.test", "test4", "테스트4");
		user.setId(2L);
		Diary diary = new Diary(user2, "test", "test", "no-image-icon.gif");
		diary.setId(1L);
		given(this.diaryService.getOneDiary(1L))
		.willReturn(diary);
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
	public void updateDiaryForm() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		Diary diary = new Diary(user, "test", "test", "no-image-icon.gif");
		diary.setId(1L);
		given(this.diaryService.getOneDiary(1L))
		.willReturn(diary);
		this.mvc.perform(get("/diary/{id}/form", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/board/diary/diary_updateForm")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("diary", diary))
		.andExpect(model().size(1));
	}
	
	@Test
	public void updateNotLoginUserDiaryForm() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		Diary diary = new Diary(user, "test", "test", "no-image-icon.gif");
		diary.setId(1L);
		given(this.diaryService.getOneDiary(1L))
		.willReturn(diary);
		this.mvc.perform(get("/diary/{id}/form", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void updateMatchNotUserDiaryForm() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = new User("test4@test.test", "test4", "테스트4");
		user.setId(2L);
		Diary diary = new Diary(user2, "test", "test", "no-image-icon.gif");
		diary.setId(1L);
		given(this.diaryService.getOneDiary(1L))
		.willReturn(diary);
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
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		Diary diary = new Diary(user, "test", "test", "no-image-icon.gif");
		diary.setId(1L);
		given(this.diaryService.getOneDiary(1L))
		.willReturn(diary);
		Diary updatedDiary = new Diary(user, "test", "updateTest", "no-image-icon.gif");
		diary.setId(1L);
		given(this.diaryService.updateDiary(diary, "updateTest", "no-image-icon.gif"))
		.willReturn(updatedDiary);
		this.mvc.perform(put("/diary/{id}", 1L)
				.param("contents", "updateTest")
				.param("thumbnail", "no-image-icon.gif")
				.session(mockSession))
		.andExpect(redirectedUrl("/diary/" + 1L + "/detail"));
	}
	
	@Test
	public void updateFailedNotLoginUserDiary() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		this.mvc.perform(put("/diary/{id}", 1L)
				.param("contents", "updateTest")
				.param("thumbnail", "no-image-icon.gif")
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void updateFailedNotMatchUserDiary() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = new User("test4@test.test", "test4", "테스트4");
		user.setId(2L);
		Diary diary = new Diary(user2, "test", "test", "no-image-icon.gif");
		diary.setId(1L);
		given(this.diaryService.getOneDiary(1L))
		.willReturn(diary);
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
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		Diary diary = new Diary(user, "test", "test", "no-image-icon.gif");
		diary.setId(1L);
		given(this.diaryService.getOneDiary(1L))
		.willReturn(diary);
		this.mvc.perform(delete("/diary/{id}/delete", 1L)
				.session(mockSession))
		.andExpect(redirectedUrl("/diary/list/" + user.getEmail()));
	}
	
	@Test
	public void deleteFailedNotLoginUserDiary() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		this.mvc.perform(delete("/diary/{id}/delete", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk());
	}
	
	@Test
	public void deleteFailedNotMatchAuthorityDiary() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User("test3@test.test", "test", "테스트");
		user.setId(1L);
		mockSession.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		mockSession.setAttribute("messages", null);
		mockSession.setAttribute("countMessages", "");
		mockSession.setAttribute("alerts", null);
		User user2 = new User("test4@test.test", "test4", "테스트4");
		user.setId(2L);
		Diary diary = new Diary(user2, "test", "test", "no-image-icon.gif");
		diary.setId(1L);
		given(this.diaryService.getOneDiary(1L))
		.willReturn(diary);
		this.mvc.perform(delete("/diary/{id}/delete", 1L)
				.session(mockSession))
		.andDo(print())
		.andExpect(view().name(containsString("/user/login")))
		.andExpect(status().isOk())
		.andExpect(model().attribute("errorMessage", "삭제 권한이 없습니다."))
		.andExpect(model().size(1));
	}
}
