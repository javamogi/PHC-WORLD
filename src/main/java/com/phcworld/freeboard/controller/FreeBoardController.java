package com.phcworld.freeboard.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.phcworld.freeboard.controller.port.FreeBoardSearchDto;
import com.phcworld.service.timeline.TimelineServiceImpl;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.controller.port.FreeBoardResponse;
import com.phcworld.freeboard.controller.port.FreeBoardService;
import com.phcworld.utils.HttpSessionUtils;

@Controller
@RequestMapping("/freeboards")
@RequiredArgsConstructor
public class FreeBoardController {

	private final FreeBoardService freeBoardService;
	private final TimelineServiceImpl timelineService;
	
	@GetMapping("/temp/test")
	public String getSearchResult(FreeBoardSearchDto search, Model model) {
		List<FreeBoardResponse> list = freeBoardService.getSearchResult(search);
		model.addAttribute("freeboards", list);
		return "board/freeboard/freeboard";
	}

	@GetMapping("/{id}/form")
	public String update(@PathVariable Long id, Model model, HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "user/login";
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		FreeBoardResponse freeBoard = freeBoardService.getOneFreeBoard(id);
		if (!loginUser.matchId(freeBoard.getWriter().getId())) {
			model.addAttribute("errorMessage", "본인의 작성한 글만 수정 가능합니다.");
			return "user/login";
		}
		model.addAttribute("freeBoard", freeBoard);
		return "board/freeboard/freeboard_updateForm";
	}

	@PatchMapping("")
	public String modify(FreeBoardRequest request, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "user/login";
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		FreeBoardResponse oneFreeBoard = freeBoardService.getOneFreeBoard(request.getId());
		if (!loginUser.matchId(oneFreeBoard.getWriter().getId())) {
			model.addAttribute("errorMessage", "본인의 작성한 글만 수정 가능합니다.");
			return "user/login";
		}
		
		freeBoardService.updateFreeBoard(request);
		return "redirect:/freeboards/" + request.getId();
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "user/login";
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		FreeBoardResponse freeBoard = freeBoardService.getOneFreeBoard(id);
		if (!loginUser.matchAdminAuthority() && !loginUser.matchId(freeBoard.getWriterId())) {
			model.addAttribute("errorMessage", "삭제 권한이 없습니다.");
			return "user/login";
		}
		freeBoardService.deleteFreeBoard(id);
		return "redirect:/freeboards";
	}

}
