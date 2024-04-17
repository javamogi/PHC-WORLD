package com.phcworld.freeboard.controller.port;

import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.domain.dto.FreeBoardUpdateRequest;
import com.phcworld.user.domain.User;
import com.phcworld.user.infrastructure.UserEntity;

import java.util.List;

public interface FreeBoardService {
    FreeBoard register(FreeBoardRequest request, User user);

    List<FreeBoard> findAllList();

    FreeBoard addReadCount(Long freeBoardId);

    FreeBoard getFreeBoard(Long id, UserEntity loginUser);

    FreeBoard update(FreeBoardUpdateRequest request, UserEntity loginUser);

    FreeBoard delete(Long id, UserEntity loginUser);
}
