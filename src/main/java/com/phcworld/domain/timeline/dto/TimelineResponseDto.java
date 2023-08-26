package com.phcworld.domain.timeline.dto;

import com.phcworld.domain.timeline.Timeline;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TimelineResponseDto {
    private Long id;
    private String type;
    private String icon;
    private String formattedSaveDate;

    public static TimelineResponseDto of(Timeline timeline){
        switch (timeline.getPostInfo().getSaveType()){
            case DIARY:
                return TimelineResponseDto.builder()
                        .id(timeline.getId())
                        .type("diary")
                        .icon("edit")
                        .formattedSaveDate(timeline.getFormattedSaveDate())
                        .build();
            case DIARY_ANSWER:
                return TimelineResponseDto.builder()
                        .id(timeline.getId())
                        .type("diary answer")
                        .icon("comment")
                        .formattedSaveDate(timeline.getFormattedSaveDate())
                        .build();
            case FREE_BOARD:
                return TimelineResponseDto.builder()
                        .id(timeline.getId())
                        .type("free board")
                        .icon("list-alt")
                        .formattedSaveDate(timeline.getFormattedSaveDate())
                        .build();
            case FREE_BOARD_ANSWER:
                return TimelineResponseDto.builder()
                        .id(timeline.getId())
                        .type("freeBoard answer")
                        .icon("comment")
                        .formattedSaveDate(timeline.getFormattedSaveDate())
                        .build();
            default:
                return TimelineResponseDto.builder()
                        .id(timeline.getId())
                        .type("good")
                        .icon("thumbs-up")
                        .formattedSaveDate(timeline.getFormattedSaveDate())
                        .build();
        }
    }
}
