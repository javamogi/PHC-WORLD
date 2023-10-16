package com.phcworld.domain.board.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DiaryResponseDto {
    private List<DiaryResponse> diaries;
    private int totalPages;
}
