package com.phcworld.domain.board;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class TempDiary extends BasicBoard {

	private String thumbnail;

	@OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE)
	// @JsonManagedReference
	@JsonBackReference
	private List<DiaryAnswer> diaryAnswers;

	@OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE)
	@JsonBackReference
	private List<Good> goodPushedUser;

	@Builder
	public TempDiary(Long id, User writer, String title, String contents, String thumbnail) {
		super(id, writer, title, contents);
		this.thumbnail = thumbnail;
	}

	public String getCountOfAnswer() {
		if (this.diaryAnswers == null || this.diaryAnswers.size() == 0) {
			return "";
		}
		return "[" + diaryAnswers.size() + "]";
	}

	public Integer getCountOfGood() {
		if (goodPushedUser == null) {
			return 0;
		}
		return goodPushedUser.size();
	}

	public boolean matchUser(User loginUser) {
		if (loginUser == null) {
			return false;
		}
		return loginUser.equals(super.getWriter());
	}

	public void update(TempDiaryRequest diaryRequest) {
		super.setContents(diaryRequest.getContents());
		this.thumbnail = diaryRequest.getThumbnail();
	}

	public boolean matchId(Long diaryId) {
		if (diaryId == null) {
			return false;
		}
		return super.getId().equals(diaryId);
	}

	@Override
	public String toString() {
		return "TempDiary [id=" + super.getId() + ", writer=" + super.getWriter() + ", title=" + super.getTitle()
				+ ", contents=" + super.getContents() + ", createDate=" + super.getCreateDate() + ", updateDate="
				+ super.getUpdateDate() + ", thumbnail=" + thumbnail + ", diaryAnswers=" + diaryAnswers
				+ ", goodPushedUser=" + goodPushedUser + "]";
	}

}
