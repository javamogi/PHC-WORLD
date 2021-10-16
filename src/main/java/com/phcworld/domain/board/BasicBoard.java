package com.phcworld.domain.board;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.phcworld.domain.user.User;
import com.phcworld.utils.LocalDateTimeUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BasicBoard {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_writer"))
	private User writer;

	private String title;

	@Lob
	private String contents;

	@CreatedDate
	private LocalDateTime createDate;
	
	@LastModifiedDate
	private LocalDateTime updateDate;

	public BasicBoard(Long id, User writer, String title, String contents) {
		this.id = id;
		this.writer = writer;
		this.title = title;
		this.contents = contents;
	}
	
	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}
	public String getFormattedUpdateDate() {
		return LocalDateTimeUtils.getTime(updateDate);
	}

}
