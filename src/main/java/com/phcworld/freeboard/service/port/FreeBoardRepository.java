package com.phcworld.freeboard.service.port;

import com.phcworld.freeboard.domain.FreeBoard;

import java.util.List;
import java.util.Optional;

public interface FreeBoardRepository {

    FreeBoard save(FreeBoard freeBoard);

    List<FreeBoard> findAll();

    Optional<FreeBoard> findById(Long freeBoardId);

    void delete(Long id);
}
