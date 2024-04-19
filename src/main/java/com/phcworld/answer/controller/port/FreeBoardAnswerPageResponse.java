package com.phcworld.answer.controller.port;

import com.phcworld.answer.domain.FreeBoardAnswer;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class FreeBoardAnswerPageResponse {

    private List<FreeBoardAnswerResponse> answers;
    private Integer totalOfPage;
    private Integer currentPageNum;

    public static FreeBoardAnswerPageResponse from(Page<FreeBoardAnswer> freeBoardAnswer) {
        return FreeBoardAnswerPageResponse.builder()
                .totalOfPage(freeBoardAnswer.getTotalPages())
                .currentPageNum(freeBoardAnswer.getNumber() + 1)
                .answers(freeBoardAnswer.getContent()
                        .stream()
                        .map(FreeBoardAnswerResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
