package com.phcworld.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.alert.AlertServiceImpl;

@Controller
public class HomeController {
	
	@Autowired
	private AlertServiceImpl alertService;
	
	@GetMapping("")
	public String home() {
		return "index";
	}
	
	@RequestMapping("/alert/{id}")
	public String redirectToAlert(@PathVariable Long id) {
		Alert alert = alertService.getOneAlert(id);
		if(alert.getDiaryAnswer() != null) {
			return "redirect:/diary/"+ alert.getDiaryAnswer().getDiary().getId() + "/detail";
		}
		if(alert.getGood() != null) {
			return "redirect:/diary/"+ alert.getGood().getDiary().getId() + "/detail";
		}
		return "redirect:/freeboard/"+ alert.getFreeBoardAnswer().getFreeBoard().getId() + "/detail";
	}
	
}
