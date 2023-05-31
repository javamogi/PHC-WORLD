package com.phcworld.repository.board;

import com.phcworld.domain.board.Diary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiaryRepositoryCustom {
    Page<Diary> findAllPage(Pageable pageable);
}
