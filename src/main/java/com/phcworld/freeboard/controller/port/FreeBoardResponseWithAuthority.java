package com.phcworld.freeboard.controller.port;

import com.phcworld.domain.api.model.response.FreeBoardAnswerApiResponse;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@Builder
public class FreeBoardResponseWithAuthority {
	
	private Long id;
	
	private UserEntity writer;
	
	private String title;
	
	private String contents;
	
	private String createDate;
	
	private Integer count;
	
	private String countOfAnswer;
	
	private List<FreeBoardAnswerApiResponse> freeBoardAnswerList;

	private Boolean existLoginUser;
	private Boolean isModifyAuthority; // 수정권한
	private Boolean isDeleteAuthority;

	public static FreeBoardResponseWithAuthority from(FreeBoard freeBoard, UserEntity loginUser){

		boolean existLoginUser = false;
		boolean isModifyAuthority = false;
		boolean isDeleteAuthority = false;

		if (Objects.nonNull(loginUser)) {
			existLoginUser = true;
			if (Objects.nonNull(freeBoard) && (freeBoard.matchWriter(loginUser))) {
				isModifyAuthority = true;
				isDeleteAuthority = true;
			}
			if (loginUser.getAuthority() == Authority.ROLE_ADMIN) {
				isDeleteAuthority = true;
			}
		}
		return FreeBoardResponseWithAuthority.builder()
				.id(freeBoard.getId())
				.writer(UserEntity.from(freeBoard.getWriter()))
				.title(freeBoard.getTitle())
				.contents(freeBoard.getContents())
				.createDate(freeBoard.getFormattedCreateDate())
				.count(freeBoard.getCount())
				.countOfAnswer(freeBoard.getCountOfAnswer())
				.existLoginUser(existLoginUser)
				.isModifyAuthority(isModifyAuthority)
				.isDeleteAuthority(isDeleteAuthority)
				.build();
	}

}
