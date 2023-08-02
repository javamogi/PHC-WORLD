package com.phcworld.domain.board;

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

    public static FreeBoardInsertDto of(FreeBoard freeBoard){
        return FreeBoardInsertDto.builder()
                .writerId(freeBoard.getWriter().getId())
                .title(freeBoard.getTitle())
                .contents(freeBoard.getContents())
                .icon(freeBoard.getIcon())
                .badge(freeBoard.getBadge())
                .count(freeBoard.getCount())
                .createDate(freeBoard.getCreateDate())
                .updateDate(freeBoard.getUpdateDate())
                .build();
    }
}
