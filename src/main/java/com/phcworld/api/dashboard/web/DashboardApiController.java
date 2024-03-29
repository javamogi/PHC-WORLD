package com.phcworld.api.dashboard.web;

import com.phcworld.user.controller.port.DashBoardUser;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.user.infrastructure.UserJpaRepository;
import com.phcworld.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardApiController {

    private final DashboardService dashboardService;

    private final UserJpaRepository userRepository;

    @GetMapping("")
    public DashBoardUser requestDashboard() {
        UserEntity loginUser = userRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException());
        return dashboardService.getDashBoardUser(loginUser);
    }
}
