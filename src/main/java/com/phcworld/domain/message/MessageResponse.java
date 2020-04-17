package com.phcworld.domain.message;

import com.phcworld.domain.user.User;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MessageResponse {

	private Long id;

	private User sender;

	private User receiver;

	private String contents;

	private String className;

	private String confirm;

	private String sendDate;

}
