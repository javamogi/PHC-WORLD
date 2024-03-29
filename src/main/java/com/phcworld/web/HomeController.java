package com.phcworld.web;

import com.phcworld.domain.common.SaveType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.phcworld.domain.alert.Alert;
import com.phcworld.service.alert.AlertService;
import com.phcworld.service.alert.AlertServiceImpl;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final AlertService alertService;
	
	@GetMapping("")
	public String home() {
		return "index";
	}
	
	@RequestMapping("/alert/{id}")
	public String redirectToAlert(@PathVariable Long id) {
		Alert alert = alertService.getOneAlert(id);
//		if(alert.getDiaryAnswer() != null) {
//			return "redirect:/diaries/"+ alert.getDiaryAnswer().getDiary().getId();
//		}
//		if(alert.getGood() != null) {
//			return "redirect:/diaries/"+ alert.getGood().getDiary().getId();
//		}
//		return "redirect:/freeboards/"+ alert.getFreeBoardAnswer().getFreeBoard().getId();

//		if(alert.getSaveType() == SaveType.DIARY_ANSWER
//				|| alert.getSaveType() == SaveType.GOOD){
//			return "redirect:/diaries/" + alert.getRedirectId();
//		}
//		return "redirect:/freeboards/" + alert.getRedirectId();
		return alert.getRedirectUrl();
	}
	
}
