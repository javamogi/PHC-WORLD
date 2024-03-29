package com.phcworld.domain.answer;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phcworld.domain.board.Diary;
import com.phcworld.utils.LocalDateTimeUtils;

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
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(name = "idx__writer_id_create_date", columnList = "writer_id, createDate"),
		@Index(name = "idx__diary_id_create_date", columnList = "diary_id, createDate")})
public class DiaryAnswer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_diaryAnswer_writer"))
//	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private UserEntity writer;

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

	@CreatedDate
	private LocalDateTime createDate;
	
	@LastModifiedDate
	private LocalDateTime updateDate;
	
	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}
	
	public String getFormattedUpdateDate() {
		return LocalDateTimeUtils.getTime(updateDate);
	}

	public boolean isSameWriter(UserEntity loginUser) {
		return this.writer.equals(loginUser);
	}
	
	public void update(String contents) {
		this.contents = contents;
	}

}
