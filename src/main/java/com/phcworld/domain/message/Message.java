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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phcworld.domain.user.User;
import com.phcworld.web.LocalDateTimeUtils;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_message_fromUser"))
	private User fromUser;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_message_toUser"))
	private User toUser;

	@Lob
	private String contents;

	private String confirm;
	
	private String className;

	private LocalDateTime sendDate;

	public Message() {
	}

	public Message(User loginUser, User receiveUser, String contents) {
		this.fromUser = loginUser;
		this.toUser = receiveUser;
		this.contents = contents;
		this.confirm = "읽지 않음";
		this.className = "important";
		this.sendDate = LocalDateTime.now();
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public User getFromUser() {
		return fromUser;
	}

	public User getToUser() {
		return toUser;
	}

	public String getContents() {
		return contents;
	}

	public String getConfirm() {
		return confirm;
	}
	
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(this.sendDate);
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", fromUser=" + fromUser + ", toUser=" + toUser + ", contents=" + contents
				+ ", confirm=" + confirm + ", sendDate=" + sendDate + "]";
	}

}
