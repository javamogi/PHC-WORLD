package com.phcworld.repository.board.dto;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.user.User;
import com.phcworld.utils.LocalDateTimeUtils;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiarySelectDto {

    private Long id;

    private User writer;

    private String title;

    private String thumbnail;

    private Long countOfAnswers;

    private Long countOfGood;

    private LocalDateTime updateDate;

    private LocalDateTime createDate;

    private List<String> hashtags;

    public String getFormattedUpdateDate(){
        return LocalDateTimeUtils.getTime(updateDate);
    }
    public String getFormattedCreateDate(){
        return LocalDateTimeUtils.getTime(createDate);
    }

    public static DiarySelectDto of(Diary diary){
        return DiarySelectDto.builder()
                .id(diary.getId())
                .writer(diary.getWriter())
                .title(diary.getTitle())
                .thumbnail(diary.getThumbnail())
                .updateDate(diary.getUpdateDate())
                .build();
    }
}
