package com.phcworld.freeboard.domain;

import com.phcworld.domain.answer.FreeBoardAnswer;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class FreeBoard {
    private Long id;

    private UserEntity writer;

    private String title;

    private String contents;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private Integer count;

//    private List<FreeBoardAnswer> freeBoardAnswers;
}
