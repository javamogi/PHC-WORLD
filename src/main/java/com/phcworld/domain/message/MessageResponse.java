package com.phcworld.domain.message;

import com.phcworld.user.infrastructure.UserEntity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MessageResponse {

	private Long id;

	private UserEntity sender;

	private UserEntity receiver;

	private String contents;

	private String className;

	private String confirm;

	private String sendDate;

}
