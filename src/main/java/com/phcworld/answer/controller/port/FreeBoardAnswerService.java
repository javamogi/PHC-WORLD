package com.phcworld.answer.controller.port;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.answer.domain.dto.FreeBoardAnswerUpdateRequest;
import com.phcworld.domain.api.model.response.SuccessResponse;
import com.phcworld.user.domain.User;
import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;

public interface FreeBoardAnswerService {

    FreeBoardAnswer register(long freeBoardId, User user, FreeBoardAnswerRequest request);

    FreeBoardAnswer getById(long id, UserEntity loginUser);

    FreeBoardAnswer update(FreeBoardAnswerUpdateRequest request, UserEntity loginUser);

    SuccessResponse delete(long id, UserEntity loginUser);

    Page<FreeBoardAnswer> getListByFreeBoard(long freeBoardId, int pageNum);

    List<FreeBoardAnswer> getListByUser(Long userId, Long answerId);
}
