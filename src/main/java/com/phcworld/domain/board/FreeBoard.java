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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phcworld.domain.answer.FreeBoardAnswer;
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
@ToString(exclude = {"timeline"})
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

	private Integer count;

	private Integer countOfAnswer;

	@OneToMany(mappedBy = "freeBoard", cascade = CascadeType.REMOVE)
	// @JsonManagedReference
	@JsonBackReference
	private List<FreeBoardAnswer> freeBoardAnswers;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_freeBoard_timeline"))
	// @PrimaryKeyJoinColumn
	private Timeline timeline;

	public String getCountOfAnswer() {
		if (this.countOfAnswer == 0) {
			return "";
		}
		return "[" + countOfAnswer + "]";
	}

	public void addCount() {
		this.count += 1;
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
	
}
