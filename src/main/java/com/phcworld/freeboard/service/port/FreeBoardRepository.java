package com.phcworld.freeboard.service.port;

import com.phcworld.freeboard.domain.FreeBoard;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FreeBoardRepository {

    FreeBoard save(FreeBoard freeBoard);

    List<FreeBoard> findAll();

    Optional<FreeBoard> findById(Long freeBoardId);

    Optional<FreeBoard> findByIdAndAnswers(long id, int pageNum);

    List<FreeBoard> findByUser(Long userId);
}
