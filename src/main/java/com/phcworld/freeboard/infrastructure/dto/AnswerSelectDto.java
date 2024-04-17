package com.phcworld.freeboard.infrastructure.dto;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerSelectDto {
    private Long id;
    private UserEntity writer;
    private FreeBoardEntity freeBoard;
    private String contents;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public FreeBoardAnswer toModel() {
        return FreeBoardAnswer.builder()
                .id(id)
                .writer(writer.toModel())
                .freeBoard(freeBoard.toModel())
                .contents(contents)
                .createDate(createDate)
                .updateDate(updateDate)
                .build();
    }
}
