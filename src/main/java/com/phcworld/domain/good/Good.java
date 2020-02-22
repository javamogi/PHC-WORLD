package com.phcworld.domain.good;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.board.Diary;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;
import com.phcworld.web.LocalDateTimeUtils;

@Entity
public class Good {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_good_diary"))
	@JsonIgnore
	private Diary diary;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_good_user"))
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private User user;
	
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_good_timeline"))
	@JsonIgnore
	private Timeline timeline;
	
	@OneToOne(cascade = CascadeType.REMOVE)
//	@JoinColumn(foreignKey = @ForeignKey(name = "fk_good_alert"))
	@PrimaryKeyJoinColumn
	@JsonIgnore
	private Alert alert;

	private LocalDateTime saveDate;
	
	public Good() {
	}

	public Good(Diary diary, User user) {
		this.diary = diary;
		this.user = user;
		this.saveDate = LocalDateTime.now();
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
	
	public User getUser() {
		return user;
	}
	
	public void setTimeline(Timeline timeline) {
		this.timeline = timeline;
	}
	
	public void setAlert(Alert alert) {
		this.alert = alert;
	}
	
	public LocalDateTime getSaveDate() {
		return saveDate;
	}
	
	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(this.saveDate);
	}
	
}
