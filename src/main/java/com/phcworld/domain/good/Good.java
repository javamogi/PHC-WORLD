package com.phcworld.domain.good;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.user.User;
import com.phcworld.utils.LocalDateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(indexes = {@Index(name = "idx__diary_id", columnList = "diary_id"),
		@Index(name = "idx__writer_id", columnList = "user_id")},
uniqueConstraints = {
		@UniqueConstraint(
				name = "constraintGood",
				columnNames = {"diary_id", "user_id"})
})
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
