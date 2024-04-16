package com.phcworld.answer.infrastructure;

import com.phcworld.answer.domain.FreeBoardAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FreeBoardAnswerRepositoryImpl implements FreeBoardAnswerRepository{

    private final FreeBoardAnswerJpaRepository freeBoardAnswerJpaRepository;

    @Override
    public FreeBoardAnswer save(FreeBoardAnswer freeBoardAnswer) {
        return freeBoardAnswerJpaRepository.save(FreeBoardAnswerEntity.from(freeBoardAnswer)).toModel();
    }
}
