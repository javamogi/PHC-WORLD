package com.phcworld.freeboard.controller.port;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FreeBoardSearchDto {
    private int pageNum;
    private int pageSize;
    private String keyword;
}
