package com.phcworld.domain.board;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.user.User;
import com.phcworld.utils.LocalDateTimeUtils;

import lombok.experimental.Accessors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = {"writer", "freeBoardAnswers"})
//@Table(name = "free_board", indexes = @Index(name = "idx__create_date", columnList = "createDate"))
//@SequenceGenerator(
//		name = "BOARD_SEQ_GENERATOR",
//		sequenceName = "BOARD_SEQ",
//		initialValue = 1, allocationSize = 10000
//)
public class FreeBoard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GENERATOR")
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_freeBoard_writer"))
//	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private User writer;

	private String title;

	@Lob
//	@Column(nullable = true, columnDefinition = "TEXT")
	private String contents;

	private String icon;

	private String badge;

	@CreatedDate
	private LocalDateTime createDate;
	
	@LastModifiedDate
	private LocalDateTime updateDate;
	
	private Integer count;

	@OneToMany(mappedBy = "freeBoard", cascade = CascadeType.REMOVE)
	// @JsonManagedReference
	@JsonBackReference
	private List<FreeBoardAnswer> freeBoardAnswers;

	public String getCountOfAnswer() {
		if (this.freeBoardAnswers == null|| this.freeBoardAnswers.size() == 0) {
			return "";
		}
		return "[" + freeBoardAnswers.size() + "]";
	}

	public void addCount() {
		this.count += 1;
	}

	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}
	
	public String getFormattedUpdateDate() {
		return LocalDateTimeUtils.getTime(updateDate);
	}

	public void update(FreeBoardRequest request) {
		this.contents = request.getContents();
		this.icon = request.getIcon();
	}

	public boolean matchUser(User loginUser) {
		if (loginUser == null) {
			return false;
		}
		return this.writer.equals(loginUser);
	}

	public String getBadge(){
		final int HOUR_OF_DAY = 24;
		final int MINUTES_OF_HOUR = 60;

		long createdDateAndNowDifferenceMinutes =
				Duration.between(createDate == null ? LocalDateTime.now() : createDate, LocalDateTime.now()).toMinutes();
		if (createdDateAndNowDifferenceMinutes / MINUTES_OF_HOUR < HOUR_OF_DAY) {
			badge = "New";
		} else {
			badge = "";
		}
		return badge;
	}
	
}
