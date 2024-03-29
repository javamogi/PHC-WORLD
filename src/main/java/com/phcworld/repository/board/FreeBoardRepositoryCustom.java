package com.phcworld.repository.board;

import com.phcworld.repository.board.dto.FreeBoardSelectDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FreeBoardRepositoryCustom {
    List<FreeBoardSelectDto> findByKeywordOrderById(String keyword, Pageable pageable);
}
