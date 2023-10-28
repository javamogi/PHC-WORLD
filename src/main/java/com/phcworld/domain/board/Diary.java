package com.phcworld.domain.board;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import com.phcworld.domain.board.dto.DiaryRequest;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.phcworld.domain.answer.DiaryAnswer;
import com.phcworld.domain.good.Good;
import com.phcworld.domain.user.User;
import com.phcworld.utils.LocalDateTimeUtils;

import lombok.experimental.Accessors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = {"writer", "diaryAnswers", "goodPushedUser", "diaryHashtags"})
@Table(indexes = {@Index(name = "idx__writer_id_create_date", columnList = "writer_id, createDate"),
				@Index(name = "idx__create_date", columnList = "createDate"),
				@Index(name = "idx__count_good_create_date", columnList = "countGood, createDate")})
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

	private Long countGood;

	@CreatedDate
	private LocalDateTime createDate;

	@LastModifiedDate
	private LocalDateTime updateDate;

	@OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE)
	// @JsonManagedReference
	@JsonBackReference
	private List<DiaryAnswer> diaryAnswers;

	@OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE)
	@JsonBackReference
	private List<Good> goodPushedUser;

	@OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE)
	private List<DiaryHashtag> diaryHashtags;

	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}
	public String getFormattedUpdateDate() {
		return LocalDateTimeUtils.getTime(updateDate);
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

	public void update(DiaryRequest diaryRequest) {
		this.contents = diaryRequest.getContents();
		this.thumbnail = diaryRequest.getThumbnail();
	}

	public boolean matchId(Long diaryId) {
		if (diaryId == null) {
			return false;
		}
		return this.id.equals(diaryId);
	}

	public void removeAnswer(DiaryAnswer diaryAnswer) {
		this.getDiaryAnswers().remove(diaryAnswer);
	}

	public void addGood() {
		this.countGood++;
	}

	public void removeGood() {
		this.countGood--;
	}
}
