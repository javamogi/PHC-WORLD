package com.phcworld.mock;

import com.phcworld.exception.model.FreeBoardNotFoundException;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.service.port.FreeBoardRepository;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeFreeBoardRepository implements FreeBoardRepository {
    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<FreeBoard> data = new ArrayList<>();
    @Override
    public FreeBoard save(FreeBoard freeBoard) {
        if(freeBoard.getId() == null || freeBoard.getId().equals(0L)){
            FreeBoard newFreeBoard = FreeBoard.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .title(freeBoard.getTitle())
                    .contents(freeBoard.getContents())
                    .writer(freeBoard.getWriter())
                    .createDate(freeBoard.getCreateDate())
                    .updateDate(freeBoard.getUpdateDate())
                    .count(freeBoard.getCount())
                    .build();
            data.add(newFreeBoard);
            return newFreeBoard;
        } else {
            data.removeIf(f -> Objects.equals(f.getId(), freeBoard.getId()));
            data.add(freeBoard);
            return freeBoard;
        }
    }

    @Override
    public List<FreeBoard> findAll() {
        return data;
    }

    @Override
    public Optional<FreeBoard> findById(Long freeBoardId) {
        return data.stream()
                .filter(f -> f.getId().equals(freeBoardId))
                .findAny();
    }

    @Override
    public Optional<FreeBoard> findByIdAndAnswers(long id, int pageNum) {
        return data.stream()
                .filter(f -> f.getId().equals(id))
                .findAny();
    }
}
