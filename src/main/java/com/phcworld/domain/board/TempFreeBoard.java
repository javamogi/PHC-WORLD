package com.phcworld.domain.board;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.phcworld.domain.answer.TempFreeBoardAnswer;
import com.phcworld.domain.board.dto.TempFreeBoardRequest;
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
public class TempFreeBoard extends BasicBoardAndAnswer {
	
	private String title;

	private String icon;

	private String badge;

	private Integer count;

	@OneToMany(mappedBy = "tempFreeBoard", cascade = CascadeType.REMOVE)
	@JsonBackReference
	private List<TempFreeBoardAnswer> tempFreeBoardAnswers;

	@Builder
	public TempFreeBoard(Long id, UserEntity writer, String contents, String title, String icon, String badge, Integer count,
                         List<TempFreeBoardAnswer> freeBoardAnswers, LocalDateTime createDate) {
		super(id, writer, contents, createDate);
		this.title = title;
		this.icon = icon;
		this.badge = badge;
		this.count = count;
		this.tempFreeBoardAnswers = freeBoardAnswers;
	}

	public String getCountOfAnswer() {
		if (this.tempFreeBoardAnswers == null || this.tempFreeBoardAnswers.size() == 0) {
			return "";
		}
		return "[" + tempFreeBoardAnswers.size() + "]";
	}

	public void addCount() {
		this.count += 1;
	}

	public void update(TempFreeBoardRequest request) {
		super.setContents(request.getContents());
		this.icon = request.getIcon();
	}

	public boolean matchUser(UserEntity loginUser) {
		if (loginUser == null) {
			return false;
		}
		return super.getWriter().equals(loginUser);
	}

	@Override
	public String toString() {
		return "TempFreeBoard [id=" + super.getId() + ", writer=" + super.getWriter() + ", title=" + this.title
				+ ", contents=" + super.getContents() + ", createDate=" + super.getCreateDate() + ", updateDate="
				+ super.getUpdateDate() + ", icon=" + icon + ", badge=" + badge + ", count=" + count
				+ ", freeBoardAnswers=" + tempFreeBoardAnswers + "]";
	}

}
