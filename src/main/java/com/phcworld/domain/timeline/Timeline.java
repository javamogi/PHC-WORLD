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

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.good.Good;
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
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_free_board"))
	private FreeBoard freeBoard;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_free_board_answer"))
	private FreeBoardAnswer freeBoardAnswer;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_diary"))
	private Diary diary;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_diary_answer"))
	private DiaryAnswer diaryAnswer;
	
	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_good"))
	private Good good;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_user"))
//	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private User user;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
	
	private LocalDateTime saveDate;

	public String getFormattedSaveDate() {
		return LocalDateTimeUtils.getTime(saveDate);
	}
	
	public String redirectUrl() {
		if(this.type.equals("diary")) {
			return "redirect:/diary/"+ this.getDiary().getId() + "/detail";
		}
		if(this.type.equals("diary answer")) {
			return "redirect:/diary/"+ this.getDiaryAnswer().getDiary().getId() + "/detail";
		}
		if(this.type.equals("free board")) {
			return "redirect:/freeboards/" + this.getFreeBoard().getId() + "/detail";
		}
		if(this.type.equals("good")) {
			return "redirect:/diary/"+ this.getGood().getDiary().getId() + "/detail";
		}
		return "redirect:/freeboards/" + this.getFreeBoardAnswer().getFreeBoard().getId() + "/detail";
	}

}
