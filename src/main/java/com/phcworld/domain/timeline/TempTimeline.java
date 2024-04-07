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

import com.phcworld.domain.good.TempGood;
import com.phcworld.domain.parent.BasicBoardAndAnswer;
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
public class TempTimeline {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String type;

	private String icon;
	
	private String url;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_temptimeline_board"))
	private BasicBoardAndAnswer board;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_temptimeline_good"))
	private TempGood good;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_temptimeline_user"))
	private UserEntity user;
	
	private LocalDateTime saveDate;

	public String getFormattedSaveDate() {
		return LocalDateTimeUtils.getTime(saveDate);
	}
	
}
