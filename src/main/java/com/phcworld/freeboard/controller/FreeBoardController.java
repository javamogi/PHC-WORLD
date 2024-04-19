package com.phcworld.freeboard.controller;

import com.phcworld.freeboard.controller.port.FreeBoardResponse;
import com.phcworld.freeboard.controller.port.FreeBoardResponseWithAuthority;
import com.phcworld.freeboard.controller.port.FreeBoardService;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.domain.dto.FreeBoardUpdateRequest;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.utils.HttpSessionUtils;
import com.phcworld.utils.PageNationsUtil;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/freeboards")
@RequiredArgsConstructor
@Builder
public class FreeBoardController {

	private final FreeBoardService freeBoardService;

	@GetMapping("/form")
	public String form(HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "user/login";
		}
		return "board/freeboard/freeboard_form";
	}

	@PostMapping("")
	public String register(FreeBoardRequest request, HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "user/login";
		}
		UserEntity sessionUser = HttpSessionUtils.getUserEntityFromSession(session);
		
		FreeBoard freeBoard = freeBoardService.register(request, sessionUser.toModel());

		return "redirect:/freeboards/"+ freeBoard.getId();
	}

	@GetMapping("")
	public String getAllList(Model model) {
		List<FreeBoardResponse> list = freeBoardService.findAllList()
				.stream()
				.map(FreeBoardResponse::of)
				.collect(Collectors.toList());
		model.addAttribute("freeboards", list);
		return "board/freeboard/freeboard";
	}

	@GetMapping("/{id}")
	public String read(@PathVariable Long id,
					   @RequestParam(defaultValue = "1") int pageNum,
					   HttpSession session,
					   Model model) {
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);

		FreeBoardResponseWithAuthority freeBoard =
				FreeBoardResponseWithAuthority.from(freeBoardService.addReadCount(id, pageNum), loginUser);

		if(!freeBoard.getCountOfAnswer().isEmpty()){
			PageNationsUtil.viewPageNation("", pageNum, freeBoard.getTotalOfPage(), model);
			model.addAttribute("isPagination", true);
		}
		model.addAttribute("freeBoard", freeBoard);
		return "board/freeboard/detail_freeboard";
	}

	@GetMapping("/{id}/form")
	public String modifyForm(@PathVariable Long id, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "user/login";
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		FreeBoard freeBoard = freeBoardService.getFreeBoard(id, loginUser);
		model.addAttribute("freeBoard", FreeBoardResponse.of(freeBoard));
		return "board/freeboard/freeboard_updateForm";
	}

	@PatchMapping("")
	public String modify(FreeBoardUpdateRequest request, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "user/login";
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		FreeBoard freeBoard = freeBoardService.update(request, loginUser);
		return "redirect:/freeboards/" + freeBoard.getId();
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id, HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "user/login";
		}
		UserEntity loginUser = HttpSessionUtils.getUserEntityFromSession(session);
		freeBoardService.delete(id, loginUser);
		return "redirect:/freeboards";
	}

}
