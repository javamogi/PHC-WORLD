package com.phcworld.web.board;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.phcworld.domain.board.FreeBoardServiceImpl;
import com.phcworld.domain.user.User;
import com.phcworld.web.HttpSessionUtils;

@Controller
@RequestMapping("/freeboard")
public class FreeBoardController {

	private static final Logger log = LoggerFactory.getLogger(FreeBoardController.class);

	@Autowired
	private FreeBoardServiceImpl freeBoardService;

	@GetMapping("/list")
	public String freeBoardList(Model model) {
		List<FreeBoard> list = freeBoardService.findFreeBoardAllList();
		for (int i = 0; i < list.size(); i++) {
			long minutesGap = Duration.between(list.get(i).getCreateDate(), LocalDateTime.now()).toMinutes();
			if (minutesGap / 60 < 24) {
				FreeBoard freeBoard = list.get(i);
				freeBoard.setBadge("New");
			}
		}

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
	public String create(String title, String contents, String icon, HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		log.debug("imgIcon : {}", icon);
		User sessionUser = HttpSessionUtils.getUserFromSession(session);

		freeBoardService.createFreeBoard(sessionUser, title, contents, icon);

		return "redirect:/freeboard/list";
	}

	@GetMapping("/{id}/detail")
	public String detail(@PathVariable Long id, HttpSession session, Model model) {
		boolean booleanUser = false;
		boolean matchUser = false;
		boolean matchAuthority = false;
		User loginUser = HttpSessionUtils.getUserFromSession(session);

		FreeBoard freeBoard = freeBoardService.addFreeBoardCount(id);

		model.addAttribute("freeBoard", freeBoard);
		if (loginUser != null) {
			booleanUser = true;
			if (freeBoard != null) {
				if (freeBoard.matchUser(loginUser)) {
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
		return "/board/freeboard/detail_freeboard";
	}

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
		return "/board/freeboard/freeBoard_updateForm";
	}

	@PutMapping("/{id}")
	public String modify(@PathVariable Long id, String contents, String icon, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		FreeBoard freeBoard = freeBoardService.getOneFreeBoard(id);
		if (!loginUser.matchId(freeBoard.getWriter().getId())) {
			model.addAttribute("errorMessage", "본인의 작성한 글만 수정 가능합니다.");
			return "/user/login";
		}

		freeBoardService.updateFreeBoard(freeBoard, contents, icon);
		return "redirect:/freeboard/" + id + "/detail";
	}

	@DeleteMapping("/{id}/delete")
	public String delete(@PathVariable Long id, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/user/login";
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		FreeBoard freeBoard = freeBoardService.getOneFreeBoard(id);
		if (!loginUser.matchId(freeBoard.getWriter().getId()) && !loginUser.matchAuthority()) {
			model.addAttribute("errorMessage", "삭제 권한이 없습니다.");
			return "/user/login";
		}
		freeBoardService.deleteFreeBoardById(id);
		return "redirect:/freeboard/list";
	}

}
