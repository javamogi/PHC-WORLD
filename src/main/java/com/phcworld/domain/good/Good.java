package com.phcworld.domain.good;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.user.User;
import com.phcworld.utils.LocalDateTimeUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(indexes = @Index(name = "idx__diary_id", columnList = "diary_id"))
public class Good {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_good_diary"))
	private Diary diary;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_good_user"))
	private User user;
	
	private LocalDateTime createDate;
	
	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}
}
