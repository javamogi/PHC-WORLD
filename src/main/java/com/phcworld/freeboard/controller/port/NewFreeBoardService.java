package com.phcworld.freeboard.controller.port;

import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.user.domain.User;

public interface NewFreeBoardService {
    FreeBoard register(FreeBoardRequest request, User user);
}
