package com.phcworld.domain.alert;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.user.User;
import com.phcworld.web.LocalDateTimeUtils;

@Entity
public class Alert {
	// 다이어리 좋아요. 내가 작성한 글 댓글 알림

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String type;

	@OneToOne
	// @JoinColumn(foreignKey = @ForeignKey(name = "fk_alert_good"))
	@PrimaryKeyJoinColumn
	private Diary diary;

	@OneToOne
	// @JoinColumn(foreignKey = @ForeignKey(name = "fk_alert_diary_answer"))
	@PrimaryKeyJoinColumn
	private DiaryAnswer diaryAnswer;

	@OneToOne
	@PrimaryKeyJoinColumn
	private FreeBoardAnswer freeBoardAnswer;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_alert_writer"))
	private User postWriter;
	
	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_alert_user"))
	private User registerUser;

	private LocalDateTime saveDate;

	public Alert() {
	}

	public Alert(String type, Diary diary, User postWriter, User registerUser, LocalDateTime saveDate) {
		this.type = type;
		this.diary = diary;
		this.postWriter = postWriter;
		this.registerUser = registerUser;
		this.saveDate = saveDate;
	}

	public Alert(String type, DiaryAnswer diaryAnswer, User writer, LocalDateTime saveDate) {
		this.type = type;
		this.diaryAnswer = diaryAnswer;
		this.postWriter = writer;
		this.saveDate = saveDate;
	}

	public Alert(String type, FreeBoardAnswer freeBoardAnswer, User writer, LocalDateTime saveDate) {
		this.type = type;
		this.freeBoardAnswer = freeBoardAnswer;
		this.postWriter = writer;
		this.saveDate = saveDate;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	
	public Diary getDiary() {
		return diary;
	}

	public DiaryAnswer getDiaryAnswer() {
		return diaryAnswer;
	}

	public FreeBoardAnswer getFreeBoardAnswer() {
		return freeBoardAnswer;
	}

	public User getWriter() {
		return postWriter;
	}

	public String getFormattedSaveDate() {
		return LocalDateTimeUtils.getTime(saveDate);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diaryAnswer == null) ? 0 : diaryAnswer.hashCode());
		result = prime * result + ((freeBoardAnswer == null) ? 0 : freeBoardAnswer.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((postWriter == null) ? 0 : postWriter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alert other = (Alert) obj;
		if (diaryAnswer == null) {
			if (other.diaryAnswer != null)
				return false;
		} else if (!diaryAnswer.equals(other.diaryAnswer))
			return false;
		if (freeBoardAnswer == null) {
			if (other.freeBoardAnswer != null)
				return false;
		} else if (!freeBoardAnswer.equals(other.freeBoardAnswer))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (postWriter == null) {
			if (other.postWriter != null)
				return false;
		} else if (!postWriter.equals(other.postWriter))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Alert [id=" + id + ", type=" + type + ", diaryAnswer=" + diaryAnswer
				+ ", freeBoardAnswer=" + freeBoardAnswer + ", writer=" + postWriter + ", saveDate=" + saveDate + "]";
	}

}
