package com.phcworld.domain.answer;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phcworld.domain.board.TempDiary;
import com.phcworld.domain.parent.BasicBoardAndAnswer;
import com.phcworld.domain.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class TempDiaryAnswer extends BasicBoardAndAnswer {

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_tempDiaryAnswer_to_tempDiary"), nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private TempDiary tempDiary;

//	@OneToOne(cascade = CascadeType.REMOVE)
//	@JoinColumn(foreignKey = @ForeignKey(name = "fk_diary_answer_alert"))
//	@JsonIgnore
//	private Alert alert;

	@Builder
	public TempDiaryAnswer(Long id, User writer, String contents, TempDiary tempDiary) {
		super(id, writer, contents);
		this.tempDiary = tempDiary;
	}

	public boolean isSameWriter(User loginUser) {
		return super.getWriter().equals(loginUser);
	}

	public void update(String contents) {
		super.setContents(contents);
	}

}
