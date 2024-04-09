package com.phcworld.freeboard.controller;

import com.phcworld.freeboard.controller.port.NewFreeBoardService;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.utils.HttpSessionUtils;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/freeboards")
@RequiredArgsConstructor
@Builder
public class NewFreeBoardController {

	private final NewFreeBoardService freeBoardService;

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

}
