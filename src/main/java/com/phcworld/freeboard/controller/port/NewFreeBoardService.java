package com.phcworld.freeboard.controller.port;

import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.user.domain.User;

import java.util.List;

public interface NewFreeBoardService {
    FreeBoard register(FreeBoardRequest request, User user);

    List<FreeBoard> findAllList();
}
