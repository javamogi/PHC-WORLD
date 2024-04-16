package com.phcworld.answer.service.port;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.answer.domain.dto.FreeBoardAnswerRequest;
import com.phcworld.user.domain.User;

public interface FreeBoardAnswerService {

    FreeBoardAnswer register(long freeBoardId, User user, FreeBoardAnswerRequest request);
}
