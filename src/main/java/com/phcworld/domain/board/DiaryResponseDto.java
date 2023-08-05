package com.phcworld.domain.board;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
public class DiaryResponseDto {
    private List<DiaryResponse> diaries;
    private int totalPages;
}
