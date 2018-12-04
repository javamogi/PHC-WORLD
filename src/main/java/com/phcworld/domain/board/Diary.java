package com.phcworld.domain.board;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;
import com.phcworld.web.LocalDateTimeUtils;

@Entity
public class Diary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_dairy_writer"))
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private User writer;

	private String title;

	@Lob
	private String contents;

	private String thumbnail;

	private Integer countOfGood = 0;

	private Integer countOfAnswer = 0;

	@OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE)
	// @JsonManagedReference
	@JsonBackReference
	private List<DiaryAnswer> diaryAnswers;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_diary_timeline"))
	private Timeline timeline;

	@OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE)
	private List<Good> goods;

	private LocalDateTime createDate;

	public Diary() {
	}

	public Diary(User writer, String title, String contents, String thumbnail) {
		this.writer = writer;
		this.title = title;
		this.contents = contents;
		this.thumbnail = thumbnail;
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

	public String getTitle() {
		return title;
	}

	public String getContents() {
		return contents;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public Integer getCountOfGood() {
		return countOfGood;
	}

	public void setTimeline(Timeline timeline) {
		this.timeline = timeline;
	}
	
	@JsonIgnore
	public Timeline getTimeline() {
		return timeline;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}

	public String getCountOfAnswer() {
		if (this.countOfAnswer == 0) {
			return "";
		}
		return "[" + countOfAnswer + "]";
	}

	public List<DiaryAnswer> getDiaryAnswers() {
		return diaryAnswers;
	}

	public boolean matchUser(User loginUser) {
		if (loginUser == null) {
			return false;
		}
		return loginUser.equals(this.writer);
	}

	public void update(String newContents, String newThumbnail) {
		this.contents = newContents;
		this.thumbnail = newThumbnail;
	}

	public void addAnswer() {
		this.countOfAnswer += 1;
	}

	public void deleteAnswer() {
		this.countOfAnswer -= 1;
	}

	public void addGood() {
		this.countOfGood += 1;
	}

	public void minusGood() {
		this.countOfGood -= 1;
	}

	public boolean matchId(Long diaryId) {
		if (diaryId == null) {
			return false;
		}
		return this.id.equals(diaryId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contents == null) ? 0 : contents.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((thumbnail == null) ? 0 : thumbnail.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((writer == null) ? 0 : writer.hashCode());
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
		Diary other = (Diary) obj;
		if (contents == null) {
			if (other.contents != null)
				return false;
		} else if (!contents.equals(other.contents))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (thumbnail == null) {
			if (other.thumbnail != null)
				return false;
		} else if (!thumbnail.equals(other.thumbnail))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (writer == null) {
			if (other.writer != null)
				return false;
		} else if (!writer.equals(other.writer))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Diary [id=" + id + ", writer=" + writer + ", title=" + title + ", contents=" + contents + ", thumbnail="
				+ thumbnail + ", createDate=" + createDate + "]";
	}

}
