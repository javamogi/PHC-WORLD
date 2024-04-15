package com.phcworld.answer.domain;

import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.user.domain.User;

import java.time.LocalDateTime;

public class FreeBoardAnswer {
    private Long id;
    private User writer;
    private FreeBoard freeBoard;
    private String contents;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
