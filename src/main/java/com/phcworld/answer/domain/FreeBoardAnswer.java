package com.phcworld.answer.domain;

import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.answer.domain.dto.FreeBoardAnswerUpdateRequest;
import com.phcworld.answer.infrastructure.dto.FreeBoardAnswerSelectDto;
import com.phcworld.common.infrastructure.LocalDateTimeHolder;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.user.domain.User;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.utils.LocalDateTimeUtils;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FreeBoardAnswer {
    private Long id;
    private User writer;
    private FreeBoard freeBoard;
    private String contents;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public static FreeBoardAnswer from(FreeBoard freeBoard,
                                       User user,
                                       FreeBoardAnswerRequest request,
                                       LocalDateTimeHolder localDateTimeHolder) {
        return FreeBoardAnswer.builder()
                .writer(user)
                .freeBoard(freeBoard)
                .contents(request.getContents().replace("\r\n", "<br>"))
                .createDate(localDateTimeHolder.now())
                .updateDate(localDateTimeHolder.now())
                .build();
    }
    public static FreeBoardAnswer from(FreeBoardAnswerSelectDto dto) {
        return FreeBoardAnswer.builder()
                .id(dto.getId())
                .writer(dto.getWriter() != null ?
                        dto.getWriter().toModel() : null)
                .contents(dto.getContents())
                .updateDate(dto.getUpdateDate())
                .freeBoard(dto.getFreeBoard() != null ? dto.getFreeBoard().toModel() : null)
                .build();
    }

    public String getFormattedUpdateDate() {
        return LocalDateTimeUtils.getTime(updateDate);
    }

    public boolean matchWriter(UserEntity loginUser) {
        return writer.getId().equals(loginUser.getId()) && writer.getAuthority() == loginUser.getAuthority();
    }

    public FreeBoardAnswer update(FreeBoardAnswerUpdateRequest request, LocalDateTimeHolder localDateTimeHolder) {
        return FreeBoardAnswer.builder()
                .id(id)
                .writer(writer)
                .freeBoard(freeBoard)
                .contents(request.getContents())
                .createDate(createDate)
                .updateDate(localDateTimeHolder.now())
                .build();
    }
}
