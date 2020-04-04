package com.phcworld.web.board;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;
import com.phcworld.service.board.FreeBoardServiceImpl;
import com.phcworld.utils.HttpSessionUtils;

@Controller
@RequestMapping("/freeboards")
public class FreeBoardController {

	@Autowired
	private FreeBoardServiceImpl freeBoardService;
	
	@GetMapping("/list")
	public String getFreeBoardAllList(Model model) {
		List<FreeBoard> list = freeBoardService.findFreeBoardAllListAndSetNewBadge();
		model.addAttribute("freeboards", list);
		return "/board/freeboard/freeboard";
	}

	@GetMapping("/form")
	public String form(HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		return "/board/freeboard/freeboard_form";
	}

	@PostMapping("")
	public String create(FreeBoard freeBoard, HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User sessionUser = HttpSessionUtils.getUserFromSession(session);
		
		FreeBoard createdFreeBoard = freeBoardService.createFreeBoard(sessionUser, freeBoard);
		
		return "redirect:/freeboards/"+ createdFreeBoard.getId() + "/detail";
	}

	@GetMapping("/{id}/detail")
	public String read(@PathVariable Long id, HttpSession session, Model model) {
		boolean isLoginUser = false;
		boolean matchLoginUserAndWriter = false;
		boolean matchAuthority = false;
		User loginUser = HttpSessionUtils.getUserFromSession(session);

		FreeBoard freeBoard = freeBoardService.addFreeBoardCount(id);

		// checkAdminAndWiter
		if (loginUser != null) {
			isLoginUser = true;
			if (freeBoard != null) {
				if (freeBoard.matchUser(loginUser)) {
					matchLoginUserAndWriter = true;
				}
			}
			if (matchLoginUserAndWriter == false && loginUser.matchAdminAuthority()) {
				matchAuthority = true;
			}
		}
		
		model.addAttribute("freeBoard", freeBoard);
		model.addAttribute("user", isLoginUser);
		model.addAttribute("matchUser", matchLoginUserAndWriter);
		model.addAttribute("matchAuthority", matchAuthority);
		return "/board/freeboard/detail_freeboard";
	}

//	@GetMapping("/{id}/form")
//	public String update(@PathVariable Long id, Model model, HttpSession session) {
//		if (!HttpSessionUtils.isLoginUser(session)) {
//			return "/user/login";
//		}
//		User loginUser = HttpSessionUtils.getUserFromSession(session);
//		FreeBoardResponse freeBoard = freeBoardService.getOneFreeBoard(id);
//		if (!loginUser.matchId(freeBoard.getWriter().getId())) {
//			model.addAttribute("errorMessage", "본인의 작성한 글만 수정 가능합니다.");
//			return "/user/login";
//		}
//		model.addAttribute("freeBoard", freeBoard);
//		return "/board/freeboard/freeboard_updateForm";
//	}
	@GetMapping("/{id}/form")
	public String update(@PathVariable Long id, Model model, HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		FreeBoard freeBoard = freeBoardService.getOneFreeBoard(id);
		if (!loginUser.matchId(freeBoard.getWriter().getId())) {
			model.addAttribute("errorMessage", "본인의 작성한 글만 수정 가능합니다.");
			return "/user/login";
		}
		model.addAttribute("freeBoard", freeBoard);
		return "/board/freeboard/freeboard_updateForm";
	}

	@PutMapping("/{id}")
	public String modify(@PathVariable Long id, FreeBoard freeBoard, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		FreeBoard oneFreeBoard = freeBoardService.getOneFreeBoard(id);
		if (!loginUser.matchId(oneFreeBoard.getWriter().getId())) {
			model.addAttribute("errorMessage", "본인의 작성한 글만 수정 가능합니다.");
			return "/user/login";
		}
		
		freeBoardService.updateFreeBoard(freeBoard);
		return "redirect:/freeboards/" + id + "/detail";
	}

	@DeleteMapping("/{id}/delete")
	public String delete(@PathVariable Long id, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		FreeBoard freeBoard = freeBoardService.getOneFreeBoard(id);
		if (!loginUser.matchId(freeBoard.getWriter().getId()) && !loginUser.matchAdminAuthority()) {
			model.addAttribute("errorMessage", "삭제 권한이 없습니다.");
			return "/user/login";
		}
		freeBoardService.deleteFreeBoard(freeBoard);
		return "redirect:/freeboards/list";
	}

}
