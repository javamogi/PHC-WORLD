package com.phcworld.freeboard.service.port;

import com.phcworld.freeboard.domain.FreeBoard;

import java.util.List;

public interface FreeBoardRepository {

    FreeBoard save(FreeBoard freeBoard);

    List<FreeBoard> findAll();
}
