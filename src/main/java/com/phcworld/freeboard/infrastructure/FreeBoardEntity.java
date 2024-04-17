package com.phcworld.freeboard.infrastructure;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.*;

import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
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
@DynamicUpdate
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

	@ColumnDefault("0")
	@Builder.Default
	private Integer count = 0;

	@ColumnDefault("false")
	private Boolean isDeleted = false;

	@OneToMany(mappedBy = "freeBoard", cascade = CascadeType.REMOVE)
	// @JsonManagedReference
	@JsonBackReference
	private List<FreeBoardAnswerEntity> freeBoardAnswers;

	public static FreeBoardEntity from(FreeBoard freeBoard) {
		return FreeBoardEntity.builder()
				.id(freeBoard.getId())
				.writer(UserEntity.from(freeBoard.getWriter()))
				.title(freeBoard.getTitle())
				.contents(freeBoard.getContents())
				.count(freeBoard.getCount())
				.createDate(freeBoard.getCreateDate())
				.updateDate(freeBoard.getUpdateDate())
				.isDeleted(freeBoard.isDeleted())
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
				.freeBoardAnswers(
						freeBoardAnswers != null ?
						freeBoardAnswers.stream()
						.map(FreeBoardAnswerEntity::toModel)
						.collect(Collectors.toList()) : new ArrayList<>())
				.isDeleted(isDeleted)
				.build();
	}

	public FreeBoard toModelWithoutAnswer() {
		return FreeBoard.builder()
				.id(id)
				.title(title)
				.contents(contents)
				.count(count)
				.createDate(createDate)
				.updateDate(updateDate)
				.writer(writer.toModel())
				.isDeleted(isDeleted)
				.build();
	}

}
