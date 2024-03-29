package com.phcworld.domain.timeline;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.phcworld.domain.embedded.PostInfo;
import com.phcworld.user.infrastructure.UserEntity;
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

//	@Enumerated(EnumType.STRING)
//	private SaveType saveType;
//
//	private Long postId;
//
//	private Long redirectId;

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

	@Embedded
	private PostInfo postInfo;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_user"))
//	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private UserEntity user;
	
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
		return postInfo.getRedirectUrl();
	}

}
