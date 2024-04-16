package com.phcworld.answer.service.port;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.answer.domain.dto.FreeBoardAnswerUpdateRequest;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.user.domain.User;
import com.phcworld.user.infrastructure.UserEntity;

public interface FreeBoardAnswerService {

    FreeBoardAnswer register(long freeBoardId, User user, FreeBoardAnswerRequest request);

    FreeBoardAnswer getById(long id, UserEntity loginUser);

    FreeBoardAnswer update(FreeBoardAnswerUpdateRequest request, UserEntity loginUser);

    SuccessResponse delete(long id, UserEntity loginUser);
}
