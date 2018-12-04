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
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;
import com.phcworld.web.LocalDateTimeUtils;

@Entity
public class FreeBoardAnswer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_freeBoardAnswer_writer"))
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private User writer;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_freeBoardAnswer_to_freeBoard"), nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	// @JsonBackReference
	@JsonManagedReference
	private FreeBoard freeBoard;

	@Lob
	private String contents;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_freeBoard_Answer_timeline"))
//	@PrimaryKeyJoinColumn
	private Timeline timeline;

	@OneToOne(cascade = CascadeType.REMOVE)
//	 @JoinColumn(foreignKey = @ForeignKey(name = "fk_journal_answer_alert"))
	@PrimaryKeyJoinColumn
	private Alert alert;

	private LocalDateTime createDate;

	public FreeBoardAnswer() {
	}

	public FreeBoardAnswer(User writer, FreeBoard freeBoard, String contents) {
		this.writer = writer;
		this.freeBoard = freeBoard;
		this.contents = contents;
		this.createDate = LocalDateTime.now();
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public User getWriter() {
		return writer;
	}

	public FreeBoard getFreeBoard() {
		return freeBoard;
	}

	public String getContents() {
		return contents;
	}

	public void setTimeline(Timeline timeline) {
		this.timeline = timeline;
	}
	
	@JsonIgnore
	public Timeline getTimeline() {
		return timeline;
	}
	
	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}

	public boolean isSameWriter(User loginUser) {
		return this.writer.equals(loginUser);
	}

	@Override
	public String toString() {
		return "FreeBoardAnswer [writer=" + writer + ", freeBoard=" + freeBoard + ", contents=" + contents
				+ ", createDate=" + createDate + "]";
	}

}
