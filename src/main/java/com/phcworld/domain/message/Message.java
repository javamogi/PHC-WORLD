package com.phcworld.domain.message;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.utils.LocalDateTimeUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_message_fromUser"))
	private UserEntity sender;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_message_toUser"))
	private UserEntity receiver;

	@Lob
	private String contents;

	private String confirm;
	
	private String className;

	private LocalDateTime sendDate;

	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(this.sendDate);
	}

}
