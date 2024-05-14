package com.phcworld.answer.infrastructure;

import com.phcworld.answer.infrastructure.port.FreeBoardAnswerSelectDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FreeBoardAnswerJpaRepositoryCustom {
        Page<FreeBoardAnswerSelectDto> findByFreeBoardId(long freeBoardId, Pageable pageable);

        List<FreeBoardAnswerSelectDto> findByUserNonOffset(Long userId, Long answerId);
}
