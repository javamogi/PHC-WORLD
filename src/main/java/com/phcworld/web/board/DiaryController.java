package com.phcworld.web.board;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.DiaryRequest;
import com.phcworld.domain.board.DiaryResponse;
import com.phcworld.domain.exception.LoginNotUserException;
import com.phcworld.domain.user.User;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.service.user.UserService;
import com.phcworld.utils.HttpSessionUtils;
import com.phcworld.utils.PageNationsUtil;

@Controller
@RequestMapping("/diaries")
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
			List<DiaryResponse> diaryResponseList = diaryService.getDiaryResponseList(diaries);
			model.addAttribute("diaries", diaryResponseList);
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
	public String create(DiaryRequest diaryRequest, HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User sessionUser = HttpSessionUtils.getUserFromSession(session);
		DiaryResponse response = diaryService.createDiary(sessionUser, diaryRequest);

		return "redirect:/diaries/" + response.getId();
	}

	@GetMapping("/{id}")
	public String read(@PathVariable Long id, HttpSession session, Model model) {
		boolean isLoginUser = false;
		boolean matchLoginUserAndWriter = false;
		boolean matchAuthority = false;
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		DiaryResponse diary = diaryService.getOneDiary(id);
		if (loginUser != null) {
			isLoginUser = true;
			if (diary != null) {
				if (diary.getWriter().equals(loginUser)) {
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
		DiaryResponse diary = diaryService.getOneDiary(id);
		if (!loginUser.matchId(diary.getWriter().getId())) {
			model.addAttribute("errorMessage", "본인의 작성한 글만 수정 가능합니다.");
			return "/user/login";
		}
		model.addAttribute("diary", diary);
		return "/board/diary/diary_updateForm";
	}

	@PatchMapping("")
	public String update(DiaryRequest diaryRequest, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		DiaryResponse diary = diaryService.getOneDiary(diaryRequest.getId());
		if (!loginUser.matchId(diary.getWriter().getId())) {
			model.addAttribute("errorMessage", "본인의 작성한 글만 수정 가능합니다.");
			return "/user/login";
		}
		diaryService.updateDiary(diaryRequest);
		return "redirect:/diaries/" + diaryRequest.getId();
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		DiaryResponse diary = diaryService.getOneDiary(id);
		if (!loginUser.matchId(diary.getWriter().getId()) && !loginUser.matchAdminAuthority()) {
			model.addAttribute("errorMessage", "삭제 권한이 없습니다.");
			return "/user/login";
		}
		diaryService.deleteDiary(id);
		return "redirect:/diaries/list/" + loginUser.getEmail();
	}
	
	@PutMapping("/{id}/good")
	public @ResponseBody SuccessResponse updateGoodCount(@PathVariable Long id, HttpSession session) 
			throws LoginNotUserException {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		return diaryService.updateGood(id, loginUser);
	}
}
