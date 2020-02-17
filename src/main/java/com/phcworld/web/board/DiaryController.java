package com.phcworld.web.board;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryServiceImpl;
import com.phcworld.domain.user.User;
import com.phcworld.service.user.UserService;
import com.phcworld.web.HttpSessionUtils;
import com.phcworld.web.PageNationsUtil;

@Controller
@RequestMapping("/diary")
public class DiaryController {

	@Autowired
	private UserService userService;

	@Autowired
	private DiaryServiceImpl diaryService;

	@GetMapping("/list/{email}")
	public String dairy(@PathVariable String email, @RequestParam(defaultValue = "1") Integer pageNum, Model model,
			HttpSession session) {
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		User requestUser = userService.findUserByEmail(email);

		PageRequest pageRequest = PageRequest.of(pageNum - 1, 6, new Sort(Direction.DESC, "id"));
		Page<Diary> diaryPage = diaryService.findPageDiaryByWriter(loginUser, pageRequest, requestUser);

		PageNationsUtil pageNation = new PageNationsUtil();
		if (diaryPage != null) {
			pageNation.viewPageNation("", pageNum, diaryPage.getTotalPages(), model);
			List<Diary> diaries = diaryPage.getContent();
			model.addAttribute("diaries", diaries);
		}

		model.addAttribute("requestUser", requestUser);

		return "/board/diary/diary";
	}

	@GetMapping("/form")
	public String form(HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		model.addAttribute("user", loginUser);
		return "/board/diary/diary_form";
	}

	@PostMapping("")
	public String create(String title, String contents, String thumbnail, HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User sessionUser = HttpSessionUtils.getUserFromSession(session);

		diaryService.createDiary(sessionUser, title, contents, thumbnail);

		return "redirect:/diary/list/" + sessionUser.getEmail();
	}

	@GetMapping("/{id}/detail")
	public String detail(@PathVariable Long id, HttpSession session, Model model) {
		boolean booleanUser = false;
		boolean matchUser = false;
		boolean matchAuthority = false;
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		Diary diary = diaryService.getOneDiary(id);
		model.addAttribute("diary", diary);
		if (loginUser != null) {
			booleanUser = true;
			if (diary != null) {
				if (diary.matchUser(loginUser)) {
					matchUser = true;
				}
			}
			if (matchUser == false && loginUser.matchAuthority()) {
				matchAuthority = true;
			}
		}
		model.addAttribute("user", booleanUser);
		model.addAttribute("matchUser", matchUser);
		model.addAttribute("matchAuthority", matchAuthority);
		return "/board/diary/detail_diary";
	}

	@GetMapping("/{id}/form")
	public String update(@PathVariable Long id, Model model, HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		Diary diary = diaryService.getOneDiary(id);
		if (!loginUser.matchId(diary.getWriter().getId())) {
			model.addAttribute("errorMessage", "본인의 작성한 글만 수정 가능합니다.");
			return "/user/login";
		}
		model.addAttribute("diary", diary);
		return "/board/diary/diary_updateForm";
	}

	@PutMapping("/{id}")
	public String modify(@PathVariable Long id, String contents, String thumbnail, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		Diary diary = diaryService.getOneDiary(id);
		if (!loginUser.matchId(diary.getWriter().getId())) {
			model.addAttribute("errorMessage", "본인의 작성한 글만 수정 가능합니다.");
			return "/user/login";
		}
		diaryService.updateDiary(diary, contents, thumbnail);
		return "redirect:/diary/" + id + "/detail";
	}

	@DeleteMapping("/{id}/delete")
	public String delete(@PathVariable Long id, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		Diary diary = diaryService.getOneDiary(id);
		if (!loginUser.matchId(diary.getWriter().getId()) && !loginUser.matchAuthority()) {
			model.addAttribute("errorMessage", "삭제 권한이 없습니다.");
			return "/user/login";
		}
		diaryService.deleteDiaryById(id);
		return "redirect:/diary/list/" + loginUser.getEmail();
	}
}
