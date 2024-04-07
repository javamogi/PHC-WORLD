package com.phcworld.repository.dashboard;

import com.phcworld.domain.dashboard.dto.DashBoardSelectDto;
import com.phcworld.user.infrastructure.UserEntity;

public interface DashBoardRepositoryCustom {
    DashBoardSelectDto findActiveCountByUser(UserEntity user);
}
