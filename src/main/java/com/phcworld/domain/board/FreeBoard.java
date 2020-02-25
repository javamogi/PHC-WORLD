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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.user.User;
import com.phcworld.web.LocalDateTimeUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
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

	@OneToMany(mappedBy = "freeBoard", cascade = CascadeType.REMOVE)
	// @JsonManagedReference
	@JsonBackReference
	private List<FreeBoardAnswer> freeBoardAnswers;

	public String getCountOfAnswer() {
		if (this.freeBoardAnswers.size() == 0) {
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

	public void update(String newContents, String newIcon) {
		this.contents = newContents;
		this.icon = newIcon;
	}

	public boolean matchUser(User loginUser) {
		if (loginUser == null) {
			return false;
		}
		return this.writer.equals(loginUser);
	}
	
}
