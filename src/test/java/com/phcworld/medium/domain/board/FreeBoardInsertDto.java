package com.phcworld.medium.domain.board;

import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FreeBoardInsertDto {
    private Long writerId;
    private String title;
    private String contents;
    private String icon;
    private String badge;
    private Integer count;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public static FreeBoardInsertDto of(FreeBoardEntity freeBoard){
        return FreeBoardInsertDto.builder()
                .writerId(freeBoard.getWriter().getId())
                .title(freeBoard.getTitle())
                .contents(freeBoard.getContents())
                .count(freeBoard.getCount())
                .createDate(freeBoard.getCreateDate())
                .updateDate(freeBoard.getUpdateDate())
                .build();
    }
}
