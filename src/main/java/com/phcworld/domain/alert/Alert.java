package com.phcworld.domain.alert;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.common.SaveType;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.utils.LocalDateTimeUtils;

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

	@Enumerated(EnumType.STRING)
	private SaveType saveType;

	private Long postId;
	private Long redirectId;

//	private String type;

//	@OneToOne
//	@JoinColumn(foreignKey = @ForeignKey(name = "fk_alert_good"))
//	private Good good;
//
//	@OneToOne
//	@JoinColumn(foreignKey = @ForeignKey(name = "fk_alert_diary_answer"))
//	private DiaryAnswer diaryAnswer;
//
//	@OneToOne
//	@JoinColumn(foreignKey = @ForeignKey(name = "fk_alert_free_board_answer"))
//	private FreeBoardAnswer freeBoardAnswer;

	@ManyToOne
	private User registerUser;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_alert_post_writer"))
	private User postWriter;
	
	private LocalDateTime createDate;

	private Boolean read;

	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}

	public String getRedirectUrl() {
		if(this.saveType == SaveType.DIARY_ANSWER
				|| this.saveType == SaveType.GOOD){
			return "redirect:/diaries/" + getRedirectId();
		}
		return "redirect:/freeboards/" + getRedirectId();
	}
}
