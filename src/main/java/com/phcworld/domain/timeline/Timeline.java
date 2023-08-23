package com.phcworld.domain.timeline;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
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
public class Timeline {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

//	private String type;
	@Enumerated(EnumType.STRING)
	private SaveType saveType;

//	private String icon;

//	@OneToOne
//	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_free_board"))
//	private FreeBoard freeBoard;
//
//	@OneToOne
//	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_free_board_answer"))
//	private FreeBoardAnswer freeBoardAnswer;
//
//	@ManyToOne
//	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_diary"))
//	private Diary diary;
//
//	@OneToOne
//	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_diary_answer"))
//	private DiaryAnswer diaryAnswer;
//
//	@OneToOne
//	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_good"))
//	private Good good;

	private Long postId;

	private Long redirectId;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_user"))
//	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private User user;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
	
	private LocalDateTime saveDate;

	public String getFormattedSaveDate() {
		return LocalDateTimeUtils.getTime(saveDate);
	}
	
//	public String redirectUrl() {
//		if(this.type.equals("diary")) {
//			return "redirect:/diaries/"+ this.getDiary().getId();
//		}
//		if(this.type.equals("diary answer")) {
//			return "redirect:/diaries/"+ this.getDiaryAnswer().getDiary().getId();
//		}
//		if(this.type.equals("free board")) {
//			return "redirect:/freeboards/" + this.getFreeBoard().getId();
//		}
//		if(this.type.equals("good")) {
//			return "redirect:/diaries/"+ this.getGood().getDiary().getId();
//		}
//		return "redirect:/freeboards/" + this.getFreeBoardAnswer().getFreeBoard().getId();
//	}

	public String redirectUrl() {
		if (this.saveType == SaveType.DIARY
				|| this.saveType == SaveType.DIARY_ANSWER
				|| this.saveType == SaveType.GOOD){
			return "redirect:/diaries/"+ this.redirectId;
		}
		return "redirect:/freeboards/" + this.redirectId;
	}

}
