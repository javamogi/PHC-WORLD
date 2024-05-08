package com.phcworld.freeboard.infrastructure;

import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.service.port.FreeBoardRepository;
import com.phcworld.user.infrastructure.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        return freeBoardJpaRepository.findAllWithAnswersCount()
                .stream()
                .map(FreeBoard::from)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FreeBoard> findById(Long freeBoardId) {
        return freeBoardJpaRepository.findById(freeBoardId).map(FreeBoardEntity::toModelWithoutAnswer);
    }

    @Override
    public Optional<FreeBoard> findByIdAndAnswers(long id, int pageNum){
        Pageable pageable = PageRequest.of(pageNum - 1, 10);
        return freeBoardJpaRepository.findByIdAndAnswers(id, pageable)
                .map(FreeBoard::from);
    }

    @Override
    public List<FreeBoard> findByUser(Long userId, Long freeBoardId) {
        return freeBoardJpaRepository.findByUser(userId, freeBoardId)
                .stream()
                .map(FreeBoard::from)
                .collect(Collectors.toList());
    }
}
