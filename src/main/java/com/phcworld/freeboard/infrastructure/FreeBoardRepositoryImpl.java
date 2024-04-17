package com.phcworld.freeboard.infrastructure;

import com.phcworld.exception.model.NotFoundException;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.service.port.FreeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FreeBoardRepositoryImpl implements FreeBoardRepository {

    private final FreeBoardJpaRepository freeBoardJpaRepository;

    @Override
    public FreeBoard save(FreeBoard freeBoard) {
        return freeBoardJpaRepository.save(FreeBoardEntity.from(freeBoard)).toModel();
    }

    @Override
    public List<FreeBoard> findAll() {
        return freeBoardJpaRepository.findAll()
                .stream()
                .map(FreeBoardEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FreeBoard> findById(Long freeBoardId) {
        return freeBoardJpaRepository.findById(freeBoardId).map(FreeBoardEntity::toModel);
    }

    @Override
    public void delete(Long id) {
        freeBoardJpaRepository.deleteById(id);
    }
}
