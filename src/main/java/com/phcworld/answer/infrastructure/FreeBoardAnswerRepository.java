package com.phcworld.answer.infrastructure;

import com.phcworld.answer.domain.FreeBoardAnswer;

public interface FreeBoardAnswerRepository {

    FreeBoardAnswer save(FreeBoardAnswer freeBoardAnswer);
}
