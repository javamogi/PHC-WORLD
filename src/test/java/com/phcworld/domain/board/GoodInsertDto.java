package com.phcworld.domain.board;

import com.phcworld.domain.good.Good;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GoodInsertDto {
    private Long diaryId;
    private Long userId;
    private LocalDateTime createDate;

    public static GoodInsertDto of(Good good){
        return GoodInsertDto.builder()
                .diaryId(good.getDiary().getId())
                .userId(good.getUser().getId())
                .createDate(good.getCreateDate())
                .build();
    }
}
