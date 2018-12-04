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
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.timeline.Timeline;
import com.phcworld.domain.user.User;
import com.phcworld.web.LocalDateTimeUtils;

@Entity
public class FreeBoard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_freeBoard_writer"))
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private User writer;

	private String title;

	@Lob
	private String contents;

	private String icon;

	private String badge;

	private LocalDateTime createDate;

	private Integer count = 0;

	private Integer countOfAnswer = 0;

	@OneToMany(mappedBy = "freeBoard", cascade = CascadeType.REMOVE)
	// @JsonManagedReference
	@JsonBackReference
	private List<FreeBoardAnswer> freeBoardAnswers;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_freeBoard_timeline"))
	// @PrimaryKeyJoinColumn
	private Timeline timeline;

	public FreeBoard() {
	}

	public FreeBoard(User writer, String title, String icon, String contents) {
		this.writer = writer;
		this.title = title;
		this.contents = contents;
		this.icon = icon;
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

	public String getIcon() {
		return icon;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public String getBadge() {
		return badge;
	}

	public String getCountOfAnswer() {
		if (this.countOfAnswer == 0) {
			return "";
		}
		return "[" + countOfAnswer + "]";
	}

	public List<FreeBoardAnswer> getFreeBoardAnswers() {
		return freeBoardAnswers;
	}

	public void setTimeline(Timeline timeline) {
		this.timeline = timeline;
	}

	@JsonIgnore
	public Timeline getTimeline() {
		return timeline;
	}

	public void addCount() {
		this.count += 1;
	}

	public Integer getCount() {
		return count;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}

	public void update(String newContents, String newIcon) {
		this.contents = newContents;
		this.icon = newIcon;
	}

	public void addAnswer() {
		this.countOfAnswer += 1;
	}

	public void deleteAnswer() {
		this.countOfAnswer -= 1;
	}

	public boolean matchUser(User loginUser) {
		if (loginUser == null) {
			return false;
		}
		return this.writer.equals(loginUser);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((badge == null) ? 0 : badge.hashCode());
		result = prime * result + ((contents == null) ? 0 : contents.hashCode());
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		result = prime * result + ((countOfAnswer == null) ? 0 : countOfAnswer.hashCode());
		result = prime * result + ((icon == null) ? 0 : icon.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		FreeBoard other = (FreeBoard) obj;
		if (badge == null) {
			if (other.badge != null)
				return false;
		} else if (!badge.equals(other.badge))
			return false;
		if (contents == null) {
			if (other.contents != null)
				return false;
		} else if (!contents.equals(other.contents))
			return false;
		if (count == null) {
			if (other.count != null)
				return false;
		} else if (!count.equals(other.count))
			return false;
		if (countOfAnswer == null) {
			if (other.countOfAnswer != null)
				return false;
		} else if (!countOfAnswer.equals(other.countOfAnswer))
			return false;
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

}
