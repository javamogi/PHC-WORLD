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
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.good.Good;
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
//@ToString(exclude = {"goodPushedUser"})
public class Diary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_dairy_writer"))
//	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private User writer;

	private String title;

	@Lob
	private String contents;

	private String thumbnail;

	@OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE)
	// @JsonManagedReference
	@JsonBackReference
	private List<DiaryAnswer> diaryAnswers;

	@OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE)
	@JsonBackReference
	private List<Good> goodPushedUser;

	private LocalDateTime createDate;

	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}
	
	public String getCountOfAnswer() {
		if (this.diaryAnswers == null || this.diaryAnswers.size() == 0) {
			return "";
		}
		return "[" + diaryAnswers.size() + "]";
	}
	
	public Integer getCountOfGood() {
		if(goodPushedUser == null) {
			return 0;
		}
		return goodPushedUser.size();
	}

	public boolean matchUser(User loginUser) {
		if (loginUser == null) {
			return false;
		}
		return loginUser.equals(this.writer);
	}

	public void update(Diary inputDiary) {
		this.contents = inputDiary.getContents();
		this.thumbnail = inputDiary.getThumbnail();
	}

	public boolean matchId(Long diaryId) {
		if (diaryId == null) {
			return false;
		}
		return this.id.equals(diaryId);
	}
	
}
