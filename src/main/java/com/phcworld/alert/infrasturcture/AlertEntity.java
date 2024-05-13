package com.phcworld.alert.infrasturcture;

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
public class AlertEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

//	@Enumerated(EnumType.STRING)
//	private SaveType saveType;
//
//	private Long postId;
//	private Long redirectId;

	@Embedded
	private PostInfo postInfo;

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
	private UserEntity registerUser;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_alert_post_writer"))
	private UserEntity postWriter;
	
	private LocalDateTime createDate;

	private Boolean isRead;

	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}

	public String getRedirectUrl() {
		return postInfo.getRedirectUrl();
	}
}
