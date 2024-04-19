package com.phcworld.answer.infrastructure.dto;

import com.phcworld.user.infrastructure.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreeBoardAnswerSelectDto {
    private Long id;
    private UserEntity writer;
    private String contents;
    private LocalDateTime updateDate;

}
