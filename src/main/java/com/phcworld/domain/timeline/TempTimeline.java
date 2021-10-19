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

import com.phcworld.domain.parent.BasicBoardAndAnswer;
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
public class TempTimeline {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String type;

	private String icon;
	
	private String url;

//	@OneToOne
//	@JoinColumn(foreignKey = @ForeignKey(name = "fk_temptimeline_board"))
//	private BasicBoard board;

//	private DiaryAnswer diaryAnswer;
//
//	private FreeBoardAnswer freeBoardAnswer;
//	
//	private Good good;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_temptimeline_user"))
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

}
