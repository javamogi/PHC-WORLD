package com.phcworld.repository.board;

import com.phcworld.domain.board.Diary;
import com.phcworld.domain.user.User;
import com.phcworld.repository.board.dto.DiarySelectDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiaryRepositoryCustom {
    Page<DiarySelectDto> findAllPage(User user, Pageable pageable, String searchKeyword);
}
