package com.phcworld.answer.infrastructure;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.utils.LocalDateTimeUtils;

import lombok.experimental.Accessors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "free_board_answer",
		indexes = {@Index(name = "idx__writer_id_create_date", columnList = "writer_id, createDate"),
		@Index(name = "idx__free_board_id_create_date", columnList = "free_board_id, createDate")})
@ToString(exclude = {"freeBoard"})
public class FreeBoardAnswerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_freeBoardAnswer_writer"))
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private UserEntity writer;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_freeBoardAnswer_to_freeBoard"), nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	// @JsonBackReference
//	@JsonManagedReference
	private FreeBoardEntity freeBoard;
	
	@Lob
	private String contents;

	@CreatedDate
	private LocalDateTime createDate;
	
	@LastModifiedDate
	private LocalDateTime updateDate;

    public static FreeBoardAnswerEntity from(FreeBoardAnswer freeBoardAnswer) {
		return FreeBoardAnswerEntity.builder()
				.id(freeBoardAnswer.getId())
				.writer(UserEntity.from(freeBoardAnswer.getWriter()))
				.freeBoard(FreeBoardEntity.from(freeBoardAnswer.getFreeBoard()))
				.contents(freeBoardAnswer.getContents())
				.createDate(freeBoardAnswer.getCreateDate())
				.updateDate(freeBoardAnswer.getUpdateDate())
				.build();
    }

	public FreeBoardAnswer toModel() {
		return FreeBoardAnswer.builder()
				.id(id)
				.writer(writer.toModel())
				.freeBoard(freeBoard.toModelWithoutAnswer())
				.contents(contents)
				.createDate(createDate)
				.updateDate(updateDate)
				.build();
	}
}
