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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.board.FreeBoard;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.web.LocalDateTimeUtils;

@Entity
public class Timeline {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String type;

	private String icon;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_freeBoard"))
	private FreeBoard freeBoard;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_freeBoard_Answer"))
	private FreeBoardAnswer freeBoardAnswer;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_diary"))
	private Diary diary;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_diary_answer"))
	private DiaryAnswer diaryAnswer;

	@OneToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_good"))
	private Good good;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_timeline_user"))
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private User user;

	private LocalDateTime saveDate;

	public Timeline() {
	}

	public Timeline(String type, String icon, FreeBoard freeBoard, User user, LocalDateTime saveDate) {
		this.type = type;
		this.icon = icon;
		this.freeBoard = freeBoard;
		this.user = user;
		this.saveDate = LocalDateTime.now();
	}

	public Timeline(String type, String icon, Diary diary, User user, LocalDateTime saveDate) {
		this.type = type;
		this.icon = icon;
		this.diary = diary;
		this.user = user;
		this.saveDate = LocalDateTime.now();
	}

	public Timeline(String type, String icon, FreeBoard freeBoard, FreeBoardAnswer freeBoardAnswer, User user,
			LocalDateTime saveDate) {
		this.type = type;
		this.icon = icon;
		this.freeBoard = freeBoard;
		this.freeBoardAnswer = freeBoardAnswer;
		this.user = user;
		this.saveDate = LocalDateTime.now();
	}

	public Timeline(String type, String icon, Diary diary, DiaryAnswer diaryAnswer, User user, LocalDateTime saveDate) {
		this.type = type;
		this.icon = icon;
		this.diary = diary;
		this.diaryAnswer = diaryAnswer;
		this.user = user;
		this.saveDate = LocalDateTime.now();
	}

	public Timeline(String type, String icon, Diary diary, Good good, User user, LocalDateTime saveDate) {
		this.type = type;
		this.icon = icon;
		this.diary = diary;
		this.good = good;
		this.user = user;
		this.saveDate = LocalDateTime.now();
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getIcon() {
		return icon;
	}

	public FreeBoard getFreeBoard() {
		return freeBoard;
	}

	public FreeBoardAnswer getFreeBoardAnswer() {
		return freeBoardAnswer;
	}

	public Diary getDiary() {
		return diary;
	}

	public DiaryAnswer getDiaryAnswer() {
		return diaryAnswer;
	}

	public Good getGood() {
		return good;
	}

	public User getUser() {
		return user;
	}

	public String getFormattedSaveDate() {
		return LocalDateTimeUtils.getTime(saveDate);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((icon == null) ? 0 : icon.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Timeline other = (Timeline) obj;
		if (icon == null) {
			if (other.icon != null)
				return false;
		} else if (!icon.equals(other.icon))
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
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}
