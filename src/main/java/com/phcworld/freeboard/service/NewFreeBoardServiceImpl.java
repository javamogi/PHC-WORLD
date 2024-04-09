package com.phcworld.freeboard.service;

import com.phcworld.common.infrastructure.LocalDateTimeHolder;
import com.phcworld.freeboard.controller.port.NewFreeBoardService;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.freeboard.domain.dto.FreeBoardRequest;
import com.phcworld.freeboard.service.port.FreeBoardRepository;
import com.phcworld.user.domain.User;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Builder
public class NewFreeBoardServiceImpl implements NewFreeBoardService {

    private final FreeBoardRepository freeBoardRepository;
    private final LocalDateTimeHolder localDateTimeHolder;

    @Override
    public FreeBoard register(FreeBoardRequest request, User user) {
        FreeBoard freeBoard = FreeBoard.from(request, user, localDateTimeHolder);
        return freeBoardRepository.save(freeBoard);
    }
}
