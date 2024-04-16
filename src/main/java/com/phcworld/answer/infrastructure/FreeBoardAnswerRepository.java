package com.phcworld.answer.infrastructure;

import com.phcworld.answer.domain.FreeBoardAnswer;

import java.util.Optional;

public interface FreeBoardAnswerRepository {

    FreeBoardAnswer save(FreeBoardAnswer freeBoardAnswer);

    Optional<FreeBoardAnswer> findById(long id);
}
