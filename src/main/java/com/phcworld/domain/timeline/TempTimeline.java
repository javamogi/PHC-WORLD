package com.phcworld.domain.timeline;

import java.time.LocalDateTime;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.good.Good;
import com.phcworld.web.LocalDateTimeUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempTimeline {

	private String type;

	private String icon;
	
	private String url;

	private Diary diary;

	private DiaryAnswer diaryAnswer;

	private FreeBoard freeBoard;

	private FreeBoardAnswer freeBoardAnswer;
	
	private Good good;
	
	private LocalDateTime saveDate;

	public String getFormattedSaveDate() {
		return LocalDateTimeUtils.getTime(saveDate);
	}

}
