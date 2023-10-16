package com.phcworld.domain.board;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DiaryInsertDto {
    private Long writerId;
    private String title;
    private String contents;
    private String thumbnail;
    private Long countGood;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public static DiaryInsertDto of(Diary diary){
        return DiaryInsertDto.builder()
                .writerId(diary.getWriter().getId())
                .title(diary.getTitle())
                .contents(diary.getContents())
                .thumbnail(diary.getThumbnail())
                .countGood(diary.getCountGood())
                .createDate(diary.getCreateDate())
                .updateDate(diary.getUpdateDate())
                .build();
    }
}
