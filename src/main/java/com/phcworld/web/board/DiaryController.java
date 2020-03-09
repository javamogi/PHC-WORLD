package com.phcworld.web.board;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.exception.LoginNotUserException;
import com.phcworld.domain.user.User;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.service.user.UserService;
import com.phcworld.web.HttpSessionUtils;
import com.phcworld.web.PageNationsUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/diary")
@Slf4j
public class DiaryController {

	@Autowired
	private UserService userService;

	@Autowired
	private DiaryServiceImpl diaryService;

	@GetMapping("/list/{email}")
	public String getDairyList(@PathVariable String email, @RequestParam(defaultValue = "1") Integer pageNum, 
			Model model, HttpSession session) {
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		User requestUser = userService.findUserByEmail(email);

		Page<Diary> diaryPage = diaryService.findPageDiary(loginUser, pageNum, requestUser);

		if (diaryPage != null) {
			PageNationsUtil.viewPageNation("", pageNum, diaryPage.getTotalPages(), model);
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
	public String create(Diary diary, HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User sessionUser = HttpSessionUtils.getUserFromSession(session);
		diaryService.createDiary(sessionUser, diary);

		return "redirect:/diary/list/" + sessionUser.getEmail();
	}

	@GetMapping("/{id}/detail")
	public String read(@PathVariable Long id, HttpSession session, Model model) {
		boolean isLoginUser = false;
		boolean matchLoginUserAndWriter = false;
		boolean matchAuthority = false;
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		Diary diary = diaryService.getOneDiary(id);
		if (loginUser != null) {
			isLoginUser = true;
			if (diary != null) {
				if (diary.matchUser(loginUser)) {
					matchLoginUserAndWriter = true;
				}
			}
			if (matchLoginUserAndWriter == false && loginUser.matchAdminAuthority()) {
				matchAuthority = true;
			}
		}
		model.addAttribute("diary", diary);
		model.addAttribute("user", isLoginUser);
		model.addAttribute("matchUser", matchLoginUserAndWriter);
		model.addAttribute("matchAuthority", matchAuthority);
		return "/board/diary/detail_diary";
	}

	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
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
	public String update(@PathVariable Long id, Diary inputDiary, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		Diary diary = diaryService.getOneDiary(id);
		if (!loginUser.matchId(diary.getWriter().getId())) {
			model.addAttribute("errorMessage", "본인의 작성한 글만 수정 가능합니다.");
			return "/user/login";
		}
		diaryService.updateDiary(diary, inputDiary);
		return "redirect:/diary/" + id + "/detail";
	}

	@DeleteMapping("/{id}/delete")
	public String delete(@PathVariable Long id, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		Diary diary = diaryService.getOneDiary(id);
		if (!loginUser.matchId(diary.getWriter().getId()) && !loginUser.matchAdminAuthority()) {
			model.addAttribute("errorMessage", "삭제 권한이 없습니다.");
			return "/user/login";
		}
		diaryService.deleteDiary(diary);
		return "redirect:/diary/list/" + loginUser.getEmail();
	}
	
	@PutMapping("/{id}/good")
	public @ResponseBody String updateGoodCount(@PathVariable Long id, HttpSession session) throws LoginNotUserException {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		return diaryService.updateGood(id, loginUser);
	}
}
