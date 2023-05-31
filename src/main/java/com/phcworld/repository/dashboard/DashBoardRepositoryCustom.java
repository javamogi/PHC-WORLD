package com.phcworld.repository.dashboard;

import com.phcworld.domain.dashboard.DashBoardSelectDto;
import com.phcworld.domain.user.User;

public interface DashBoardRepositoryCustom {
    DashBoardSelectDto findActiveCountByUser(User user);
}
