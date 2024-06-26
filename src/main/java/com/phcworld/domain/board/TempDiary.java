package com.phcworld.domain.board;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.phcworld.domain.answer.TempDiaryAnswer;
import com.phcworld.domain.board.dto.TempDiaryRequest;
import com.phcworld.domain.good.TempGood;
import com.phcworld.domain.parent.BasicBoardAndAnswer;
import com.phcworld.user.infrastructure.UserEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class TempDiary extends BasicBoardAndAnswer {

	private String title;
	
	private String thumbnail;

	@OneToMany(mappedBy = "tempDiary", cascade = CascadeType.REMOVE)
	@JsonBackReference
	private List<TempDiaryAnswer> tempDiaryAnswers;

	@OneToMany(mappedBy = "tempDiary", cascade = CascadeType.REMOVE)
	@JsonBackReference
	private Set<TempGood> tempGood;

	@Builder
	public TempDiary(Long id, UserEntity writer, String contents, String title, String thumbnail, LocalDateTime createDate) {
		super(id, writer, contents, createDate);
		this.title = title;
		this.thumbnail = thumbnail;
	}

	public String getCountOfAnswer() {
		if (this.tempDiaryAnswers == null || this.tempDiaryAnswers.size() == 0) {
			return "";
		}
		return "[" + tempDiaryAnswers.size() + "]";
	}

	public Integer getCountOfGood() {
		if (tempGood == null) {
			return 0;
		}
		return tempGood.size();
	}

	public boolean matchUser(UserEntity loginUser) {
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

	public boolean updateGood(TempGood pushedUser) {
		boolean result = tempGood.add(pushedUser);
		if(!result) {
			tempGood.remove(pushedUser);
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "TempDiary [id=" + super.getId() + ", writer=" + super.getWriter() + ", title=" + this.title
				+ ", contents=" + super.getContents() + ", createDate=" + super.getCreateDate() + ", updateDate="
				+ super.getUpdateDate() + ", thumbnail=" + thumbnail + ", diaryAnswers=" + tempDiaryAnswers
				+ ", goodPushedUser=" + tempGood + "]";
	}

}
