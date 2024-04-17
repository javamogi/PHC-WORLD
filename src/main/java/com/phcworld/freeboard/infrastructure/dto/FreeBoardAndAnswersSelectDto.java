package com.phcworld.freeboard.infrastructure.dto;

import com.phcworld.answer.infrastructure.FreeBoardAnswerEntity;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class FreeBoardAndAnswersSelectDto {
    private Long id;
    private UserEntity writer;
    private String title;
    private String contents;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer count;
    private Boolean isDeleted;
    private List<AnswerSelectDto> answers;
}
