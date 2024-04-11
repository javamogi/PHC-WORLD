package com.phcworld.freeboard.domain;

import com.phcworld.common.infrastructure.LocalDateTimeHolder;
import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.user.domain.User;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.utils.LocalDateTimeUtils;
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

    private List<FreeBoardAnswer> freeBoardAnswers;

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

    public static FreeBoard from(FreeBoardEntity freeBoard) {
        return FreeBoard.builder()
                .id(freeBoard.getId())
                .title(freeBoard.getTitle())
                .contents(freeBoard.getContents())
                .writer(freeBoard.getWriter().toModel())
                .count(freeBoard.getCount())
                .createDate(freeBoard.getCreateDate())
                .updateDate(freeBoard.getUpdateDate())
                .build();
    }

    public String getFormattedCreateDate() {
        return LocalDateTimeUtils.getTime(createDate);
    }

    public String getCountOfAnswer() {
        if (freeBoardAnswers == null || freeBoardAnswers.isEmpty()) {
            return "";
        }
        return "[" + freeBoardAnswers.size() + "]";
    }

}
