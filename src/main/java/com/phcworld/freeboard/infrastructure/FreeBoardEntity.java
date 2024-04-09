package com.phcworld.freeboard.infrastructure;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import com.phcworld.common.infrastructure.LocalDateTimeHolder;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.user.domain.User;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.utils.LocalDateTimeUtils;

import lombok.experimental.Accessors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = {"writer", "freeBoardAnswers"})
@Table(name = "free_board",
		indexes = {@Index(name = "idx__create_date", columnList = "createDate"),
				@Index(name = "idx__writer_id_create_date", columnList = "writer_id, createDate")})
//@SequenceGenerator(
//		name = "BOARD_SEQ_GENERATOR",
//		sequenceName = "BOARD_SEQ",
//		initialValue = 1, allocationSize = 10000
//)
public class FreeBoardEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GENERATOR")
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_freeBoard_writer"))
//	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private UserEntity writer;

	private String title;

	@Lob
//	@Column(nullable = true, columnDefinition = "TEXT")
	private String contents;

	@CreatedDate
	private LocalDateTime createDate;
	
	@LastModifiedDate
	private LocalDateTime updateDate;
	
	private Integer count;

	@OneToMany(mappedBy = "freeBoard", cascade = CascadeType.REMOVE)
	// @JsonManagedReference
	@JsonBackReference
	private List<FreeBoardAnswer> freeBoardAnswers;

	public static FreeBoardEntity from(FreeBoard freeBoard) {
		return FreeBoardEntity.builder()
				.id(freeBoard.getId())
				.writer(UserEntity.from(freeBoard.getWriter()))
				.title(freeBoard.getTitle())
				.contents(freeBoard.getContents())
				.count(freeBoard.getCount())
				.createDate(freeBoard.getCreateDate())
				.updateDate(freeBoard.getUpdateDate())
				.build();
	}

	public FreeBoard toModel() {
		return FreeBoard.builder()
				.id(id)
				.title(title)
				.contents(contents)
				.count(count)
				.createDate(createDate)
				.updateDate(updateDate)
				.writer(writer.toModel())
				.build();
	}

    public String getCountOfAnswer() {
		if (this.freeBoardAnswers == null|| this.freeBoardAnswers.size() == 0) {
			return "";
		}
		return "[" + freeBoardAnswers.size() + "]";
	}

	public void addCount() {
		this.count += 1;
	}

	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}

	public String getFormattedUpdateDate() {
		return LocalDateTimeUtils.getTime(updateDate);
	}

	public void update(FreeBoardRequest request) {
		this.contents = request.getContents();
	}

	public boolean matchUser(UserEntity loginUser) {
		if (loginUser == null) {
			return false;
		}
		return this.writer.equals(loginUser);
	}

}
