package com.phcworld.freeboard.controller.port;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.phcworld.answer.controller.port.FreeBoardAnswerResponse;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.user.infrastructure.UserEntity;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@Builder
public class FreeBoardResponse {
	
	private Long id;
	
	private UserEntity writer;
	
	private String title;
	
	private String contents;
	
	private String badge;
	
	private String createDate;
	
	private Integer count;
	
	private String countOfAnswer;
	
	private List<FreeBoardAnswerResponse> freeBoardAnswerList;

	public static FreeBoardResponse of(FreeBoard freeBoard){
		String badge = getBadge(freeBoard.getCreateDate());
		return FreeBoardResponse.builder()
				.id(freeBoard.getId())
				.writer(UserEntity.from(freeBoard.getWriter()))
				.title(freeBoard.getTitle())
				.contents(freeBoard.getContents())
				.badge(badge)
				.createDate(freeBoard.getFormattedCreateDate())
				.count(freeBoard.getCount())
				.countOfAnswer(freeBoard.getCountOfAnswer())
				.build();
	}

	@NotNull
	private static String getBadge(LocalDateTime freeBoard) {
		String badge = "";
		final int HOUR_OF_DAY = 24;
		final int MINUTES_OF_HOUR = 60;

		long createdDateAndNowDifferenceMinutes =
				Duration.between(freeBoard == null ?
						LocalDateTime.now() : freeBoard, LocalDateTime.now()).toMinutes();
		if (createdDateAndNowDifferenceMinutes / MINUTES_OF_HOUR < HOUR_OF_DAY) {
			badge = "New";
		}
		return badge;
	}

	public boolean matchUser(UserEntity loginUser) {
		return this.writer.equals(loginUser);
	}

}
