package com.phcworld.api.dashboard.web;

import com.phcworld.domain.user.DashBoardUser;
import com.phcworld.domain.user.User;
import com.phcworld.exception.model.NotFoundException;
import com.phcworld.repository.user.UserRepository;
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

    private final UserRepository userRepository;

    @GetMapping("")
    public DashBoardUser requestDashboard() {
        User loginUser = userRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException());
        return dashboardService.getDashBoardUser(loginUser);
    }
}
