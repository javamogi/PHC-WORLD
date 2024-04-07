package com.phcworld.repository.board;

import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.repository.board.dto.DiarySelectDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiaryRepositoryCustom {
    Page<DiarySelectDto> findAllPage(UserEntity user, Pageable pageable, String searchKeyword);
}
