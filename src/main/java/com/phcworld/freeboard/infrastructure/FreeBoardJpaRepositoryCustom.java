package com.phcworld.freeboard.infrastructure;

import com.phcworld.freeboard.infrastructure.dto.FreeBoardSelectDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FreeBoardJpaRepositoryCustom {
    List<FreeBoardSelectDto> findByKeywordOrderById(String keyword, Pageable pageable);
}
