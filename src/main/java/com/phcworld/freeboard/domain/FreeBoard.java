package com.phcworld.freeboard.domain;

import com.phcworld.common.infrastructure.LocalDateTimeHolder;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.user.domain.User;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class FreeBoard {
    private Long id;

    private User writer;

    private String title;

    private String contents;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private Integer count;

    public static FreeBoard from(FreeBoardRequest request, User user, LocalDateTimeHolder localDateTimeHolder) {
        return FreeBoard.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .writer(user)
                .createDate(localDateTimeHolder.now())
                .updateDate(localDateTimeHolder.now())
                .count(0)
                .build();
    }

//    private List<FreeBoardAnswer> freeBoardAnswers;
}
