package com.phcworld.domain.answer;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;
import com.phcworld.web.LocalDateTimeUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ToString(exclude = {"timeline", "alert"})
public class DiaryAnswer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_diary_answer_writer"))
//	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private User writer;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_diary"), nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	// @JsonBackReference
	@JsonManagedReference
	private Diary diary;

	@Lob
	private String contents;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_diary_answer_timeline"))
	@JsonIgnore
	private Timeline timeline;

	// @OneToOne(cascade = CascadeType.REMOVE)
	@OneToOne(cascade = CascadeType.ALL)
	// @JoinColumn(foreignKey = @ForeignKey(name = "fk_diary_answer_alert"))
	@PrimaryKeyJoinColumn
	@JsonIgnore
	private Alert alert;

	private LocalDateTime createDate;

	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}

	public boolean isSameWriter(User loginUser) {
		return this.writer.equals(loginUser);
	}

}
