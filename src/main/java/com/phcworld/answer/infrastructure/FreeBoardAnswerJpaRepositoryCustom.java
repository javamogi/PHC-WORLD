package com.phcworld.answer.infrastructure;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.infrastructure.dto.FreeBoardAnswerSelectDto;
import com.phcworld.freeboard.domain.FreeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FreeBoardAnswerJpaRepositoryCustom {
        Page<FreeBoardAnswerSelectDto> findByFreeBoardId(long freeBoardId, Pageable pageable);

        List<FreeBoardAnswerSelectDto> findByUserNonOffset(Long userId, Long answerId);
}
