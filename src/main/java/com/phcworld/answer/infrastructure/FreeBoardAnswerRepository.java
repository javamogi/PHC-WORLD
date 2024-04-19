package com.phcworld.answer.infrastructure;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.freeboard.domain.FreeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FreeBoardAnswerRepository {

    FreeBoardAnswer save(FreeBoardAnswer freeBoardAnswer);

    Optional<FreeBoardAnswer> findById(long id);

    void deleteById(long id);

//    Page<FreeBoardAnswer> findByFreeBoard(FreeBoard freeBoard, Pageable pageable);

    Page<FreeBoardAnswer> findByFreeBoardId(long freeBoardId, Pageable pageable);
}
