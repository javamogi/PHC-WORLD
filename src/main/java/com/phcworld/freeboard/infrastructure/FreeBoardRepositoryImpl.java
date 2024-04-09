package com.phcworld.freeboard.infrastructure;

import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.service.port.FreeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FreeBoardRepositoryImpl implements FreeBoardRepository {

    private final FreeBoardJpaRepository freeBoardJpaRepository;

    @Override
    public FreeBoard save(FreeBoard freeBoard) {
        return freeBoardJpaRepository.save(FreeBoardEntity.from(freeBoard)).toModel();
    }
}
