package com.phcworld.repository.dashboard;

import com.phcworld.domain.dashboard.dto.DashBoardSelectDto;
import com.phcworld.domain.user.User;

public interface DashBoardRepositoryCustom {
    DashBoardSelectDto findActiveCountByUser(User user);
}
