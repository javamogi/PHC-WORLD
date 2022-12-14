package com.phcworld.domain.timeline;

import org.springframework.stereotype.Component;

import com.phcworld.domain.good.TempGood;
import com.phcworld.domain.parent.BasicBoardAndAnswer;

@Component
public class TempTimelineFactory {

	public TempTimeline createTimeline(String type, BasicBoardAndAnswer board, Long urlId) {
		TempTimeline timeline = null;
		String url = getRedirectUrl(type, urlId);
		if (type.equals("free board")) {
			timeline = TempTimeline.builder()
					.type(type)
					.icon("list-alt")
					.url(url)
					.board(board)
					.user(board.getWriter())
					.saveDate(board.getCreateDate())
					.build();
		} else if (type.equals("diary")) {
			timeline = TempTimeline.builder()
					.type(type)
					.icon("edit")
					.url(url)
					.board(board)
					.user(board.getWriter())
					.saveDate(board.getCreateDate())
					.build();
		} else if (type.equals("freeBoard answer")) {
			timeline = TempTimeline.builder()
					.type(type)
					.icon("comment")
					.url(url)
					.board(board)
					.user(board.getWriter())
					.saveDate(board.getCreateDate())
					.build();
		} else if (type.equals("diary answer")) {
			timeline = TempTimeline.builder()
					.type(type)
					.icon("comment")
					.url(url)
					.board(board)
					.user(board.getWriter())
					.saveDate(board.getCreateDate())
					.build();
		}
		return timeline;
	}
	
	public TempTimeline createTimeline(String type, TempGood good, Long urlId) {
		TempTimeline timeline = null;
		String url = getRedirectUrl(type, urlId);
		timeline = TempTimeline.builder()
				.type(type)
				.icon("thumbs-up")
				.url(url)
				.good(good)
				.user(good.getUser())
				.saveDate(good.getCreateDate())
				.build();
		return timeline;
	}

	private String getRedirectUrl(String type, Long id) {
		StringBuilder sb = new StringBuilder();
		sb.append("redirect:/");
		if (type.equals("diary") || type.equals("diary answer")) {
			sb.append("diaries/");
		}
		if (type.equals("free board") || type.equals("freeBoard answer")) {
			sb.append("freeboards/");
		}
//		if (type.equals("good")) {
//			return "redirect:/diaries/" + this.getGood().getDiary().getId();
//		}
		sb.append(id);
		return sb.toString();
	}

}
