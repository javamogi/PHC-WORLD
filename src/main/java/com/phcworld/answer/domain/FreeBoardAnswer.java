package com.phcworld.answer.domain;

import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.common.infrastructure.LocalDateTimeHolder;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.user.domain.User;
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

    public String getFormattedUpdateDate() {
        return LocalDateTimeUtils.getTime(createDate);
    }
}
