package com.phcworld.domain.answer;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.phcworld.domain.board.Diary;
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
public class DiaryAnswer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_diaryAnswer_writer"))
//	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private User writer;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_diaryAnswer_to_diary"), nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	// @JsonBackReference
//	@JsonManagedReference
	private Diary diary;
	
//	@OneToOne(cascade = CascadeType.REMOVE)
//	@JoinColumn(foreignKey = @ForeignKey(name = "fk_diary_answer_alert"))
//	@JsonIgnore
//	private Alert alert;

	@Lob
	private String contents;

	private LocalDateTime createDate;

	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}

	public boolean isSameWriter(User loginUser) {
		return this.writer.equals(loginUser);
	}
	
	public void update(String contents) {
		this.contents = contents;
	}

}
