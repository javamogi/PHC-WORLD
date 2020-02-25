package com.phcworld.domain.timeline;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.user.User;
import com.phcworld.web.LocalDateTimeUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Timeline {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String type;

	private String icon;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_freeBoard"))
	private FreeBoard freeBoard;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_freeBoard_Answer"))
	private FreeBoardAnswer freeBoardAnswer;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_diary"))
	private Diary diary;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_diary_answer"))
	private DiaryAnswer diaryAnswer;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_user"))
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private User user;

	private LocalDateTime saveDate;

	public String getFormattedSaveDate() {
		return LocalDateTimeUtils.getTime(saveDate);
	}

}
