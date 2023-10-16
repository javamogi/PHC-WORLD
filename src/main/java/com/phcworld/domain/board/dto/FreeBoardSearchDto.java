package com.phcworld.domain.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FreeBoardSearchDto {
    private int pageNum;
    private int pageSize;
    private String keyword;
}
