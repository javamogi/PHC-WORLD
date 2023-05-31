package com.phcworld.domain.dashboard;

import com.phcworld.domain.timeline.Timeline;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DashBoardSelectDto {
    private Long freeBoardCount;
    private Long freeBoardAnswerCount;
    private Long diaryCount;
    private Long diaryAnswerCount;
    private Long alertCount;
    private List<Timeline> timelineList;

    public Long getAnswerCount(){
        return freeBoardAnswerCount + diaryAnswerCount;
    }
}
