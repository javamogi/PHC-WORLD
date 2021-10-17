package com.phcworld.domain.board;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.domain.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class TempFreeBoard extends BasicBoard {

	private String icon;

	private String badge;

	private Integer count;

	@OneToMany(mappedBy = "freeBoard", cascade = CascadeType.REMOVE)
	@JsonBackReference
	private List<FreeBoardAnswer> freeBoardAnswers;

	@Builder
	public TempFreeBoard(Long id, User writer, String title, String contents, String icon, String badge, Integer count,
			List<FreeBoardAnswer> freeBoardAnswers) {
		super(id, writer, title, contents);
		this.icon = icon;
		this.badge = badge;
		this.count = count;
		this.freeBoardAnswers = freeBoardAnswers;
	}

	public String getCountOfAnswer() {
		if (this.freeBoardAnswers == null || this.freeBoardAnswers.size() == 0) {
			return "";
		}
		return "[" + freeBoardAnswers.size() + "]";
	}

	public void addCount() {
		this.count += 1;
	}

	public void update(TempFreeBoardRequest request) {
		super.setContents(request.getContents());
		this.icon = request.getIcon();
	}

	public boolean matchUser(User loginUser) {
		if (loginUser == null) {
			return false;
		}
		return super.getWriter().equals(loginUser);
	}

	@Override
	public String toString() {
		return "TempFreeBoard [id=" + super.getId() + ", writer=" + super.getWriter() + ", title=" + super.getTitle()
				+ ", contents=" + super.getContents() + ", createDate=" + super.getCreateDate() + ", updateDate="
				+ super.getUpdateDate() + ", icon=" + icon + ", badge=" + badge + ", count=" + count
				+ ", freeBoardAnswers=" + freeBoardAnswers + "]";
	}

}
