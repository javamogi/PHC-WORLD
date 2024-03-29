package com.phcworld.domain.answer;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phcworld.domain.board.TempFreeBoard;
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
public class TempFreeBoardAnswer extends BasicBoardAndAnswer{

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_tempFreeBoardAnswers_to_tempFreeBoard"), nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private TempFreeBoard tempFreeBoard;
	
	@Builder
	public TempFreeBoardAnswer(Long id, User writer, String contents, TempFreeBoard tempFreeBoard, LocalDateTime createDate) {
		super(id, writer, contents, createDate);
		this.tempFreeBoard = tempFreeBoard;
	}

	public boolean isSameWriter(User loginUser) {
		return super.getWriter().equals(loginUser);
	}

	public void update(String contents) {
		super.setContents(contents);
	}

}
