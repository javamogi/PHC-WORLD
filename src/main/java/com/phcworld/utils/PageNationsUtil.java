package com.phcworld.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;

public class PageNationsUtil {
	
	public static void viewPageNation(String pageName, Integer pageNum, int totalPages, Model model) {
		int tempPage = 0, startPage = 0, endPage = 0, pageSize = 10;
		tempPage = (pageNum - 1) % pageSize;
		startPage = pageNum - tempPage;
		endPage = (startPage + pageSize) - 1;
		if(endPage > totalPages) {
			if(totalPages == 0) {
				endPage = 1;
			} else {
				endPage = totalPages;
			}
		}
		List<Integer> pageNations = new ArrayList<Integer>();
		for(int i = startPage; i < endPage+1; i++) {
			pageNations.add(i);
		}
		int nextPage, previousPage;
		if(startPage > 1) {
			previousPage = startPage - pageSize;
			model.addAttribute(pageName + "previousPage", previousPage);
		}
		if(endPage < totalPages) {
			nextPage = endPage + 1;
			model.addAttribute(pageName + "nextPage", nextPage);
		}
		
		model.addAttribute(pageName + "pageNations", pageNations);
	}
}
