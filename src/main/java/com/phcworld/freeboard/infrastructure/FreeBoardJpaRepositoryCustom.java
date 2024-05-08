package com.phcworld.freeboard.infrastructure;

import com.phcworld.freeboard.infrastructure.dto.FreeBoardAndAnswersSelectDto;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardSelectDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FreeBoardJpaRepositoryCustom {
    List<FreeBoardSelectDto> findByKeywordOrderById(String keyword, Pageable pageable);
    Optional<FreeBoardAndAnswersSelectDto> findByIdAndAnswers(long id, Pageable pageable);
    List<FreeBoardSelectDto> findAllWithAnswersCount(Long userId);
}
