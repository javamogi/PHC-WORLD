package com.phcworld.web.board;

import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.domain.board.dto.DiaryRequest;
import com.phcworld.domain.board.dto.DiaryResponse;
import com.phcworld.domain.board.dto.DiaryResponseDto;
import com.phcworld.domain.exception.LoginNotUserException;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.repository.board.dto.DiarySelectDto;
import com.phcworld.service.board.DiaryServiceImpl;
import com.phcworld.user.service.UserServiceImpl;
import com.phcworld.utils.HttpSessionUtils;
import com.phcworld.utils.PageNationsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/diaries")
@RequiredArgsConstructor
public class DiaryController {

	private final UserServiceImpl userService;

	private final DiaryServiceImpl diaryService;

	@GetMapping("/list/{email}")
	public String getDairyList(@PathVariable String email,
							   @RequestParam(defaultValue = "1") Integer pageNum,
			@RequestParam(defaultValue = "") String searchKeyword,
			Model model, HttpSession session) {
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		UserEntity requestUser = userService.findUserByEmail(email);

		Page<DiarySelectDto> diaryPage = diaryService.findPageDiary(loginUser, pageNum, requestUser, searchKeyword);

		if (diaryPage != null) {
			PageNationsUtil.viewPageNation("", pageNum, diaryPage.getTotalPages(), model);
			List<DiarySelectDto> diaries = diaryPage.getContent();
			List<DiaryResponse> diaryResponseList = diaries.stream()
					.map(DiaryResponse::of)
					.collect(Collectors.toList());
			model.addAttribute("diaries", diaryResponseList);
		}

		model.addAttribute("requestUser", requestUser);

		return "board/diary/diary";
	}

	@GetMapping("/list/{email}/temp")
	public String getDairyListTmp(@PathVariable String email, @RequestParam(defaultValue = "1") Integer pageNum,
							   @RequestParam(defaultValue = "") String searchKeyword,
							   Model model, HttpSession session) {

		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		UserEntity requestUser = userService.findUserByEmail(email);
		DiaryResponseDto dto = diaryService.getDiaryResponseListTemp(loginUser, email, pageNum, searchKeyword);

		if (dto != null) {
			PageNationsUtil.viewPageNation("", pageNum, dto.getTotalPages(), model);
			model.addAttribute("diaries", dto.getDiaries());
		}

		model.addAttribute("requestUser", requestUser);

		return "board/diary/diary";
	}

	@GetMapping("/list/{email}/temp2")
	public String getDairyListTmp2(@PathVariable String email, @RequestParam(defaultValue = "1") Integer pageNum,
							   @RequestParam(defaultValue = "") String searchKeyword, Model model) {
		UserEntity requestUser = userService.findUserByEmail(email);
		DiaryResponseDto dto = diaryService.findPageDiaryTemp2(requestUser, pageNum, searchKeyword);

		if (!dto.getDiaries().isEmpty()) {
			PageNationsUtil.viewPageNation("", pageNum, dto.getTotalPages(), model);
			model.addAttribute("diaries", dto.getDiaries());
		}

		model.addAttribute("requestUser", requestUser);

		return "board/diary/diary";
	}

	@GetMapping("/form")
	public String form(HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "user/login";
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		model.addAttribute("user", loginUser);
		return "board/diary/diary_form";
	}

	@PostMapping("")
	public String create(DiaryRequest diaryRequest, HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "user/login";
		}
		UserEntity sessionUser = HttpSessionUtils.getUserEntityFromSession(session);
		DiaryResponse response = diaryService.createDiary(sessionUser, diaryRequest);

		return "redirect:/diaries/" + response.getId();
	}

	@GetMapping("/{id}")
	public String read(@PathVariable Long id, HttpSession session, Model model) {
		boolean isLoginUser = false;
		boolean matchLoginUserAndWriter = false;
		boolean matchAuthority = false;
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
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
		return "board/diary/detail_diary";
	}

	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "user/login";
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		DiaryResponse diary = diaryService.getOneDiary(id);
		if (!loginUser.matchId(diary.getWriter().getId())) {
			model.addAttribute("errorMessage", "본인의 작성한 글만 수정 가능합니다.");
			return "user/login";
		}
		model.addAttribute("diary", diary);
		return "board/diary/diary_updateForm";
	}

	@PatchMapping("")
	public String update(DiaryRequest diaryRequest, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "user/login";
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		DiaryResponse diary = diaryService.getOneDiary(diaryRequest.getId());
		if (!loginUser.matchId(diary.getWriter().getId())) {
			model.addAttribute("errorMessage", "본인의 작성한 글만 수정 가능합니다.");
			return "user/login";
		}
		diaryService.updateDiary(diaryRequest);
		return "redirect:/diaries/" + diaryRequest.getId();
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "user/login";
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		DiaryResponse diary = diaryService.getOneDiary(id);
		if (!loginUser.matchId(diary.getWriter().getId()) && !loginUser.matchAdminAuthority()) {
			model.addAttribute("errorMessage", "삭제 권한이 없습니다.");
			return "user/login";
		}
		diaryService.deleteDiary(id);
		return "redirect:/diaries/list/" + loginUser.getEmail();
	}
	
	@PutMapping("/{id}/good")
	public @ResponseBody SuccessResponse updateGoodCount(@PathVariable Long id, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new LoginNotUserException("로그인을 해야합니다.");
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		return diaryService.updateGood(id, loginUser);
	}
}
