package com.phcworld.domain.alert;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
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
public class Alert {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String type;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_alert_diary"))
	private Diary diary;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_alert_diary_answer"))
	private DiaryAnswer diaryAnswer;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_alert_free_board_answer"))
	private FreeBoardAnswer freeBoardAnswer;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_alert_post_writer"))
	private User postWriter;
	
	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_alert_user"))
	private User registerUser;

	private LocalDateTime createDate;

	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}

	public boolean getIsDiaryNull() {
		if(getDiary() == null) {
			return false;
		}
		return true;
	}
}
